package de.htwg.se.scotlandyard.gameinitializer

import akka.http.scaladsl.server.Directives.{complete, concat, parameters, path, post}
import de.htwg.se.scotlandyard.model.JsonProtocol.{DetectiveJsonFormat, GameModelJsonFormat, MrXJsonFormat, PlayerJsonFormat, StationJsonFormat}
import de.htwg.se.scotlandyard.model.{Station, TicketType}
import de.htwg.se.scotlandyard.model.players.Player
import de.htwg.se.scotlandyard.gameinitializer.GameInitializerInterface
import de.htwg.se.scotlandyard.gameinitializer.gameInitializerBaseImpl.GameInitializer
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.server.{ExceptionHandler, Route}
import de.htwg.se.scotlandyard.model.JsonProtocol.GameModelJsonFormat.PersistenceGameModelJsonFormat
import spray.json.DefaultJsonProtocol.{BooleanJsonFormat, IntJsonFormat, vectorFormat}
import spray.json.enrichAny
import spray.json._

import scala.io.{Source, StdIn}

object Rest {

  def main(args: Array[String]): Unit = {
    val gameInitializer = new GameInitializer()
    val stations = Source.fromFile("./resources/stations.json").getLines.mkString.parseJson.convertTo[Vector[Station]]
      .sortWith((s: Station, t: Station) => s.number < t.number)

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

    val route = Route.seal(
      concat(
        // GET REQUESTS
        path("initialize") {
          parameters("nPlayer") { (nPlayer) => {
            complete(gameInitializer.initialize(nPlayer.toInt).toJson)
          }
          }
        },
        path("stations") {
          complete(stations.toJson)
        },
        path("health") {
          println("Healtcheck hit")
          complete("Alive")
        }
      )
    )


    Http().newServerAt("0.0.0.0", 8080).bind(route)
    println(s"Server online at http://localhost:8080/")
  }
}