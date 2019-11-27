package de.htwg.se.scotlandyard.model.map.station

object StationFactory {
  private var stationCounter: Integer = 0

  def createZeroIndexStation(): Station = {
    new TaxiStation(0, (1, 1))
  }

  def createTaxiStation(tuiCoords: (Integer, Integer)): Station = {
    stationCounter += 1
    new TaxiStation(stationCounter, tuiCoords)
  }

  def createBusStation(tuiCoords: (Integer, Integer)): Station = {
    stationCounter += 1
    new BusStation(stationCounter, tuiCoords)
  }

  def createUndergroundStation(tuiCoords: (Integer, Integer)): Station = {
    stationCounter += 1
    new UndergroundStation(stationCounter, tuiCoords)
  }

  def resetCounter(): Integer = {
    this.stationCounter = 0
    this.stationCounter
  }
}
