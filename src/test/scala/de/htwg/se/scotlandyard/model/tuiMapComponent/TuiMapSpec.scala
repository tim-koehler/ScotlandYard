package de.htwg.se.scotlandyard.model.tuiMapComponent

import de.htwg.se.scotlandyard.model.GameModel
import de.htwg.se.scotlandyard.model.gameInitializerComponent.gameInitializerBaseImpl.GameInitializer
import de.htwg.se.scotlandyard.model.tuiMapComponent.tuiMapBaseImpl.TuiMap
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