package de.htwg.se.scotlandyard.model.coreComponent

import de.htwg.se.scotlandyard.aview.tui.tuiMapComponent.tuiMapMockImpl.TuiMap
import de.htwg.se.scotlandyard.model.gameInitializerComponent.gameInitializerBaseImpl.GameInitializer
import de.htwg.se.scotlandyard.model.{Station, StationType, TicketType, Tickets}

import java.awt.Color
import org.scalatest._

import scala.collection.mutable
import scala.swing.Point

class GameInitializerSpec extends WordSpec with Matchers with PrivateMethodTester {
  "GameInitializer" should {
    val initializer = new GameInitializer(new TuiMap())

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