name := "plush"

version := "1.1-SNAPSHOT"

libraryDependencies := Seq(
  "net.debasishg" %% "redisclient" % "2.10",
  "org.mockito" % "mockito-core" % "1.10.19" % "test",
  "org.scalatestplus" % "play_2.10" % "1.0.0" % "test"
)

scalacOptions := Seq("-feature")

playScalaSettings
