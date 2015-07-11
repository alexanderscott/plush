package controllers

import org.scalatestplus.play.PlaySpec
import org.specs2.mutable._
import org.specs2.mock._
import play.api.test.FakeRequest
import play.api.mvc.{SimpleResult, Results, Controller}
import scala.concurrent._
import scala.concurrent.duration._


class ApiControllerSpec extends PlaySpec with Results {

  class TestApiController() extends Controller with ApiController

  "ApiController" should {
    "create registration" in {
      val controller = new TestApiController()
      val result: Future[SimpleResult] = controller.createRegistration("testRegistration").apply(FakeRequest())
      val r = Await.result(result, 5.seconds)
      val bodyText: String = r.toString()
      bodyText mustBe "ok"
    }

    "delete registration" in {
      val controller = new TestApiController()
      val result: Future[SimpleResult] = controller.deleteRegistration("testRegistration").apply(FakeRequest())
      val r = Await.result(result, 5.seconds)
      val bodyText: String = r.toString()
      bodyText mustBe "ok"
    }

    "create device token" in {
      val controller = new TestApiController()
      val result: Future[SimpleResult] = controller.createDeviceToken("testToken").apply(FakeRequest())
      val r = Await.result(result, 5.seconds)
      val bodyText: String = r.toString()
      bodyText mustBe "ok"
    }

    "delete device token" in {
      val controller = new TestApiController()
      val result: Future[SimpleResult] = controller.deleteDeviceToken("testToken").apply(FakeRequest())
      val r = Await.result(result, 5.seconds)
      val bodyText: String = r.toString()
      bodyText mustBe "ok"
    }
  }

}
