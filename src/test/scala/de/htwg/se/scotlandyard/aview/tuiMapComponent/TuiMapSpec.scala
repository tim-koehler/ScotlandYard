package de.htwg.se.scotlandyard.aview.tuiMapComponent

import de.htwg.se.scotlandyard.aview.tui.tuiMapComponent.tuiMapBaseImpl.TuiMap
import de.htwg.se.scotlandyard.model.GameModel
import de.htwg.se.scotlandyard.model.gameInitializerComponent.gameInitializerBaseImpl.GameInitializer
import org.scalatest.{Matchers, WordSpec}

class TuiMapSpec extends WordSpec with Matchers {
  "TuiMap" should {
    "toString" in {
      val tuiMap = new TuiMap()
      val gameInitializer = new GameInitializer(tuiMap)
      gameInitializer.initialize()
      tuiMap.toString should not be("")
    }
  }
}