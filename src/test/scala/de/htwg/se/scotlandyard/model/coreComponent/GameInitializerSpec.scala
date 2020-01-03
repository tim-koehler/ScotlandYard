package de.htwg.se.scotlandyard.model.coreComponent

import de.htwg.se.scotlandyard.ScotlandYard
import de.htwg.se.scotlandyard.model.tuiMapComponent.station.StationFactory
import org.scalatest._

class GameInitializerSpec extends WordSpec with Matchers {
  "GameInitializer" should {
    "not draw a starting position out of bounds" in {
      GameInitializer.drawMisterXPosition() shouldBe >= (1) //35
      GameInitializer.drawMisterXPosition() shouldBe <= (172)

      GameInitializer.drawDetectivePosition() shouldBe >= (1) //13
      GameInitializer.drawDetectivePosition() shouldBe <= (174)
    }
    "try to create and init all Station types" in {
      StationFactory.createZeroIndexStation()

      var station = StationFactory.createTaxiStation(1, (1, 1))
      station.setNeighbourTaxis(Set())

      station = StationFactory.createBusStation(2, (1, 1))
      station.setNeighbourTaxis(Set())
      station.setNeighbourBuses(Set())

      station = StationFactory.createUndergroundStation(3,(1,1))
      station.setNeighbourTaxis(Set())
      station.setNeighbourBuses(Set())
      station.setNeighbourUndergrounds(Set())
    }
    "and test drawing player positions" in {
      GameInitializer.drawDetectivePosition(2) should be (2)
      GameInitializer.drawDetectivePosition() should not be (0)
    }
    "and drawing MrX position should" in {
      GameInitializer.drawMisterXPosition(1) should be(1)
      GameInitializer.drawMisterXPosition() should not be(0)
    }
  }
}