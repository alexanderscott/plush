package controllers

import org.scalatestplus.play.PlaySpec
import org.specs2.mutable._
import org.specs2.mock._
import play.api.test.FakeRequest
import play.api.mvc.{SimpleResult, Results, Controller}
import scala.concurrent._
import scala.concurrent.duration._


class RegistrationsControllerSpec extends PlaySpec with Results {

  class TestRegistrationsController() extends Controller with RegistrationsController

  "Example Page#index" should {
    "should be valid" in {
      val controller = new TestRegistrationsController()
      val result: Future[SimpleResult] = controller.index().apply(FakeRequest())
      val bodyText: String = contentAsString(result)
      bodyText mustBe "ok"
    }
  }

}
