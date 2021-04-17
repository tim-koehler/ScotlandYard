package de.htwg.se.scotlandyard.model

import de.htwg.se.scotlandyard.model.TicketType.TicketType
import de.htwg.se.scotlandyard.model.players.{Detective, MrX}
import spray.json.DefaultJsonProtocol.listFormat
import spray.json.{DefaultJsonProtocol, DeserializationException, JsArray, JsBoolean, JsNumber, JsObject, JsString, JsValue, RootJsonFormat, enrichAny}

import java.awt.Color
import scala.swing.Point

object JsonProtocol extends DefaultJsonProtocol {
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
      "neighbourTaxis" -> station.neighbourTaxis.toJson,
      "neighbourBuses" -> station.neighbourBuses.toJson,
      "neighbourUndergrounds" -> station.neighbourUndergrounds.toJson,
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
      "station" -> mrx.station.toJson,
      "tickets" -> mrx.tickets.toJson,
      "name" -> JsString(mrx.name),
      "color" -> JsString(String.valueOf(mrx.color.getRGB())),
      "isVisible" -> JsBoolean(mrx.isVisible),
      "lastSeen" -> JsString(mrx.lastSeen),
      "history" -> mrx.history.map(e => e.toString).toJson)
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
        case Seq(
        station,
        tickets,
        JsString(name),
        JsString(color),
        JsBoolean(isVisible),
        JsString(lastSeen),
        JsArray(history)) =>
          MrX(station.convertTo[Station], tickets.convertTo[Tickets], name, Color.getColor(color), isVisible, lastSeen, history.asInstanceOf[List[TicketType]])
        case _ => throw DeserializationException("Station expected")
      }
    }
  }
  implicit object DetectiveJsonFormat extends RootJsonFormat[Detective] {
    def write(detective: Detective): JsObject = JsObject(
      "tickets" -> detective.tickets.toJson,
      "name" -> JsString(detective.name),
      "color" -> JsString(detective.color.toString),
      "station" -> detective.station.toJson)
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