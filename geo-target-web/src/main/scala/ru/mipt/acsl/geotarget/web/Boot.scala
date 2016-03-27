package ru.mipt.acsl.geotarget.web

import akka.actor.{ActorSystem, Props}
import akka.pattern.ask
import akka.io.IO
import akka.util.Timeout
import spray.can.Http
import scala.concurrent.duration._

/**
  * Created by metadeus on 28.03.16.
  */
object Boot extends App {
  implicit val system = ActorSystem("geo-target")

  val service = system.actorOf(Props[GeoTargetActor], "geo-target-main")

  implicit val timeout = Timeout(5.seconds)

  IO(Http) ? Http.Bind(service, interface = "localhost", port = 39772)
}
