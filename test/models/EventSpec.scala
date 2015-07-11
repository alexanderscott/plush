package models

import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.specs2.mutable._
import org.specs2.mock._

class EventSpec extends PlaySpec with MockitoSugar {
  "Event" should {
    "create from key, severity and message" in {
      val res = Event.create("testAppKey", 1.toShort, "testMessage")

    }

    "find by app key" in {
      val events = Event.findAllByAppKey("testAppKey", 0, 5)
      events.length mustBe 1
      events.head.message mustBe "testMessage"
    }

    "count by app key" in {
      val count = Event.countAllByAppKey("testAppKey")
      count mustBe 1
    }
  }

}
