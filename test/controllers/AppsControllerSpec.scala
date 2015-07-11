package controllers

import org.scalatestplus.play.PlaySpec
import org.specs2.mutable._
import org.specs2.mock._
import play.api.test.FakeRequest
import play.api.mvc.{SimpleResult, Results, Controller}
import scala.concurrent._
import scala.concurrent.duration._


class AppsControllerSpec extends PlaySpec with Results {

  class TestAppsController() extends Controller with AppsController

  "AppsController#index" should {
    "should be valid" in {
      val controller = new TestAppsController()
      val result: Future[SimpleResult] = controller.index().apply(FakeRequest())
      val bodyText: String = contentAsString(result)
      bodyText mustBe "ok"
    }
  }

}
