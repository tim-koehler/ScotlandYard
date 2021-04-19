package de.htwg.se.scotlandyard.aview.rest

import akka.http.scaladsl.server.Directives.{complete, concat, parameters, path, post}
import de.htwg.se.scotlandyard.ScotlandYard.{injector, stationsJsonFilePath}
import de.htwg.se.scotlandyard.controller.ControllerInterface
import de.htwg.se.scotlandyard.model.JsonProtocol.{DetectiveJsonFormat, GameModelJsonFormat, MrXJsonFormat, PlayerJsonFormat, StationJsonFormat}
import de.htwg.se.scotlandyard.model.TicketType
import de.htwg.se.scotlandyard.model.players.Player
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.server.{ExceptionHandler, Route}
import spray.json.DefaultJsonProtocol.{BooleanJsonFormat, IntJsonFormat, vectorFormat}
import spray.json.enrichAny

import scala.io.{Source, StdIn}

object Rest {

  val controller: ControllerInterface = injector.getInstance(classOf[ControllerInterface])

  def main(args: Array[String]): Unit = {

    val stationsSource: String = Source.fromFile(stationsJsonFilePath).getLines.mkString
    controller.initializeStations(stationsSource)
    controller.initialize(3)

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

        path("currentPlayer") {
          complete(controller.getCurrentPlayer)
        },
        path("load") {
          complete(controller.load())
        },
        path("save") {
          complete(controller.save().toJson)
        },
        path("mrX") {
          complete(controller.getMrX)
        },
        path("detectives") {
          complete(controller.getDetectives)
        },
        path("stations") {
          complete(controller.getStations())
        },
        path("totalRound") {
          complete(controller.getTotalRound().toJson)
        },
        path("win") {
          complete(controller.getWin().toJson)
        },
        path("gameRunning") {
          complete(controller.getGameRunning().toJson)
        },
        path("winningPlayer") {
          complete(controller.getWinningPlayer())
        },

        // POST REQUESTS (CHANGES THE STATE)
        post {
          path("move") {
            parameters("newPosition", "ticketType") { (newPosition, ticketType) => {
              complete(controller.move(newPosition.toInt, TicketType.parse(ticketType)))
            }
            }
          }
        },
        post {
          path("winGame") {
            entity(as[Player]) {
              player =>
                complete(controller.winGame(player).toJson)
            }
          }
        },
        post {
          path("initialize") {
            parameters("numberOfPlayer") { (numberOfPlayer) => {
              complete(controller.initialize(numberOfPlayer.toInt))
            }
            }
          }
        }
      )
    )

    val bindingFuture = Http().newServerAt("localhost", 8080).bind(route)
    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }
}
