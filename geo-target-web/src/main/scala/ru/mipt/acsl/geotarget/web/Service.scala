package ru.mipt.acsl.geotarget.web

import spray.http.MediaTypes._
import spray.routing.HttpService

/**
  * Created by metadeus on 28.03.16.
  */
trait Service extends HttpService {
  val route =
    path("") {
      get {
        respondWithMediaType(`text/html`) {
          complete {
            <html>
              <body>
                <h1>Hello, GeoTarget!</h1>
              </body>
            </html>
          }
        }
      }
    }
}
