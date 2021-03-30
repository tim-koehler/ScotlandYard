package de.htwg.se.scotlandyard.controller

import de.htwg.se.scotlandyard.ScotlandYard.stationsJsonFilePath
import de.htwg.se.scotlandyard.aview.tui.tuiMapComponent.tuiMapMockImpl.TuiMap
import de.htwg.se.scotlandyard.controller.controllerBaseImpl.MoveCommand
import de.htwg.se.scotlandyard.controller.gameInitializerComponent.gameInitializerBaseImpl.GameInitializer
import de.htwg.se.scotlandyard.model.{GameModel, TicketType}
import org.scalatest.{Matchers, PrivateMethodTester, WordSpec, color}

import scala.io.Source

class MoveCommandSpec extends WordSpec with Matchers with PrivateMethodTester {

  "MoveCommand" when {
    val stationsSource: String = Source.fromFile(stationsJsonFilePath).getLines.mkString
    val gameInitializer = new GameInitializer(new TuiMap())
    var gameModel = gameInitializer.initialize(3, stationsSource)
    val moveCommand = new MoveCommand(1, 9, TicketType.Taxi)
    "doStep" should {
      "return a gameModel" in {
        gameModel = moveCommand.doStep(gameModel)
        gameModel.getMrX(gameModel.players).station.number should be (9)
      }
    }
    "undoStep" should {
      "return a gameModel" in {
        gameModel = moveCommand.doStep(gameModel)
        gameModel.getMrX(gameModel.players).station.number should be (9)
      }
    }
  }
}
