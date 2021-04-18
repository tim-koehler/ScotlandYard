package de.htwg.se.scotlandyard.model

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._

import scala.io.StdIn
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.event.Logging
import JsonProtocol._
import akka.http.scaladsl.model.StatusCodes.InternalServerError
import akka.http.scaladsl.server.{ExceptionHandler, Route}
import de.htwg.se.scotlandyard.model.players.{Detective, MrX}
import spray.json.enrichAny

import java.awt.Color
import scala.swing.Point


object Rest {
  def main(args: Array[String]): Unit = {

    var gameModel = GameModel()

    implicit val system = ActorSystem(Behaviors.empty, "my-system")
    // needed for the future flatMap/onComplete in the end
    implicit val executionContext = system.executionContext

    implicit def myExceptionHandler: ExceptionHandler =
      ExceptionHandler {
        case e: Exception =>
          println("---------------- exception log start")
          e.printStackTrace()
          println("---------------- exception log end")
          complete("server made a boo boo")
      }

    val route = Route.seal(
      concat(
        path("currentPlayer") {
          val player = gameModel.getCurrentPlayer(gameModel.players, gameModel.round)
          player match {
            case x: MrX =>
              complete(x)
            case _ =>
              complete(player.asInstanceOf[Detective])
          }
        },
        path("previousPlayer") {
          val player = gameModel.getPreviousPlayer(gameModel.players, gameModel.round)
          player match {
            case x: MrX =>
              complete(x)
            case _ =>
              complete(player.asInstanceOf[Detective])
          }
        },
        path("station") {
          val station = Station(number = 99, stationType = StationType.Taxi, neighbourTaxis = Set(1,2,3))
          val station2 = station.toJson.convertTo[Station]
          complete(station2)
        },
        path("tickets") {
          val tickets = Tickets(1, 2, 3, 4)
          val tickets2 = tickets.toJson.convertTo[Tickets]
          complete(tickets2)
        },
        path("detective") {
          val station = Station(number = 99, stationType = StationType.Taxi, neighbourTaxis = Set(1,2,3), neighbourBuses = Set(2,3,4), neighbourUndergrounds = Set(3,4,5), tuiCoordinates = new Point(5,5), guiCoordinates = new Point(6,6))
          val detective = Detective(station = station, name = "Dt1", color = Color.green, tickets = Tickets(1,2,3,4))
          val detective2 = detective.toJson.convertTo[Detective]
          complete(detective2)
        },
        path("mrx") {
          val station = Station(number = 99, stationType = StationType.Taxi, neighbourTaxis = Set(1,2,3), neighbourBuses = Set(2,3,4), neighbourUndergrounds = Set(3,4,5), tuiCoordinates = new Point(5,5), guiCoordinates = new Point(6,6))
          val mrx = MrX(station = station, color = Color.black, tickets = Tickets(1,2,3,4), history = List(TicketType.Taxi, TicketType.Bus, TicketType.Underground, TicketType.Black))
          val mrx2 = mrx.toJson.convertTo[MrX]
          complete(mrx2)
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
