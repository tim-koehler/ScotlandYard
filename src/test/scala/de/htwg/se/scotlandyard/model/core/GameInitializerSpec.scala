package de.htwg.se.scotlandyard.model.core

import de.htwg.se.scotlandyard.ScotlandYard
import de.htwg.se.scotlandyard.model.map.station.StationFactory
import org.scalatest._

class GameInitializerSpec extends WordSpec with Matchers {
  "GameInitializer" should {
    "not draw a starting position out of bounds" in {
      if(!ScotlandYard.isDebugMode) {
        GameInitializer.drawMisterXPosition() shouldBe >= (35)
        GameInitializer.drawMisterXPosition() shouldBe <= (172)

        GameInitializer.drawDetectivePosition() shouldBe >= (13)
        GameInitializer.drawDetectivePosition() shouldBe <= (174)
      } else {
        GameInitializer.drawMisterXPosition() shouldBe >= (1)
        GameInitializer.drawMisterXPosition() shouldBe <= (3)

        GameInitializer.drawDetectivePosition() shouldBe >= (1)
        GameInitializer.drawDetectivePosition() shouldBe <= (3)
      }
    }
    "try to create and init all Station types" in {
      StationFactory.createZeroIndexStation()

      var station = StationFactory.createTaxiStation(1, 1)
      station.setNeighbourTaxis(Set())
      station.setNeighbourBuses(Set())
      station.setNeighbourUndergrounds(Set())

      station = StationFactory.createBusStation(1, 1)
      station.setNeighbourTaxis(Set())
      station.setNeighbourBuses(Set())
      station.setNeighbourUndergrounds(Set())

      station = StationFactory.createUndergroundStation(1,1)
      station.setNeighbourTaxis(Set())
      station.setNeighbourBuses(Set())
      station.setNeighbourUndergrounds(Set())

      StationFactory.resetCounter()
    }
    "and test drawing player positions" in {
      GameInitializer.drawDetectivePosition(true) should be (2)
      GameInitializer.drawDetectivePosition(false) should not be (0)
    }
    "and drawing MrX position should" in {
      GameInitializer.drawMisterXPosition(true) should be(1)
      GameInitializer.drawMisterXPosition(false) should not be(0)
    }
  }
}