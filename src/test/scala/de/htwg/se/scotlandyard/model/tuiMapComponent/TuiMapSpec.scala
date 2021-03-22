package de.htwg.se.scotlandyard.model.tuiMapComponent

import de.htwg.se.scotlandyard.model.coreComponent.GameMaster
import de.htwg.se.scotlandyard.model.coreComponent.gameInitializerComponent.gameInitializerBaseImpl.GameInitializer
import de.htwg.se.scotlandyard.model.tuiMapComponent.tuiMapBaseImpl.TuiMap
import org.scalatest.{Matchers, WordSpec}

class TuiMapSpec extends WordSpec with Matchers {
  "TuiMap" should {
    "toString" in {
      val tuiMap = new TuiMap()
      GameMaster.gameInitializer = new GameInitializer(tuiMap)
      GameMaster.initialize()
      tuiMap.toString should not be("")
    }
  }
}