package controllers

import org.scalatestplus.play.PlaySpec
import org.specs2.mutable._
import org.specs2.mock._
import play.api.test.FakeRequest
import play.mvc.{SimpleResult, Results, Controller}
import scala.concurrent._


class UsersControllerSpec extends PlaySpec with Results {

  class TestUsersController() extends Controller with UsersController

  "Example Page#index" should {
    "should be valid" in {
      val controller = new TestUsersController()
      val result: Future[SimpleResult] = controller.index().apply(FakeRequest())
      val bodyText: String = contentAsString(result)
      bodyText mustBe "ok"
    }
  }

}
