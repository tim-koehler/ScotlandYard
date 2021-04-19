package de.htwg.se.scotlandyard.controller

import de.htwg.se.scotlandyard.ScotlandYard.{injector, stationsJsonFilePath}
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.javadsl.server.Directives
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{ExceptionHandler, Route}
import de.htwg.se.scotlandyard.model.TicketType
import de.htwg.se.scotlandyard.model.players.{Detective, MrX}
import de.htwg.se.scotlandyard.model.JsonProtocol._

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
          if(currentPlayer.isInstanceOf[MrX]) {
            complete(controller.getCurrentPlayer.asInstanceOf[MrX])
          } else {
            complete(controller.getCurrentPlayer.asInstanceOf[Detective])
          }
        },
        path("mrX") {
          complete(controller.getMrX)
        },
        path("playersList") {
          complete(controller.getPlayersList())
        },
        path("stations") {
          complete(controller.getStations())
        },
        path("totalRound") {
          complete(controller.getTotalRound())
        },
        path("win") {
          complete(controller.getWin())
        },
        path("gameRunning") {
          complete(controller.getGameRunning())
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
            parameters("winningPlayerName") { (winningPlayerName) => {
              val filteredWinningPlayers = controller.getPlayersList().filter(p => p.name == winningPlayerName)
              complete(controller.winGame(filteredWinningPlayers.head))
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
