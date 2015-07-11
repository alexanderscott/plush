package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.libs._

import models._
import views._

trait AppsController extends Controller with Secured {

  // TODO: certificate validation
  val appForm = Form(
    tuple(
      "name" -> nonEmptyText,
      "appMode" -> number(min=0, max=1),
      "debugMode" -> boolean,
      "iosCertPassword" -> optional(text),
      "gcmApiKey" -> optional(text)
    )
  )

  def index = withAuth { username => implicit request =>
    Ok(html.apps.index(App.all))
  }

  def show(key: String) = withAuth { username => implicit request =>
    App.findByKey(key) map { app =>
      Ok(html.apps.show(app))
    } getOrElse NotFound
  }

  def add = withAuth { username => implicit request =>
    Ok(html.apps.add(appForm))
  }

  def create = withAuth(parse.multipartFormData) { username => implicit request =>
    appForm.bindFromRequest.fold(
      formWithErrors => BadRequest(html.apps.add(formWithErrors)),
      values => {
        val (name, appMode, debugMode, iosCertPassword, gcmApiKey) = values
        // TODO: use real user ID once auth is done
        App.create(1, name, appMode, debugMode, iosCertPassword, gcmApiKey) match {
          case Some(key) => { App.findByKey(key) map { app =>
              processIcon(app)
              moveCertificate(app)
              Redirect(routes.AppsController.show(app.key)).flashing("success" -> "Application successfully created")
            }
          } getOrElse InternalServerError
          case _ => InternalServerError
        }
      }
    )
  }

  def edit(key: String) = withAuth { username => implicit request =>
    App.findByKey(key) map { app =>
      val values = (app.name, app.appMode, app.debugMode, app.iosCertPassword, app.gcmApiKey)
      Ok(html.apps.edit(app, appForm.fill(values)))
    } getOrElse NotFound
  }

  def update(key: String) = withAuth(parse.multipartFormData) { username => implicit request =>
    App.findByKey(key) map { app =>
      appForm.bindFromRequest.fold(
        formWithErrors => BadRequest(html.apps.edit(app, formWithErrors)),
        values => {
          val (name, appMode, debugMode, iosCertPassword, gcmApiKey) = values
          val attrs = Map("name" -> name, "appMode" -> appMode, "debugMode" -> debugMode, "iosCertPassword" -> iosCertPassword, "gcmApiKey" -> gcmApiKey)
          app.update(attrs) match {
            case true => {
              processIcon(app)
              moveCertificate(app)
              // Stop the iOS workers of the modified app if any of the following conditions occurs:
              // * A new certificate has been uploaded
              // * The certificate password has changed
              // * The app mode has changed
              // * The debug mode has changed
              if (request.body.file("certificate").isDefined ||
                  attrs.get("iosCertPassword").get != app.iosCertPassword ||
                  attrs.get("appMode").get != app.appMode ||
                  attrs.get("debugMode").get != app.debugMode) {
                models.Push.stopIosWorkers(app)
              }
              Redirect(routes.AppsController.show(app.key)).flashing("success" -> "Application successfully updated")
            }
            case false => InternalServerError
          }
        }
      )
    } getOrElse NotFound
  }

  def delete(key: String) = withAuth { username => implicit request =>
    App.findByKey(key) map { app =>
      app.delete match {
        case true => Redirect(routes.AppsController.index).flashing("success" -> "Application successfully deleted")
        case false => InternalServerError
      }
    } getOrElse NotFound
  }

  def icon(key: String) = withAuth { username => implicit request =>
    App.findByKey(key) map { app =>
      import java.io.File
      val iconFile = app.iconFile
      if (iconFile.exists) Ok.sendFile(iconFile)
      else NotFound
    } getOrElse NotFound
  }

  private def moveCertificate(app: App)(implicit request: Request[MultipartFormData[Files.TemporaryFile]]) =
    request.body.file("certificate").map { certificate =>
      if (certificate.ref.file.length > 0) certificate.ref.moveTo(app.certFile, true)
    }

  private def processIcon(app: App)(implicit request: Request[MultipartFormData[Files.TemporaryFile]]) =
    request.body.file("icon").map { icon =>
      import java.awt.Image
      import java.awt.image.BufferedImage
      import javax.imageio.ImageIO

      val source = ImageIO.read(icon.ref.file)
      if (source != null) {
        val resized = source.getScaledInstance(57, 57, Image.SCALE_SMOOTH)
        val buffered = new BufferedImage(resized.getWidth(null), resized.getHeight(null), BufferedImage.TYPE_INT_RGB)
        buffered.getGraphics().drawImage(resized, 0, 0, null)
        ImageIO.write(buffered, "png", app.iconFile)
      }
    }
}


object AppsController extends Controller with AppsController
