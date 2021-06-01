package de.htwg.se.scotlandyard.controller.controllerBaseImpl

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import com.google.inject.Inject
import de.htwg.se.scotlandyard.ScotlandYard.stationsJsonFilePath
import de.htwg.se.scotlandyard.controller.{ControllerInterface, GameLoaded, GameNotLoaded, LobbyChange, NumberOfPlayersChanged, PlayerColorChanged, PlayerMoved, PlayerNameChanged, PlayerWin, StartGame}
import de.htwg.se.scotlandyard.model.{GameModel, PersistenceGameModel, Station, StationType, TicketType}
import de.htwg.se.scotlandyard.model.TicketType.TicketType
import de.htwg.se.scotlandyard.model.players.{Detective, MrX, Player}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpMethod, HttpMethods, HttpRequest, HttpResponse}
import akka.http.scaladsl.server.Directives.as

import java.awt.Color
import scala.concurrent.{Await, Future}
import scala.swing.Publisher
import scala.util.{Failure, Success, Try}
import scala.util.control.Breaks.{break, breakable}
import spray.json.DefaultJsonProtocol.{BooleanJsonFormat, IntJsonFormat, vectorFormat}
import spray.json.enrichAny
import de.htwg.se.scotlandyard.model.JsonProtocol._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.unmarshalling.Unmarshal
import de.htwg.se.scotlandyard.model.JsonProtocol.GameModelJsonFormat.PersistenceGameModelJsonFormat

import scala.concurrent.duration.DurationInt


class Controller extends ControllerInterface with Publisher {

  private var stationsSource: String = ""
  private var gameModel: GameModel = _
  private val undoManager = new UndoManager()

  private val fetchedStations = {
    implicit val system = ActorSystem(Behaviors.empty, "SingleRequest")
    implicit val executionContext = system.executionContext
    var response = HttpResponse()
    try {
      response = Await.result(Http().singleRequest(HttpRequest(
        uri = "http://gameinitializer:8080/stations")),
        5.seconds)
    } catch {
      case _: Exception =>
        println("\n\n!!!GameInitializer service unavailable!!!\n\n")
        Runtime.getRuntime().halt(-1)
    }
    Unmarshal(response).to[Vector[Station]].value.get.get
  }

  def initializeStations(stationsSource: String): Boolean = {
    this.stationsSource = stationsSource
    true
  }

  def initialize(nPlayers: Int = 3): Unit = {
    implicit val system = ActorSystem(Behaviors.empty, "SingleRequest")
    implicit val executionContext = system.executionContext

    val stationsFuture = Http().singleRequest(HttpRequest(
      uri = "http://gameinitializer:8080/stations"))
    val gameModelFuture = Http().singleRequest(HttpRequest(
      uri = "http://gameinitializer:8080/initialize?nPlayer=" + nPlayers))

    val aggFut = for{
      f1Result <- stationsFuture
      f2Result <- gameModelFuture
    } yield (f1Result, f2Result)
    aggFut.onComplete {
      case Success(response) =>
        val stations = Unmarshal(response._1).to[Vector[Station]].value.get.get
        val minimalGameModel = Unmarshal(response._2).to[PersistenceGameModel].value.get.get
        publish(new NumberOfPlayersChanged)
        this.gameModel = minimalGameModel.toGameModel(stations)
    }
  }

  def load(): Option[GameModel] = {
    implicit val system = ActorSystem(Behaviors.empty, "SingleRequest")
    implicit val executionContext = system.executionContext

    var response = HttpResponse()
    try {
      response = Await.result(Http().singleRequest(HttpRequest(
        uri = "http://persistence:8080/load")),
        5.seconds)
    } catch {
      case _: Exception =>
        return None
    }
    this.gameModel = Unmarshal(response).to[PersistenceGameModel].value.get.get.toGameModel(this.gameModel.stations)
    Some(this.gameModel)
  }

  def save(): Boolean = {
    implicit val system = ActorSystem(Behaviors.empty, "SingleRequest")
    implicit val executionContext = system.executionContext

    val responseFuture: Future[HttpResponse] = Http().singleRequest(HttpRequest(
      uri = "http://persistence:8080/save",
      method = HttpMethods.POST,
      entity = HttpEntity(ContentTypes.`application/json`, this.gameModel.toPersistenceGameModel.toJson.toString())))

    responseFuture
      .onComplete {
        case Success(response) =>
          if(!Unmarshal(response).to[String].value.get.get.equalsIgnoreCase("true")) {
            return false
          }
          true
        case Failure(_)   => return false
      }
    true
  }

  private def checkDetectiveWin(): Boolean = {
    for (dt <- gameModel.getDetectives(gameModel.players)) {
      if (dt.station == gameModel.getMrX(gameModel.players).station) {
        return true
      }
    }
    false
  }

  private def checkMrXWin(): Boolean = {
    gameModel.round == gameModel.WINNING_ROUND * gameModel.players.length
  }

  def move(newPosition: Int, ticketType: TicketType): GameModel = {
    if (!validateMove(newPosition, ticketType)) {
      return this.gameModel
    }
    val currentPlayer = gameModel.getCurrentPlayer(gameModel.players, gameModel.round)
    this.gameModel = undoManager.doStep(new MoveCommand(currentPlayer.station, newPosition, ticketType), this.gameModel)
    publish(new PlayerMoved)

    if (gameModel.allPlayerStuck) winGame(gameModel.getMrX(gameModel.players))
    if (checkDetectiveWin()) winGame(gameModel.getPreviousPlayer(gameModel.players, gameModel.round))
    if (checkMrXWin()) winGame(gameModel.getMrX(gameModel.players))
    this.gameModel
  }

  def winGame(winningPlayer: Player): Boolean = {
    this.gameModel = gameModel.winGame(gameModel, winningPlayer)
    publish(new PlayerWin)
    this.gameModel.win
  }

  def undoMove(): GameModel = {
    this.gameModel = undoManager.undoStep(this.gameModel)
    publish(new PlayerMoved)
    this.gameModel
  }

  def redoMove(): GameModel = {
    this.gameModel = undoManager.redoStep(this.gameModel)
    publish(new PlayerMoved)
    this.gameModel
  }

  def startGame(): Boolean = {
    this.gameModel = gameModel.startGame(this.gameModel)
    publish(new StartGame)
    this.gameModel.gameRunning
  }

  private def validateMove(newPosition: Int, ticketType: TicketType): Boolean = {
    val currentPlayer = gameModel.getCurrentPlayer(gameModel.players, gameModel.round)
    if (!isTargetStationInBounds(newPosition)) return false
    if (currentPlayer.station == newPosition) return false
    if (!isMeanOfTransportValid(currentPlayer, newPosition, ticketType)) return false
    if (!isTargetStationEmpty(currentPlayer, newPosition)) return false
    true
  }

  private def isTargetStationInBounds(newPosition: Int): Boolean = {
    newPosition < gameModel.stations.size && newPosition > 0
  }

  private def isMeanOfTransportValid(player: Player, newPosition: Integer, ticketType: TicketType): Boolean = {
    ticketType match {
      case TicketType.Taxi =>
        isTransportMoveValid(newPosition)(player.tickets.taxiTickets, gameModel.stations(player.station).neighbourTaxis)
      case TicketType.Bus =>
        if (gameModel.stations(player.station).stationType == StationType.Taxi) return false
        isTransportMoveValid(newPosition)(player.tickets.busTickets, gameModel.stations(player.station).neighbourBuses)
      case TicketType.Underground =>
        if (gameModel.stations(player.station).stationType != StationType.Underground) return false
        isTransportMoveValid(newPosition)(player.tickets.undergroundTickets, gameModel.stations(player.station).neighbourUndergrounds)
      case _ =>
        if (!player.equals(gameModel.players.head)) return false
        isBlackMoveValid(player, newPosition)
    }
  }

  private def isTargetStationEmpty(player: Player, newPosition: Integer): Boolean = {
    for ((p, index) <- gameModel.players.zipWithIndex) {
      breakable {
        if (index == 0 && !player.equals(gameModel.getMrX(gameModel.players))) break
        if (p.station == newPosition) return false
      }
    }
    true
  }

  private def isTransportMoveValid(newPosition: Int)(tickets: Int, neighbours: Set[Int]): Boolean = {
    if (tickets <= 0) return false
    neighbours.contains(newPosition)
  }

  private def isBlackMoveValid(currentPlayer: Player, newPosition: Int): Boolean = {
    if (currentPlayer.asInstanceOf[MrX].tickets.blackTickets <= 0) return false
    if (gameModel.stations(newPosition).blackStation && gameModel.stations(currentPlayer.station).blackStation) {
      true
    } else {
      gameModel.stations(currentPlayer.station).neighbourTaxis.contains(newPosition) ||
        gameModel.stations(currentPlayer.station).neighbourBuses.contains(newPosition) ||
        gameModel.stations(currentPlayer.station).neighbourUndergrounds.contains(newPosition)
    }
  }

  // Getters and Setters
  def getCurrentPlayer: Player = {
    gameModel.getCurrentPlayer(gameModel.players, gameModel.round)
  }

  def getStationOfPlayer(player: Player): Station = {
    gameModel.stations(player.station)
  }

  def getMrX: MrX = {
    gameModel.getMrX(gameModel.players)
  }

  def getDetectives: Vector[Detective] = {
    gameModel.getDetectives(gameModel.players)
  }

  def getStations(): Vector[Station] = {
    gameModel.stations
  }

  def getTotalRound(): Int = {
    gameModel.totalRound
  }

  def getWin(): Boolean = {
    gameModel.win
  }

  def getGameRunning(): Boolean = {
    gameModel.gameRunning
  }

  def getWinningPlayer(): Player = {
    gameModel.winningPlayer
  }

  def setPlayerName(inputName: String, index: Int): Boolean = {
    gameModel = gameModel.copy(players = gameModel.players.updated(index, gameModel.players(index).setPlayerName(gameModel.players(index), inputName)))
    publish(new PlayerNameChanged)
    gameModel.players(index).name == inputName
  }

  def setPlayerColor(newColor: String, index: Int): Color = {
    gameModel = gameModel.copy(players = gameModel.players.updated(index, gameModel.players(index).setPlayerColor(gameModel.players(index), newColor)))
    publish(new PlayerColorChanged)
    gameModel.players(index).color
  }

  def updateLobby(): Boolean = {
    publish(new LobbyChange)
    true
  }
}
