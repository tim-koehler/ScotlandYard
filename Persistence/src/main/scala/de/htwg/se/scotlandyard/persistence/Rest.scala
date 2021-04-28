package de.htwg.se.scotlandyard.persistence

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives.{as, complete, concat, entity, get, path, post}
import akka.http.scaladsl.server.{ExceptionHandler, Route}
import de.htwg.se.scotlandyard.model.GameModel

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

    val fileIoJson = new de.htwg.se.scotlandyard.persistence.fileio.fileIOJsonImpl.FileIO()

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
            complete(fileIoJson.load())
          }
        },
        path("health") {
          println("Healtcheck hit")
          complete("Alive")
        }
      )
    )

    Http().newServerAt("0.0.0.0", 8080).bind(route)
    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
  }
}
