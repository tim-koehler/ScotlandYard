package de.htwg.se.scotlandyard.model.coreComponent.gameInitializerComponent.stationInitializerComponent

import de.htwg.se.scotlandyard.model.tuiMapComponent.station.Station

trait StationInitializerInterface {
  def initStations(): List[Station]
}
