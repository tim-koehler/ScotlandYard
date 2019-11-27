package de.htwg.se.scotlandyard.model.map.station

object StationFactory {
  private var taxiStationCounter = 0
  private var busStationCounter = 0
  private var undergroundStationCounter = 0

  def createTaxiStation(tuiCoords: (Integer, Integer)): Station = {
    taxiStationCounter += 1
    new TaxiStation(taxiStationCounter, tuiCoords)
  }

  def createBusStation(tuiCoords: (Integer, Integer)): Station = {
    busStationCounter += 1
    new BusStation(busStationCounter, tuiCoords)
  }

  def createUndergroundStation(tuiCoords: (Integer, Integer)): Station = {
    undergroundStationCounter += 1
    new UndergroundStation(undergroundStationCounter, tuiCoords)
  }
}
