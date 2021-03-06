package de.htwg.se.scotlandyard.model

import de.htwg.se.scotlandyard.model.players.{Detective, MrX, Player}
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

  implicit object CoordinateJsonFormat extends RootJsonFormat[Coordinate] {
    def write(coordinate: Coordinate): JsObject = JsObject(
      "x" -> JsNumber(coordinate.x),
      "y" -> JsNumber(coordinate.y)
    )

    def read(value: JsValue): Coordinate = {
      value.asJsObject.getFields("x", "y") match {
        case Seq(JsNumber(x), JsNumber(y)) =>
          Coordinate(x.toInt, y.toInt)
        case _ => throw DeserializationException("Coordinate expected")
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
      "tuiCoordinates" -> station.tuiCoordinates.toJson,
      "guiCoordinates" -> station.guiCoordinates.toJson
    )

    def read(value: JsValue): Station = {
      value.asJsObject.getFields(
        "number",
        "stationType",
        "blackStation",
        "neighbourTaxis",
        "neighbourBuses",
        "neighbourUndergrounds",
        "tuiCoordinates",
        "guiCoordinates"
      ) match {
        case Seq(
        JsNumber(number),
        JsString(stationType),
        JsBoolean(blackStation),
        neighbourTaxis,
        neighbourBuses,
        neighbourUndergrounds,
        tuiCoordinates,
        guiCoordinates) =>
          Station(
            number.toInt,
            StationType.fromString(stationType),
            blackStation,
            neighbourTaxis.convertTo[Set[Int]],
            neighbourBuses.convertTo[Set[Int]],
            neighbourUndergrounds.convertTo[Set[Int]],
            tuiCoordinates = tuiCoordinates.convertTo[Coordinate],
            guiCoordinates = guiCoordinates.convertTo[Coordinate],
          )
        case _ => throw DeserializationException("Station expected")
      }
    }
  }

  implicit object MrXJsonFormat extends RootJsonFormat[MrX] {
    def write(mrx: MrX): JsObject = JsObject(
      "station" -> JsNumber(mrx.station),
      "tickets" -> mrx.tickets.toJson,
      "name" -> JsString(mrx.name),
      "color" -> JsString(String.format("#%02x%02x%02x", mrx.color.getRed, mrx.color.getGreen, mrx.color.getBlue)),
      "isVisible" -> JsBoolean(mrx.isVisible),
      "lastSeen" -> JsString(mrx.lastSeen),
      "history" -> mrx.history.map(e => e.toString).toJson,
      "playerType" -> mrx.playerType.get.toString.toJson)

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
        JsNumber(station),
        tickets,
        JsString(name),
        JsString(color),
        JsBoolean(isVisible),
        JsString(lastSeen),
        JsArray(history)) =>
          MrX(station.toInt, tickets.convertTo[Tickets], name, Color.decode(color), isVisible, lastSeen, history.map(e => TicketType.parse(e.convertTo[String])).toList)
        case _ => throw DeserializationException("MrX expected")
      }
    }
  }

  implicit object DetectiveJsonFormat extends RootJsonFormat[Detective] {
    def write(detective: Detective): JsObject = JsObject(
      "station" -> JsNumber(detective.station),
      "name" -> JsString(detective.name),
      "color" -> JsString(String.format("#%02x%02x%02x", detective.color.getRed, detective.color.getGreen, detective.color.getBlue)),
      "tickets" -> detective.tickets.toJson,
      "isStuck" -> JsBoolean(detective.isStuck),
      "playerType" -> detective.playerType.get.toString.toJson)

    def read(value: JsValue): Detective = {
      value.asJsObject.getFields(
        "station",
        "name",
        "color",
        "tickets",
        "isStuck"
      ) match {
        case Seq(JsNumber(station),
        JsString(name),
        JsString(color),
        tickets,
        JsBoolean(isStuck)) =>
          Detective(station.toInt, name, Color.decode(color), isStuck, tickets.convertTo[Tickets])
        case _ => throw DeserializationException("Detective expected")
      }
    }
  }

  implicit object PlayerJsonFormat extends RootJsonFormat[Player] {
    def write(player: Player): JsValue = player match {
      case mrx: MrX => mrx.toJson
      case detective: Detective => detective.toJson
    }

    def read(playerJsonObj: JsValue): Player = playerJsonObj match {
      case JsObject(fields) =>
        fields.get("playerType") match {
          case Some(JsString("mrx")) => playerJsonObj.convertTo[MrX]
          case Some(JsString("detective")) => playerJsonObj.convertTo[Detective]
          case playerType => throw DeserializationException("Unknown player type")
        }
      case _ => throw DeserializationException("Invalid Player Json")
    }
  }

  implicit object GameModelJsonFormat extends RootJsonFormat[GameModel] {
    def write(gameModel: GameModel): JsObject = JsObject(
      "stations" -> gameModel.stations.toJson,
      "players" -> gameModel.players.toJson,
      "round" -> JsNumber(gameModel.round),
      "totalRound" -> JsNumber(gameModel.totalRound),
      "win" -> JsBoolean(gameModel.win),
      "gameRunning" -> JsBoolean(gameModel.gameRunning),
      "winningPlayer" -> gameModel.winningPlayer.toJson,
      "allPlayerStuck" -> JsBoolean(gameModel.allPlayerStuck),
      "winningRound" -> JsNumber(gameModel.WINNING_ROUND),
      "mrxVisibleRounds" -> gameModel.MRX_VISIBLE_ROUNDS.toJson)

    def read(value: JsValue): GameModel = {
      value.asJsObject.getFields(
        "stations",
        "players",
        "round",
        "totalRound",
        "win",
        "gameRunning",
        "winningPlayer",
        "allPlayerStuck",
        "winningRound",
        "mrxVisibleRounds",
      ) match {
        case Seq(stations,
        players,
        JsNumber(round),
        JsNumber(totalRound),
        JsBoolean(win),
        JsBoolean(gameRunning),
        winningPlayer,
        JsBoolean(allPlayerStuck),
        JsNumber(winningRound),
        mrxVisibleRounds) =>
          GameModel(
            stations.convertTo[Vector[Station]],
            players.convertTo[Vector[Player]],
            round.toInt,
            totalRound.toInt,
            win,
            gameRunning,
            winningPlayer.convertTo[Player],
            allPlayerStuck,
            WINNING_ROUND = winningRound.toInt,
            MRX_VISIBLE_ROUNDS = mrxVisibleRounds.convertTo[Vector[Int]])
        case _ => throw DeserializationException("GameModel expected")
      }
    }

    implicit object PersistenceGameModelJsonFormat extends RootJsonFormat[PersistenceGameModel] {
      def write(gameModel: PersistenceGameModel): JsObject = JsObject(
        "players" -> gameModel.players.toJson,
        "round" -> JsNumber(gameModel.round),
        "totalRound" -> JsNumber(gameModel.totalRound),
        "win" -> JsBoolean(gameModel.win),
        "gameRunning" -> JsBoolean(gameModel.gameRunning),
        "winningPlayer" -> gameModel.winningPlayer.toJson,
        "allPlayerStuck" -> JsBoolean(gameModel.allPlayerStuck),
        "winningRound" -> JsNumber(gameModel.WINNING_ROUND),
        "mrxVisibleRounds" -> gameModel.MRX_VISIBLE_ROUNDS.toJson)

      def read(value: JsValue): PersistenceGameModel = {
        value.asJsObject.getFields(
          "players",
          "round",
          "totalRound",
          "win",
          "gameRunning",
          "winningPlayer",
          "allPlayerStuck",
          "winningRound",
          "mrxVisibleRounds",
        ) match {
          case Seq(
          players,
          JsNumber(round),
          JsNumber(totalRound),
          JsBoolean(win),
          JsBoolean(gameRunning),
          winningPlayer,
          JsBoolean(allPlayerStuck),
          JsNumber(winningRound),
          mrxVisibleRounds) =>
            PersistenceGameModel(
              players.convertTo[Vector[Player]],
              round.toInt,
              totalRound.toInt,
              win,
              gameRunning,
              winningPlayer.convertTo[Player],
              allPlayerStuck,
              WINNING_ROUND = winningRound.toInt,
              MRX_VISIBLE_ROUNDS = mrxVisibleRounds.convertTo[Vector[Int]])
          case _ => throw DeserializationException("PersistenceGameModel expected")
        }
      }
    }
  }
}