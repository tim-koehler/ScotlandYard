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
import spray.json.DefaultJsonProtocol.{BooleanJsonFormat, IntJsonFormat, StringJsonFormat, vectorFormat}
import spray.json.enrichAny

import scala.io.{Source, StdIn}
import scala.util.{Failure, Success}

object Rest {

  def startRestService(controller: ControllerInterface): Unit = {

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
        path("health") {
          complete("alive")
        },
        path("currentPlayer") {
          complete(controller.getCurrentPlayer)
        },
        path("load") {
          complete(controller.load())
        },
        path("save") {
          complete(controller.save().toJson)
        },
        path("undo") {
          complete(controller.undoMove())
        },
        path("redo") {
          complete(controller.redoMove())
        },
        path("startGame") {
          complete(controller.startGame().toJson)
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
              controller.initialize(numberOfPlayer.toInt).onComplete {
                case Success(response) =>
                  complete(response)
                case Failure(_) =>
                  println("\n\n!!!GameInitializer service unavailable!!!\n\n")
                  Runtime.getRuntime().halt(-1)
              }
            }
            }
          }
        },
        post {
          path("setPlayerName") {
            parameters("newName", "index") { (newName, index) => {
              complete(controller.setPlayerName(newName, index.toInt).toJson)
            }
            }
          }
        },
        post {
          path("setPlayerColor") {
            parameters("newColor", "index") { (newColor, index) => {
              val color = controller.setPlayerColor(newColor, index.toInt)
              complete(String.format("#%02x%02x%02x", color.getRed, color.getGreen, color.getBlue).toJson)
            }
            }
          }
        }
      )
    )

    Http().newServerAt("0.0.0.0", 8080).bind(route)
    println(s"Server online at http://localhost:8080/")
  }
}
