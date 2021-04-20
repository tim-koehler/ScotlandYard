package de.htwg.se.scotlandyard.controller.controllerBaseImpl

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import com.google.inject.Inject
import de.htwg.se.scotlandyard.ScotlandYard.stationsJsonFilePath
import de.htwg.se.scotlandyard.controller.{ControllerInterface, LobbyChange, NumberOfPlayersChanged, PlayerColorChanged, PlayerMoved, PlayerNameChanged, PlayerWin, StartGame}
import de.htwg.se.scotlandyard.model.{GameModel, Station, StationType, TicketType}
import de.htwg.se.scotlandyard.model.TicketType.TicketType
import de.htwg.se.scotlandyard.model.players.{Detective, MrX, Player}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpMethod, HttpMethods, HttpRequest, HttpResponse}
import akka.http.scaladsl.server.Directives.as

import java.awt.Color
import scala.concurrent.{Await, Future}
import scala.swing.Publisher
import scala.util.{Failure, Success}
import scala.util.control.Breaks.{break, breakable}
import spray.json.DefaultJsonProtocol.{BooleanJsonFormat, IntJsonFormat, vectorFormat}
import spray.json.enrichAny
import de.htwg.se.scotlandyard.model.JsonProtocol._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.unmarshalling.Unmarshal

import scala.concurrent.duration.DurationInt

class Controller extends ControllerInterface with Publisher {

  private var stationsSource: String = ""
  private var gameModel: GameModel = _
  private val undoManager = new UndoManager()

  def initializeStations(stationsSource: String): Boolean = {
    this.stationsSource = stationsSource
    true
  }

  def initialize(nPlayers: Int = 3): GameModel = {
    implicit val system = ActorSystem(Behaviors.empty, "SingleRequest")
    implicit val executionContext = system.executionContext

    val response = Await.result(Http().singleRequest(HttpRequest(uri = "http://localhost:8082/initialize?nPlayer=" + nPlayers)), 10.seconds)
    this.gameModel = Unmarshal(response).to[GameModel].value.get.get
    publish(new NumberOfPlayersChanged)
    this.gameModel
  }

  def load(): GameModel = {
    implicit val system = ActorSystem(Behaviors.empty, "SingleRequest")
    implicit val executionContext = system.executionContext

    val response = Await.result(Http().singleRequest(HttpRequest(uri = "http://localhost:8081/fileio/load")), 10.seconds)
    Unmarshal(response).to[GameModel].value.get.get
  }

  def save(): Boolean = {
    implicit val system = ActorSystem(Behaviors.empty, "SingleRequest")
    implicit val executionContext = system.executionContext

    Http().singleRequest(HttpRequest(uri = "http://localhost:8081/fileio/save", method = HttpMethods.POST, entity = HttpEntity(ContentTypes.`application/json`, this.gameModel.toJson.toString)))
      .onComplete {
        case Success(res) => println(res)
        case Failure(_)   => sys.error("Error in FileIO service")
      }
    true
  }

  private def checkDetectiveWin(): Boolean = {
    for (dt <- gameModel.getDetectives(gameModel.players)) {
      if (dt.station.number == gameModel.getMrX(gameModel.players).station.number) {
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
    this.gameModel = undoManager.doStep(new MoveCommand(currentPlayer.station.number, newPosition, ticketType), this.gameModel)
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
    if (currentPlayer.station.number == newPosition) return false
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
        isTransportMoveValid(newPosition)(player.tickets.taxiTickets, player.station.neighbourTaxis)
      case TicketType.Bus =>
        if (player.station.stationType == StationType.Taxi) return false
        isTransportMoveValid(newPosition)(player.tickets.busTickets, player.station.neighbourBuses)
      case TicketType.Underground =>
        if (player.station.stationType != StationType.Underground) return false
        isTransportMoveValid(newPosition)(player.tickets.undergroundTickets, player.station.neighbourUndergrounds)
      case _ =>
        if (!player.equals(gameModel.players.head)) return false
        isBlackMoveValid(player, newPosition)
    }
  }

  private def isTargetStationEmpty(player: Player, newPosition: Integer): Boolean = {
    for ((p, index) <- gameModel.players.zipWithIndex) {
      breakable {
        if (index == 0 && !player.equals(gameModel.getMrX(gameModel.players))) break
        if (p.station.number == newPosition) return false
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
    if (gameModel.stations(newPosition).blackStation && currentPlayer.station.blackStation) {
      true
    } else {
      currentPlayer.station.neighbourTaxis.contains(newPosition) ||
        currentPlayer.station.neighbourBuses.contains(newPosition) ||
        currentPlayer.station.neighbourUndergrounds.contains(newPosition)
    }
  }

  // Getters and Setters
  def getCurrentPlayer: Player = {
    gameModel.getCurrentPlayer(gameModel.players, gameModel.round)
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
