package controllers

import play.api._
import play.api.mvc._

import models._
import views._

trait EventsController extends Controller with Secured {

  def index(key: String, offset: Int, count: Int) = withAuth { username => implicit request =>
    App.findByKey(key).map { app =>
      val events = Event.findAllByAppKey(key, offset, count)
      Ok(html.events.index(app, events, offset, count))
    } getOrElse NotFound
  }

}

object EventsController extends Controller with EventsController
