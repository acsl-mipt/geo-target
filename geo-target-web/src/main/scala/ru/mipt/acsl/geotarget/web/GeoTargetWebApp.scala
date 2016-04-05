package ru.mipt.acsl.geotarget.web

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.stream.ActorMaterializer
import akka.http.scaladsl.server.Directives._

import scala.io.StdIn
import scalatags.Text.all.{head => headTag, _}

/**
  * Created by metadeus on 28.03.16.
  */
object GeoTargetWebApp extends App {

  implicit val system = ActorSystem("geo-target")

  implicit val materializer = ActorMaterializer()

  implicit val ec = system.dispatcher

  val route =
    pathPrefix("js" /) {
      get {
        getFromResourceDirectory("static/js/")
      }
    } ~
      path("") {
        get {
          complete {
            HttpResponse(entity = HttpEntity(
              ContentType(MediaTypes.`text/html`, HttpCharsets.`UTF-8`),
              html(
                headTag(
                  script(src := "/js/jquery-2.2.2.min.js"),
                  script(src := "/js/react-0.14.7.min.js"),
                  script(src := "/js/react-dom-0.14.7.min.js")),
                body(div(id := "root"), script(raw(
                  """
                    |var HelloMessage = React.createClass({
                    |  displayName: "HelloMessage",
                    |
                    |  render: function render() {
                    |    return React.createElement(
                    |      "div",
                    |      null,
                    |      "Hello ",
                    |      this.props.name
                    |    );
                    |  }
                    |});
                    |
                    |ReactDOM.render(React.createElement(HelloMessage, { name: "John" }), document.getElementById('root'));
                  """.stripMargin)))).toString()))
          }
        }
      }

  val bindingFuture = Http().bindAndHandle(route, "localhost", 39772)

  println("Server online at http://localhost:39772/\nPress RETURN to stop...")
  StdIn.readLine()

  bindingFuture
    .flatMap(_.unbind())
    .onComplete(_ => system.terminate())
}
