package ru.mipt.acsl.geotarget.web

import akka.actor.Actor

/**
  * Created by metadeus on 28.03.16.
  */
class GeoTargetActor extends Actor with Service {
  def actorRefFactory = context
  def receive = runRoute(route)
}
