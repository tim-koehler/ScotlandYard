package de.htwg.se.scotlandyard.controller

import de.htwg.se.scotlandyard.ScotlandYard.stationsJsonFilePath
import de.htwg.se.scotlandyard.controller.controllerBaseImpl.MoveCommand
import de.htwg.se.scotlandyard.gameinitializer.gameInitializerBaseImpl.GameInitializer
import de.htwg.se.scotlandyard.model.players.{Detective, MrX, Player}
import de.htwg.se.scotlandyard.model.{GameModel, Station, TicketType, Tickets}
import org.scalatest.{Matchers, PrivateMethodTester, WordSpec, color}

import scala.io.Source

class MoveCommandSpec extends WordSpec with Matchers with PrivateMethodTester {

  "MoveCommand" when {
    val stationsSource: String = Source.fromFile(stationsJsonFilePath).getLines.mkString
    val gameInitializer = new GameInitializer()
    var gameModel = gameInitializer.initialize(3, stationsSource)
    val moveCommand = new MoveCommand(1, 9, TicketType.Taxi)
    "doStep" should {
      "return a gameModel" in {
        gameModel = moveCommand.doStep(gameModel)
        gameModel.getMrX(gameModel.players).station.number should be (9)
      }
      "add stuck player" in {
        // setup
        val players: Vector[Player] = Vector(MrX(), Detective(station = gameModel.stations(9), tickets = Tickets()), Detective())
        var gameModelTmp = GameModel(round = 2, players = players, stations = gameModel.stations)

        // test
        gameModelTmp = moveCommand.doStep(gameModelTmp)
        gameModelTmp.round should be (4)
      }
    }
    "undoStep" should {
      "return a gameModel" in {
        gameModel = moveCommand.undoStep(gameModel)
        gameModel.getMrX(gameModel.players).station.number should be (1)
      }
      "return the same gameModel in first round" in {
        gameModel = moveCommand.undoStep(gameModel)
        gameModel.totalRound should be (1)
        gameModel.round should be (1)
        gameModel.getMrX(gameModel.players).station.number should be(1)
      }
    }
    "redoStep" should {
      "return a gameModel" in {
        gameModel = moveCommand.redoStep(gameModel)
        gameModel.getMrX(gameModel.players).station.number should be (9)
      }
    }
  }
}
