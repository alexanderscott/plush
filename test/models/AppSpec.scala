package models

import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.specs2.mutable._
import org.specs2.mock._

class AppSpec extends PlaySpec with MockitoSugar {

  val mockApp = App(
    userId = 0L,
    name = "testApp",
    key = "someKey",
    secret = "someSecret",
    masterSecret = "someMasterSecret",
    appMode = 1,
    debugMode = true,
    iosCertPassword = None,
    gcmApiKey = None
  )

  class TestRedisModel extends RedisModel {

    def appExists(key: String) = {
      redis.exists(s"apps:$key")
    }

    def getApp(key: String) = {
      redis.hgetall(s"apps:$key")
    }
  }

  val testRedis = new TestRedisModel


  "App" should {
    "create from an empty map" in {
      val newApp = App.fromMap(Map())
      newApp.userId mustBe 0L
      newApp.name mustBe ""
      newApp.key mustBe ""
      newApp.secret mustBe ""
      newApp.masterSecret mustBe ""
      newApp.appMode mustBe 0L
      newApp.debugMode mustBe false
      newApp.gcmApiKey mustBe None
      newApp.iosCertPassword mustBe None
    }

    "create as a map" in {
      val newApp = App.toMap(mockApp)
      newApp("userId") mustBe Some(0L)
      newApp("name") mustBe Some("testApp")
      newApp("key") mustBe Some("someKey")
      newApp("secret") mustBe Some("someSecret")

    }

    "create an app" in {
      val keyOption = App.create(mockApp.userId, mockApp.name, mockApp.appMode, mockApp.debugMode, mockApp.iosCertPassword, mockApp.gcmApiKey)
      testRedis.appExists(mockApp.key) mustBe true
      val fetchedApp = testRedis.getApp(mockApp.key)

      fetchedApp.get("userId") mustBe Some(0L)
      fetchedApp.get("name") mustBe Some("testApp")

    }


    "find an app" in {
      val fetchedApp = App.findByKey(mockApp.key)
      fetchedApp.get mustBe mockApp

      fetchedApp.get.delete
    }

    "find all apps" in {
      val keyOption1 = App.create(mockApp.userId, mockApp.name, mockApp.appMode, mockApp.debugMode, mockApp.iosCertPassword, mockApp.gcmApiKey)
      val keyOption2 = App.create(1L, "testApp2", mockApp.appMode, mockApp.debugMode, mockApp.iosCertPassword, mockApp.gcmApiKey)
      val key = keyOption2.get
      testRedis.appExists(key) mustBe true
      val fetchedApp = testRedis.getApp(key)

      fetchedApp.get("userId") mustBe Some(1L)
      fetchedApp.get("name") mustBe Some("testApp2")

      val fetchedApps = App.all

      fetchedApps.length mustBe 2

      fetchedApps.foreach(_.delete)

    }

  }

}
