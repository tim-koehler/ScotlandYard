package de.htwg.se.scotlandyard.controller

import de.htwg.se.scotlandyard.aview.tui.tuiMapComponent.tuiMapMockImpl.TuiMap
import de.htwg.se.scotlandyard.controller.controllerBaseImpl.MoveValidator
import de.htwg.se.scotlandyard.model.{GameModel, TicketType}
import de.htwg.se.scotlandyard.model.gameInitializerComponent.gameInitializerBaseImpl.GameInitializer
import org.scalatest._

class MoveValidatorSpec extends WordSpec with Matchers with PrivateMethodTester {
  "MoveValidator" when {
    val gameInitializer = new GameInitializer(new TuiMap)
    gameInitializer.initialize(5)
    "move is validated" should {
      "should return true with a black move" in {
        GameModel.getMrX.station = GameModel.stations(34)
        MoveValidator.validateMove(22, TicketType.Black) should be (true)

      }
    }
  }

}
