package controllers

import org.scalatestplus.play.PlaySpec
import org.specs2.mutable._
import org.specs2.mock._
import play.api.test.FakeRequest
import play.api.mvc.{SimpleResult, Results, Controller}
import scala.concurrent._
import scala.concurrent.duration._


class PushControllerSpec extends PlaySpec with Results {

  class TestPushController() extends Controller with PushController

  "Example Page#index" should {
    "should be valid" in {
      val controller = new TestPushController()
      val result: Future[SimpleResult] = controller.index().apply(FakeRequest())
      val bodyText: String = contentAsString(result)
      bodyText mustBe "ok"
    }
  }

}
