package de.htwg.se.scotlandyard.model.coreComponent.gameInitializerComponent.stationInitializerComponent.stationInitializerMockImpl

import de.htwg.se.scotlandyard.model.{Station, StationType}
import de.htwg.se.scotlandyard.model.coreComponent.gameInitializerComponent.stationInitializerComponent.StationInitializerInterface

class StationInitializer extends StationInitializerInterface{
  override def initStations(): List[Station] = List(
    //StationFactory.createTaxiStation(0, (3, 3)),
    new Station(0, StationType.Taxi),
    //StationFactory.createBusStation(1, (4, 3)),
    new Station(1, StationType.Bus),
    //StationFactory.createUndergroundStation(2, (3, 7)),
    new Station(2, StationType.Underground),
    //StationFactory.createTaxiStation(3, (7, 5)))
    new Station(3, StationType.Taxi))
}
