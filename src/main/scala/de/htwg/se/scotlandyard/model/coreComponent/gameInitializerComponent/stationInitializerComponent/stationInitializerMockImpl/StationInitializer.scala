package de.htwg.se.scotlandyard.model.coreComponent.gameInitializerComponent.stationInitializerComponent.stationInitializerMockImpl

import de.htwg.se.scotlandyard.model.coreComponent.gameInitializerComponent.stationInitializerComponent.StationInitializerInterface
import de.htwg.se.scotlandyard.model.tuiMapComponent.station.{Station, StationFactory}

class StationInitializer extends StationInitializerInterface{
  override def initStations(): List[Station] = List(
    StationFactory.createTaxiStation(0, (3, 3)),
    StationFactory.createBusStation(1, (4, 3)),
    StationFactory.createUndergroundStation(2, (3, 7)),
    StationFactory.createTaxiStation(3, (7, 5)))
}
