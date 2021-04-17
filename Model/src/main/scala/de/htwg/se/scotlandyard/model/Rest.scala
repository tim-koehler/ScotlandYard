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
import spray.json.{DefaultJsonProtocol, DeserializationException, JsArray, JsNumber, JsObject, JsString, JsValue, JsonFormat, RootJsonFormat}

object ScotlandYardJsonProtocol extends DefaultJsonProtocol {
  implicit val ticketsFormat = jsonFormat4(Tickets)
  implicit val stationsFormat = jsonFormat8(Station)
  ///implicit val mrxFormat = jsonFormat7(MrX)
  //implicit val colorFormat = jsonFormat1(Col)
  //implicit val detectiveFormat = jsonFormat4(Detective)
}

object Rest {
  def main(args: Array[String]): Unit = {

    implicit val system = ActorSystem(Behaviors.empty, "my-system")
    // needed for the future flatMap/onComplete in the end
    implicit val executionContext = system.executionContext

    implicit val ticketsFormat = jsonFormat4(Tickets)

    def fetchTicket(itemId: Int): Future[Option[Tickets]] = Future {
      Some(Tickets(itemId.asInstanceOf[Int], 3, 4))
    }

    val route =
      concat(
        get {
          pathPrefix("item" / IntNumber) { id =>
            // there might be no item for a given id
            val maybeItem: Future[Option[Tickets]] = fetchTicket(id)

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
      "blackStation" -> JsString(station.blackStation.toString),
      "neighbourTaxis" -> JsArray(station.neighbourTaxis.asInstanceOf[JsArray]),
      "neighbourBuses" -> JsArray(station.neighbourBuses.asInstanceOf[JsArray]),
      "neighbourUndergrounds" -> JsArray(station.neighbourUndergrounds.asInstanceOf[JsArray]),
      "tuiCoordinates" -> JsObject(
        "x" -> JsNumber(station.tuiCoordinates.x),
        "y" -> JsNumber(station.tuiCoordinates.y)
      ),
      "guiCoordinates" -> JsObject(
        "x" -> JsNumber(station.guiCoordinates.x),
        "y" -> JsNumber(station.guiCoordinates.y)
      ))
    def read(value: JsValue): Station = {
      value.asJsObject.getFields(
        "number",
        "stationType",
        "blackStation",
        "neighbourTaxis",
        "neighbourBuses",
        "neighbourUndergrounds",
        "tuiCoordinates",
        "guiCoordinates",
      ) match {
        case Seq(JsNumber(number),
        JsString(stationType),
        JsString(blackStation),
        JsArray(neighbourTaxis),
        JsArray(neighbourBuses),
        JsArray(neighbourUndergrounds),
        JsObject(tuiCoordinates),
        JsObject(guiCoordinates)) =>
          Station(number.toInt, stationType, blackStation, neighbourTaxis, neighbourBuses, neighbourUndergrounds, tuiCoordinates, guiCoordinates)
        case _ => throw DeserializationException("Station expected")
      }
    }
  }
}