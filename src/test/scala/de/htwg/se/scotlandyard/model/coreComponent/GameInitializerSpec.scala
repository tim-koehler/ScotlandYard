package de.htwg.se.scotlandyard.model.coreComponent

import de.htwg.se.scotlandyard.ScotlandYard
import de.htwg.se.scotlandyard.model.coreComponent.gameInitializerComponent.gameInitializerBaseImpl.GameInitializer
import de.htwg.se.scotlandyard.model.tuiMapComponent.station.StationFactory
import org.scalatest._

class GameInitializerSpec extends WordSpec with Matchers with PrivateMethodTester {
  "GameInitializer" should {
    val initializer = new GameInitializer()
    "init" in {
      initializer.initialize(3) should be(true)
      /*initializer invokePrivate PrivateMethod[Int](Symbol("drawMisterXPosition"))() shouldBe >= (1)
      initializer invokePrivate PrivateMethod[Int](Symbol("drawMisterXPosition"))() shouldBe <= (172)

      initializer invokePrivate PrivateMethod[Int](Symbol("drawDetectivePosition"))() shouldBe >= (1)
      initializer invokePrivate PrivateMethod[Int](Symbol("drawDetectivePosition"))() shouldBe <= (174)*/
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
    /*  initializer invokePrivate PrivateMethod[Int](Symbol("drawDetectivePosition"))(2) should be(2) //35
      initializer invokePrivate PrivateMethod[Int](Symbol("drawDetectivePosition"))() should not be (0)*/
    }
    "and drawing MrX position should" in {
     /* initializer invokePrivate PrivateMethod[Int](Symbol("drawMisterXPosition"))(1) should be(1) //35
      initializer invokePrivate PrivateMethod[Int](Symbol("drawMisterXPosition"))() should not be (0)*/
    }
  }
}