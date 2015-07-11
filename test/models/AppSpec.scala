package models

import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.specs2.mutable._
import org.specs2.mock._

class AppSpec extends PlaySpec with MockitoSugar {

  val mockApp = App(
    userId = 0L,
    name = "testUser",
    key = "key",
    secret = "someSecret",
    masterSecret = "someMasterSecret",
    appMode = 1,
    debugMode = true,
    iosCertPassword = None,
    gcmApiKey = None
  )


  "UserService#isAdmin" should {
    "be true when the role is admin" in {

    }
  }

}
