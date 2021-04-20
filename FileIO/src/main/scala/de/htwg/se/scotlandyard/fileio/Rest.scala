package de.htwg.se.scotlandyard.fileio

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{ExceptionHandler, Route}
import de.htwg.se.scotlandyard.fileio.fileIOJsonImpl.FileIO
import de.htwg.se.scotlandyard.model.JsonProtocol.GameModelJsonFormat
import de.htwg.se.scotlandyard.model.{GameModel, Station}
import de.htwg.se.scotlandyard.model.JsonProtocol._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import spray.json.enrichAny

import scala.io.{Source, StdIn}

object Rest {
  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem(Behaviors.empty, "my-system")
    // needed for the future flatMap/onComplete in the end
    implicit val executionContext = system.executionContext

    implicit def myExceptionHandler: ExceptionHandler =
      ExceptionHandler {
        case e: Exception =>
          println("---------------- exception log start")
          e.printStackTrace()
          println("---------------- exception log end")
          complete("server shit its pants, big time")
      }

    val fileIoJson = new de.htwg.se.scotlandyard.fileio.fileIOJsonImpl.FileIO()

    val route = Route.seal(
      concat(
        post {
          path("fileio" / "save") {
            entity(as[GameModel]) { gameModel =>
              complete(fileIoJson.save(gameModel).toString)
            }
          }
        },
        path("fileio" / "load") {
          get {
            complete(fileIoJson.load("stations.json"))
          }
        }
      )
    )

    val bindingFuture = Http().newServerAt("localhost", 8081).bind(route)
    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }
}
