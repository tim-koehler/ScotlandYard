package de.htwg.se.scotlandyard.controller

import de.htwg.se.scotlandyard.controller.controllerBaseImpl.MoveCommand
import de.htwg.se.scotlandyard.model.players.{Detective, MrX, Player}
import de.htwg.se.scotlandyard.model.{GameModel, Station, StationType, TicketType, Tickets}
import org.scalatest.{Matchers, PrivateMethodTester, WordSpec, color}

class MoveCommandSpec extends WordSpec with Matchers with PrivateMethodTester {

  "MoveCommand" when {
    //setup players
    val player1 = MrX(station = 1)
    val player2 = Detective(station = 2, tickets = Tickets(11, 8, 4))
    val player3 = Detective(station = 3, tickets = Tickets(11, 8, 4))
    val players: Vector[Player] = Vector(player1, player2, player3)

    //setup stations
    val station0 = Station(0)
    val station1 = Station(1, StationType.Underground, blackStation = false, Set(2), Set(), Set())
    val station2 = Station(2, StationType.Underground, blackStation = false, Set(1), Set(), Set())
    val station3 = Station(3, StationType.Taxi, blackStation = false, Set(1), Set(), Set())
    val station4 = Station(4, StationType.Bus, blackStation = false, Set(1), Set(), Set())
    val station5 = Station(5, StationType.Underground, blackStation = false, Set(1, 2), Set(), Set())
    val stations: Vector[Station] = Vector(station0, station1, station2, station3, station4, station5)

    var gameModel = GameModel(players = players, stations = stations)
    val moveCommandTaxi = new MoveCommand(1, 2, TicketType.Taxi)
    val moveCommandBus = new MoveCommand(1, 2, TicketType.Bus)
    val moveCommandUnderground = new MoveCommand(1, 2, TicketType.Underground)
    "doStep" should {
      "do step with taxi and return a gameModel" in {
        gameModel = moveCommandTaxi.doStep(gameModel)
        gameModel.getMrX(gameModel.players).station should be (2)
      }
      "do step with bus and return a gameModel" in {
        gameModel = moveCommandBus.doStep(gameModel)
        gameModel.getMrX(gameModel.players).station should be (2)
      }
      "do step with underground and return a gameModel" in {
        gameModel = moveCommandUnderground.doStep(gameModel)
        gameModel.getMrX(gameModel.players).station should be (2)
      }
      "add stuck player" in {
        // setup
        val players: Vector[Player] = Vector(MrX(), Detective(station = 9, tickets = Tickets()), Detective())
        var gameModelTmp = GameModel(round = 2, players = players, stations = gameModel.stations)

        // test
        gameModelTmp = moveCommandTaxi.doStep(gameModelTmp)
        gameModelTmp.round should be (4)
      }
    }
    "undoStep" should {
      "return a gameModel" in {
        gameModel = moveCommandTaxi.undoStep(gameModel)
        gameModel.getMrX(gameModel.players).station should be (2)
      }
      "return the same gameModel in first round" in {
        gameModel = moveCommandTaxi.undoStep(GameModel(stations = stations, players = players))
        gameModel.totalRound should be (1)
        gameModel.round should be (1)
      }
    }
    "redoStep" should {
      "return a gameModel" in {
        gameModel = moveCommandTaxi.redoStep(gameModel)
        gameModel.getMrX(gameModel.players).station should be (2)
      }
    }
  }
}
