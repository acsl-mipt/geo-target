package ru.mipt.acsl.geotarget.web

import scala.scalajs.js.JSApp
import scala.scalajs.js.Dynamic.{global => g}

/**
  * @author Artem Shein
  */
object GeoTargetWebJsApp extends JSApp {
  def main(): Unit = {
    println("Hello, JS world!")
    g.alert("Scala.JS works fine, dude!")
  }
}
