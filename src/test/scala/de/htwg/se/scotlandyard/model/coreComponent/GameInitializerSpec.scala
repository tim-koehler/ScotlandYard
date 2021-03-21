package de.htwg.se.scotlandyard.model.coreComponent

import de.htwg.se.scotlandyard.model.{Station, StationType, TicketType, Tickets}

import java.awt.Color
import de.htwg.se.scotlandyard.model.coreComponent.gameInitializerComponent.gameInitializerBaseImpl.GameInitializer
import de.htwg.se.scotlandyard.model.coreComponent.gameInitializerComponent.stationInitializerComponent.stationInitializerBaseImpl.StationInitializer
import de.htwg.se.scotlandyard.model.tuiMapComponent.tuiMapMockImpl.TuiMap
import org.scalatest._

import scala.collection.mutable

class GameInitializerSpec extends WordSpec with Matchers with PrivateMethodTester {
  "GameInitializer" should {
    val initializer = new GameInitializer(new StationInitializer, new TuiMap())

    "init" in {
      initializer.initialize(3) should be(true)
    }
    "try to create and init all Station types" in {
      var station = new Station(0, StationType.Taxi)
      station.setNeighbourTaxis(Set())

      station = new Station(2, StationType.Bus)
      station.setNeighbourTaxis(Set())
      station.setNeighbourBuses(Set())

      station = new Station(3, StationType.Underground)
      station.setNeighbourTaxis(Set())
      station.setNeighbourBuses(Set())
      station.setNeighbourUndergrounds(Set())
    }
    "load detectives" in {
      initializer.initDetectivesFromLoad("Bobbie", 5, Tickets(10, 8, 5), Color.GREEN)
    }
    "load mrX" in {
      initializer.initMrXFromLoad("mrX", 23, true, "never", Tickets(98, 98, 98, 3), mutable.Stack(TicketType.Taxi))
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