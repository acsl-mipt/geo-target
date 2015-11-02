lazy val geoTargetVersion = "0.1.0"

lazy val commonSettings = Seq(
  organization := "ru.mipt.acsl",
  version := geoTargetVersion,
  scalaVersion := "2.11.7"
)

lazy val decode = project

lazy val model = project in file("decode/model")
lazy val parser = project in file("decode/parser")

val worldwind = "gov.nasa" % "worldwind" % "2.0.0"
val worldwindx = "gov.nasa" % "worldwindx" % "2.0.0"
val logback = "ch.qos.logback" % "logback-classic" % "1.1.2"

lazy val root = (project in file("geo-target")).
  settings(commonSettings: _*).
  settings(
    name := "geo-target",
    libraryDependencies ++= Seq(worldwind, worldwindx, logback)
  ).
  dependsOn(parser)
