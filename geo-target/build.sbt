lazy val geoTargetVersion = "0.1.0"

lazy val commonSettings = Seq(
  organization := "ru.mipt.acsl",
  version := geoTargetVersion,
  scalaVersion := "2.11.7"
)

lazy val decode = (project in file("../decode"))

val model = "ru.mipt.acsl" % "decode-model" % geoTargetVersion

lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    name := "geo-target",
    libraryDependencies += model
  )