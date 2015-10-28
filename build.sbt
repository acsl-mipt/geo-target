lazy val geoTargetVersion = "0.1.0"

lazy val commonSettings = Seq(
  organization := "ru.mipt.acsl",
  version := geoTargetVersion,
  scalaVersion := "2.11.7"
)

lazy val decode = project

val model = "ru.mipt.acsl" % "decode-model" % geoTargetVersion

lazy val root = (project in file("geo-target")).
  settings(commonSettings: _*).
  settings(
    name := "geo-target"
  )
