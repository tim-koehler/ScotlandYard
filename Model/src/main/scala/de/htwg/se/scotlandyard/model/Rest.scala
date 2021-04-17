package de.htwg.se.scotlandyard.model

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import spray.json.DefaultJsonProtocol.{IntJsonFormat, jsonFormat4, listFormat}

import scala.concurrent.Future
import scala.io.StdIn
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import de.htwg.se.scotlandyard.model.players.{Detective, MrX}
import spray.json.{DefaultJsonProtocol, DeserializationException, JsArray, JsBoolean, JsNumber, JsObject, JsString, JsValue, JsonFormat, RootJsonFormat}
import ScotlandYardJsonProtocol._
import de.htwg.se.scotlandyard.model.TicketType.TicketType

import java.awt.Color
import scala.swing.Point

object ScotlandYardJsonProtocol extends DefaultJsonProtocol {
  implicit object TicketsJsonFormat extends RootJsonFormat[Tickets] {
    def write(tickets: Tickets): JsObject = JsObject(
      "taxiTickets" -> JsNumber(tickets.taxiTickets),
      "busTickets" -> JsNumber(tickets.busTickets),
      "undergroundTickets" -> JsNumber(tickets.undergroundTickets),
      "blackTickets" -> JsNumber(tickets.blackTickets)
    )
    def read(value: JsValue): Tickets = {
      value.asJsObject.getFields("taxiTickets", "busTickets", "undergroundTickets", "blackTickets") match {
        case Seq(JsNumber(taxiTickets), JsNumber(busTickets), JsNumber(undergroundTickets), JsNumber(blackTickets)) =>
          Tickets(taxiTickets.toInt, busTickets.toInt, undergroundTickets.toInt, blackTickets.toInt)
        case _ => throw DeserializationException("Tickets expected")
      }
    }
  }
  implicit object StationJsonFormat extends RootJsonFormat[Station] {
    def write(station: Station): JsObject = JsObject(
      "number" -> JsNumber(station.number),
      "stationType" -> JsString(station.stationType.toString),
      "blackStation" -> JsBoolean(station.blackStation),
      "neighbourTaxis" -> JsArray(station.neighbourTaxis.asInstanceOf[JsArray]),
      "neighbourBuses" -> JsArray(station.neighbourBuses.asInstanceOf[JsArray]),
      "neighbourUndergrounds" -> JsArray(station.neighbourUndergrounds.asInstanceOf[JsArray]),
      "tuiCoordinatesX" -> JsNumber(station.tuiCoordinates.x),
      "tuiCoordinatesY" -> JsNumber(station.tuiCoordinates.y),
      "guiCoordinatesX" -> JsNumber(station.guiCoordinates.x),
      "guiCoordinatesY" -> JsNumber(station.guiCoordinates.y)
      )
    def read(value: JsValue): Station = {
      value.asJsObject.getFields(
        "number",
        "stationType",
        "blackStation",
        "neighbourTaxis",
        "neighbourBuses",
        "neighbourUndergrounds",
        "tuiCoordinatesX",
        "tuiCoordinatesY",
        "guiCoordinatesX",
        "guiCoordinatesY"
      ) match {
        case Seq(
        JsNumber(number),
        JsString(stationType),
        JsBoolean(blackStation),
        JsArray(neighbourTaxis),
        JsArray(neighbourBuses),
        JsArray(neighbourUndergrounds),
        JsNumber(tuiCoordinatesX),
        JsNumber(tuiCoordinatesY),
        JsNumber(guiCoordinatesX),
        JsNumber(guiCoordinatesY)) =>
          Station(
            number.toInt,
            StationType.fromString(stationType),
            blackStation,
            neighbourTaxis.asInstanceOf[Set[Int]],
            neighbourBuses.asInstanceOf[Set[Int]],
            neighbourUndergrounds.asInstanceOf[Set[Int]],
            new Point(tuiCoordinatesX.toInt, tuiCoordinatesY.toInt),
            new Point(guiCoordinatesX.toInt, guiCoordinatesY.toInt)
          )
        case _ => throw DeserializationException("Station expected")
      }
    }
  }
  implicit object MrXJsonFormat extends RootJsonFormat[MrX] {
    def write(mrx: MrX): JsObject = JsObject(
      "station" -> JsObject[Station](mrx.station),
      "tickets" -> JsObject[Tickets](mrx.tickets),
      "name" -> JsString(mrx.name),
      "color" -> JsString(mrx.color.toString),
      "isVisible" -> JsBoolean(mrx.isVisible),
      "lastSeen" -> JsString(mrx.lastSeen),
      "history" -> JsArray(mrx.history.asInstanceOf[JsArray]))
    def read(value: JsValue): MrX = {
      value.asJsObject.getFields(
        "station",
        "tickets",
        "name",
        "color",
        "isVisible",
        "lastSeen",
        "history",
      ) match {
        case Seq(JsObject(station),
        JsObject(tickets),
        JsString(name),
        JsObject(color),
        JsBoolean(isVisible),
        JsString(lastSeen),
        JsArray(history)) =>
          MrX(station.asInstanceOf[Station], tickets.asInstanceOf[Tickets], name, color.asInstanceOf[Color], isVisible, lastSeen, history.asInstanceOf[List[TicketType]])
        case _ => throw DeserializationException("Station expected")
      }
    }
  }
  implicit object DetectiveJsonFormat extends RootJsonFormat[Detective] {
    def write(detective: Detective): JsObject = JsObject(
      "tickets" -> JsObject[Tickets](detective.tickets),
      "name" -> JsString(detective.name),
      "color" -> JsString(detective.color.toString),
      "station" -> JsObject[Station](detective.station))
    def read(value: JsValue): Detective = {
      value.asJsObject.getFields(
        "station",
        "name",
        "color",
        "tickets",
      ) match {
        case Seq(JsObject(station),
        JsString(name),
        JsObject(color),
        JsObject(tickets)) =>
          Detective(station.asInstanceOf[Station], name, color.asInstanceOf[Color], tickets.asInstanceOf[Tickets])
        case _ => throw DeserializationException("Station expected")
      }
    }
  }
}

object Rest {
  def main(args: Array[String]): Unit = {

    implicit val system = ActorSystem(Behaviors.empty, "my-system")
    // needed for the future flatMap/onComplete in the end
    implicit val executionContext = system.executionContext

    def fetchTicket(itemId: Int): Future[Option[Station]] = Future {
      Some(Station(itemId.asInstanceOf[Int]))
    }

    val route =
      concat(
        get {
          pathPrefix("item" / IntNumber) { id =>
            // there might be no item for a given id
            val maybeItem: Future[Option[Station]] = fetchTicket(id)
            onSuccess(maybeItem) {
              case Some(item) => complete(item)
              case None       => complete(StatusCodes.NotFound)
            }
          }
        }
      )

    val bindingFuture = Http().newServerAt("localhost", 8080).bind(route)
    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }
}
