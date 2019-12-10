package de.htwg.se.scotlandyard.model.map.station

object StationFactory {

  def createZeroIndexStation(): Station = {
    new TaxiStation(0, (1, 1))
  }

  def createTaxiStation(number: Integer, tuiCoords: (Integer, Integer)): Station = {
    new TaxiStation(number, tuiCoords)
  }

  def createBusStation(number: Integer,tuiCoords: (Integer, Integer)): Station = {
    new BusStation(number, tuiCoords)
  }

  def createUndergroundStation(number: Integer,tuiCoords: (Integer, Integer)): Station = {
    new UndergroundStation(number, tuiCoords)
  }
}
