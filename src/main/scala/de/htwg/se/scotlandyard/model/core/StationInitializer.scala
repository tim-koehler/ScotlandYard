package de.htwg.se.scotlandyard.model.core

import de.htwg.se.scotlandyard.model.map.station.{Station, StationFactory}

import scala.collection.mutable.ListBuffer
import scala.io.Source
import scala.swing.Point
import scala.util.{Failure, Success, Try}

class StationInitializer {

  val guiStationCoordinateFilePath = "./src/main/scala/de/htwg/se/scotlandyard/coordsMap.txt"

  def initStations(): List[Station] = {

    var stations = createStations()

    stations = stations.sortWith((s: Station, t: Station) => s.number < t.number)

    setGuiCoordinates(stations)
    setNeighbours(stations)

    stations
  }

  private def createStations(): List[Station] = {
    var stationsBuffer = new ListBuffer[Station]()

    stationsBuffer += StationFactory.createZeroIndexStation()

    val stationPositionsList = parseStationsFromMapFile()

    for (line <- stationPositionsList) {
      stationsBuffer += parseCreateStationLine(line)
    }

    stationsBuffer.toList
  }

  private def parseStationsFromMapFile(): List[String] = {

    val path = "./src/main/scala/de/htwg/se/scotlandyard/ScotlandYardMap.txt"

    Try(Source.fromFile(path)) match {
      case Success(v) =>
        var listBuffer = new ListBuffer[String]
        val content = v.getLines().toList

        for ((line, i) <- content.zipWithIndex) {
          var lineIndex = 0
          while (lineIndex < line.length - 1) {
            val value = line.indexOf("│   │", lineIndex)
            if(value != -1){
              val stationType = parseStationType(content, value, i)
              val num = parseStationNumber(content, value, i)
              listBuffer += num + " " + stationType + " " + (i + 1) + " " + (value + 2)
              lineIndex = value + 1
            } else {
              lineIndex = line.length
            }

          }
        }
        listBuffer.toList
      case Failure(e) => List[String]()
    }
  }

  private def parseStationType(content: List[String], value: Integer, i: Integer): Char = {
    if(content(i-2).substring(value+1, value+4).contains("U")) 'u'
    else if(content(i-2).substring(value+1, value+4).contains("B")) 'b'
    else 't'
  }

  private def parseStationNumber(content: List[String], value: Integer, i: Integer): Integer = {
    content(i-1).substring(value+1, value+4).toInt
  }

  private def parseCreateStationLine(line: String): Station = {
    val args = splitLine(line)

    if(args(1).equalsIgnoreCase("t"))
      StationFactory.createTaxiStation(args(0).toInt, (args(3).toInt, args(2).toInt))
    else if(args(1).equalsIgnoreCase("b"))
      StationFactory.createBusStation(args(0).toInt, (args(3).toInt, args(2).toInt))
    else
      StationFactory.createUndergroundStation(args(0).toInt, (args(3).toInt, args(2).toInt))
  }

  private def setNeighbours(stations: List[Station]): Integer = {

    val path = "./src/main/scala/de/htwg/se/scotlandyard/neighbours.dat"

    Try(Source.fromFile(path)) match {
      case Success(v) =>
        for (line <- v.getLines()) {
          parseNeighbourStationLine(stations, line)
        }
        v.close()
      case Failure(e) => None
    }

    stations.size
  }

  private def parseNeighbourStationLine(stations: List[Station], line: String): Set[Station] = {
    val args = splitLine(line)
    var buffer = new ListBuffer[Station]()

    for(i <- 2 to (args.length - 1))
      buffer += stations(args(i).toInt)

    if(args(1).toLowerCase().equalsIgnoreCase("t")) {
      stations(args(0).toInt).setNeighbourTaxis(buffer.toSet)
      stations(args(0).toInt).neighbourTaxis
    } else if (args(1).toLowerCase().equalsIgnoreCase("b")) {
      stations(args(0).toInt).setNeighbourBuses(buffer.toSet)
      stations(args(0).toInt).neighbourBuses
    } else {
      stations(args(0).toInt).setNeighbourUndergrounds(buffer.toSet)
      stations(args(0).toInt).neighbourUndergrounds
    }
  }

  private def setGuiCoordinates(stations: List[Station]): Integer = {
    val points = parseGuiCoordinatesFromFile(guiStationCoordinateFilePath)
    for((station, index) <- stations.zipWithIndex) {
      station.guiCoords = points.get(index)
    }
    points.get.size
  }

  private def parseGuiCoordinatesFromFile(path: String): Option[List[Point]] = {
    var list: List[Point] = List()
    Try(Source.fromFile(path)) match {
      case Success(v) =>
        for (line <- v.getLines()) {
          val args = line.split("\\s").filter(_.nonEmpty)
          list = new Point(args(1).toInt, args(2).toInt) :: list
        }
        v.close()
      case Failure(e) => None
    }
    Some(list.reverse)
  }

  private def splitLine(line: String) = line.split("\\s").filter(_.nonEmpty)
}
