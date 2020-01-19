package de.htwg.se.scotlandyard.model.coreComponent

import java.awt.Color

import de.htwg.se.scotlandyard.model.coreComponent.gameInitializerComponent.gameInitializerBaseImpl.GameInitializer
import de.htwg.se.scotlandyard.model.coreComponent.gameInitializerComponent.stationInitializerComponent.stationInitializerMockImpl.StationInitializer
import de.htwg.se.scotlandyard.model.tuiMapComponent.station.StationFactory
import de.htwg.se.scotlandyard.model.tuiMapComponent.tuiMapMockImpl.TuiMap
import de.htwg.se.scotlandyard.util.{TicketType, Tickets}
import org.scalatest._

class GameInitializerSpec extends WordSpec with Matchers with PrivateMethodTester {
  "GameInitializer" should {
    val initializer = new GameInitializer(new StationInitializer, new TuiMap())

    "init" in {
      initializer.initialize(3) should be(true)
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
    "load detectives" in {
      initializer.initDetectivesFromLoad("Bobbie", 5, Tickets(10, 8, 5), Color.GREEN)
    }
    "load mrX" in {
      initializer.initMrXFromLoad("mrX", 23, true, "never", Tickets(98, 98, 98, 3, 2), List(TicketType.Taxi))
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