package de.htwg.se.scotlandyard.model.coreComponent.gameInitializerComponent.stationInitializerComponent

import de.htwg.se.scotlandyard.model.Station

trait StationInitializerInterface {
  def initStations(): List[Station]
}
