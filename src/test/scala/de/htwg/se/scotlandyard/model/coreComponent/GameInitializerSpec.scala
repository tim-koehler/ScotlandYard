package de.htwg.se.scotlandyard.model.coreComponent

import de.htwg.se.scotlandyard.model.{Station, StationType, TicketType, Tickets}
import java.awt.Color
import de.htwg.se.scotlandyard.model.coreComponent.gameInitializerComponent.gameInitializerBaseImpl.GameInitializer
import de.htwg.se.scotlandyard.model.tuiMapComponent.tuiMapMockImpl.TuiMap
import org.scalatest._

import scala.collection.mutable

class GameInitializerSpec extends WordSpec with Matchers with PrivateMethodTester {
  "GameInitializer" should {
    val stationInitializer = new de.htwg.se.scotlandyard.model.coreComponent.gameInitializerComponent.stationInitializerComponent.stationInitializerBaseImpl.StationInitializer()
    val initializer = new GameInitializer(stationInitializer, new TuiMap())

    "new init stations" in {
      val newStationInitializer = new de.htwg.se.scotlandyard.model.coreComponent.gameInitializerComponent.stationInitializerComponent.stationInitializerNewImpl.StationInitializer()
      val stations = newStationInitializer.initStations()
      stations.size should be(5 + 1)
      stations(5).stationType should be(StationType.Underground)
      stations(5).getNeighbourTaxis.size should be(3)
      stations(5).getNeighbourBuses.size should be(1)
      stations(5).getNeighbourUndergrounds.size should be(1)
      stations(5).guiCoords.x should be(1234)
      stations(5).guiCoords.y should be(3452)
    }

    /*"be equal for new and old implementation" in {
      val newStationInitializer = new de.htwg.se.scotlandyard.model.coreComponent.gameInitializerComponent.stationInitializerComponent.stationInitializerNewImpl.StationInitializer()
      val oldStationInitializer = new de.htwg.se.scotlandyard.model.coreComponent.gameInitializerComponent.stationInitializerComponent.stationInitializerBaseImpl.StationInitializer()
      val oldStations = oldStationInitializer.initStations()
      val newStations = newStationInitializer.initStations()

      oldStations.size should be(newStations.size)

      for((oldStation, index) <- oldStations.zipWithIndex) {
        oldStation.number should be(newStations(index).number)
        oldStation.tuiCoords should be(newStations(index).tuiCoords)
        oldStation.guiCoords should be(newStations(index).guiCoords)
        oldStation.getNeighbourTaxis should be(newStations(index).getNeighbourTaxis)
        oldStation.getNeighbourBuses should be(newStations(index).getNeighbourBuses)
        oldStation.getNeighbourUndergrounds should be(newStations(index).getNeighbourUndergrounds)
      }
    }*/

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
  }
}