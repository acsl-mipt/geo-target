lazy val geoTargetVersion = "0.1.0-SNAPSHOT"

lazy val commonSettings = Seq(
  organization := "ru.mipt.acsl",
  version := geoTargetVersion,
  scalaVersion := "2.11.7"
)

lazy val model = RootProject(file("../decode/model"))

lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    name := "geo-target"
  ).
  dependsOn(model)