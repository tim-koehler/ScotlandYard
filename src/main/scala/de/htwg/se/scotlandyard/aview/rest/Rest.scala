package de.htwg.se.scotlandyard.aview.rest

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives.{complete, concat, parameters, path, post}
import akka.http.scaladsl.server.{ExceptionHandler, Route}
import de.htwg.se.scotlandyard.ScotlandYard.{injector, stationsJsonFilePath}
import de.htwg.se.scotlandyard.controller.ControllerInterface
import de.htwg.se.scotlandyard.model.JsonProtocol.{DetectiveJsonFormat, GameModelJsonFormat, MrXJsonFormat, StationJsonFormat}
import de.htwg.se.scotlandyard.model.TicketType
import de.htwg.se.scotlandyard.model.players.{Detective, MrX, Player}
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
          val currentPlayer = controller.getCurrentPlayer
          if (currentPlayer.isInstanceOf[MrX]) {
            complete(HttpEntity(ContentTypes.`application/json`, controller.getCurrentPlayer.asInstanceOf[MrX].toJson.toString()))
          } else {
            complete(HttpEntity(ContentTypes.`application/json`, controller.getCurrentPlayer.asInstanceOf[Detective].toJson.toString()))
          }
        },
        path("load") {
          complete(HttpEntity(ContentTypes.`application/json`, controller.load().toJson.toString()))
        },
        path("save") {
          complete(HttpEntity(ContentTypes.`application/json`, controller.save().toJson.toString()))
        },
        path("mrX") {
          complete(HttpEntity(ContentTypes.`application/json`, controller.getMrX.toJson.toString()))
        },
        path("detectives") {
          complete(HttpEntity(ContentTypes.`application/json`, controller.getDetectives.toJson.toString()))
        },
        path("stations") {
          complete(HttpEntity(ContentTypes.`application/json`, controller.getStations().toJson.toString()))
        },
        path("totalRound") {
          complete(HttpEntity(ContentTypes.`application/json`, controller.getTotalRound().toJson.toString()))
        },
        path("win") {
          complete(HttpEntity(ContentTypes.`application/json`, controller.getWin().toJson.toString()))
        },
        path("gameRunning") {
          complete(HttpEntity(ContentTypes.`application/json`, controller.getGameRunning().toJson.toString()))
        },
        path("winningPlayer") {
          if (controller.getWinningPlayer().isInstanceOf[MrX]) {
            complete(HttpEntity(ContentTypes.`application/json`, controller.getWinningPlayer().asInstanceOf[MrX].toJson.toString()))
          } else {
            complete(HttpEntity(ContentTypes.`application/json`, controller.getWinningPlayer().asInstanceOf[Detective].toJson.toString()))
          }
        },

        // POST REQUESTS (CHANGES THE STATE)
        post {
          path("move") {
            parameters("newPosition", "ticketType") { (newPosition, ticketType) => {
              complete(HttpEntity(ContentTypes.`application/json`, controller.move(newPosition.toInt, TicketType.parse(ticketType)).toJson.toString()))
            }
            }
          }
        },
        post {
          path("winGame") {
            parameters("winningPlayerName") { (winningPlayerName) => {
              var filteredWinningPlayer: Player = controller.getMrX
              if (controller.getMrX.name != winningPlayerName) {
                filteredWinningPlayer = controller.getDetectives.filter(p => p.name == winningPlayerName).head
              }
              complete(HttpEntity(ContentTypes.`application/json`, controller.winGame(filteredWinningPlayer).toJson.toString()))
            }
            }
          }
        },
        post {
          path("initialize") {
            parameters("numberOfPlayer") { (numberOfPlayer) => {
              complete(HttpEntity(ContentTypes.`application/json`, controller.initialize(numberOfPlayer.toInt).toJson.toString()))
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
