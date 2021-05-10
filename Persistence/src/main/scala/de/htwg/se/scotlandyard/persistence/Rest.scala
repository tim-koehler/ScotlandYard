package de.htwg.se.scotlandyard.persistence

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives.{as, complete, concat, entity, get, path, post, delete}
import akka.http.scaladsl.server.{ExceptionHandler, Route}
import com.google.inject.{Guice, Injector}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import de.htwg.se.scotlandyard.model.GameModel
import de.htwg.se.scotlandyard.model.JsonProtocol._

object Rest {
  def main(args: Array[String]): Unit = {
    val injector: Injector = Guice.createInjector(new PersistenceModule)

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

    val persistence = injector.getInstance(classOf[PersistenceInterface])

    val route = Route.seal(
      concat(
        post {
          path("save") {
            entity(as[GameModel]) { gameModel =>
              complete(persistence.save(gameModel).toString)
            }
          }
        },
        post {
          path("update") {
            entity(as[GameModel]) { gameModel =>
              complete(persistence.update(gameModel).toString)
            }
          }
        },
        path("load") {
          get {
            complete(persistence.load())
          }
        },
        path("delete") {
          delete {
            complete(persistence.delete().toString)
          }
        },
        path("health") {
          println("Healtcheck hit")
          complete("Alive")
        }
      )
    )

    Http().newServerAt("0.0.0.0", 8080).bind(route)
    println(s"Server online at http://localhost:8080/\n")
  }
}
