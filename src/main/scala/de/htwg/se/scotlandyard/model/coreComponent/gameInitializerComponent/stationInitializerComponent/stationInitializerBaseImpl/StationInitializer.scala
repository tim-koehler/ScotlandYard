package de.htwg.se.scotlandyard.model.coreComponent.gameInitializerComponent.stationInitializerComponent.stationInitializerBaseImpl

import de.htwg.se.scotlandyard.model.StationType.StationType
import de.htwg.se.scotlandyard.model.coreComponent.GameMaster
import de.htwg.se.scotlandyard.model.{Station, StationType}
import de.htwg.se.scotlandyard.model.coreComponent.gameInitializerComponent.stationInitializerComponent.StationInitializerInterface
import play.api.libs.json.OFormat.oFormatFromReadsAndOWrites
import play.api.libs.json.{JsArray, JsValue, Json}

import scala.collection.mutable.ListBuffer
import scala.io.{Codec, Source}
import scala.swing.Point
import scala.util.{Failure, Success, Try}
import java.nio.charset.CodingErrorAction

class StationInitializer extends StationInitializerInterface {

  val guiStationCoordinateFilePath = "./resources/coordsMap.txt"
  val neighboursFilePath = "./resources/neighbours.txt"
  val tuiMapPath = "./resources/ScotlandYardMap.txt"

  val stationsJsonFilePath = "./resources/stations.json"

  override def initStations(): List[Station] = {
    var stations = createStations()

    stations = stations.sortWith((s: Station, t: Station) => s.number < t.number)

    //setTuiCoordinates(stations)
    setGuiCoordinates(stations)
    setNeighbours(stations)

    stations
  }

  def newInitStations(): List[Station] = {
    val source: String = Source.fromFile(stationsJsonFilePath).getLines.mkString
    val json = Json.parse(source)

    val jsonStations = json.as[JsArray].value
    val stationsBuffer = new ListBuffer[Station]()

    stationsBuffer += new Station(0, StationType.Taxi)

    // First loop over json file to create all Station objects
    for(jsonStation <- jsonStations ) {
      val stationType = StationType.fromString((jsonStation \ "type").as[String])
      val station = new Station((jsonStation \ "number").as[Int], stationType)
      station.tuiCoords = new Point((jsonStation \ "tuiCoordinates" \ "x").as[Int], (jsonStation \ "tuiCoordinates" \ "y").as[Int])
      station.guiCoords = new Point((jsonStation \ "guiCoordinates" \ "x").as[Int], (jsonStation \ "guiCoordinates" \ "y").as[Int])
      stationsBuffer += station
    }

    val stations = stationsBuffer.toList.sortWith((s: Station, t: Station) => s.number < t.number)

    // Second loop over json file to set all neighbours. This needs to run after the first loop because all stations need to be created before getting assigned as neighbours
    for((jsonStation, index) <- jsonStations.zipWithIndex) {
      stations(index + 1).setNeighbourTaxis(getNeighboursFor("taxi", jsonStation, stations))
      stations(index + 1).setNeighbourBuses(getNeighboursFor("bus", jsonStation, stations))
      stations(index + 1).setNeighbourUndergrounds(getNeighboursFor("underground", jsonStation, stations))
    }
    stations
  }

  private def getNeighboursFor(transportType: String, jsonStation: JsValue, stations: List[Station]): Set[Station] = {
    val neighboursIntList = (jsonStation \ "neighbours" \ transportType).as[List[Int]]
    var neighboursSet = Set[Station]()
    for(number <- neighboursIntList) {
      neighboursSet += stations(number)
    }
    neighboursSet
  }

  private def createStations(): List[Station] = {
    var stationsBuffer = new ListBuffer[Station]()

    val zeroIndexStation = new Station(0, StationType.Taxi)
    zeroIndexStation.tuiCoords = new Point(1,1)
    stationsBuffer += zeroIndexStation

    val stationPositionsList = parseStationsFromMapFile()

    for (line <- stationPositionsList) {
      stationsBuffer += parseCreateStationLine(line)
    }

    stationsBuffer.toList
  }

  private def parseStationsFromMapFile(): List[String] = {
    Try(Source.fromFile(tuiMapPath)) match {
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

    val tuiCoords = new Point(args(3).toInt, args(2).toInt)
    if (args(1).equalsIgnoreCase("t")) {
      val station = new Station(args(0).toInt, StationType.Taxi)
      station.tuiCoords = tuiCoords
      station

    } else if (args(1).equalsIgnoreCase("b")) {
      val station = new Station(args(0).toInt, StationType.Bus)
      station.tuiCoords = tuiCoords
      station
    } else {
      val station = new Station(args(0).toInt, StationType.Underground)
      station.tuiCoords = tuiCoords
      station
    }
  }

  private def setNeighbours(stations: List[Station]): Integer = {
    Try(Source.fromFile(neighboursFilePath)) match {
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
      stations(args(0).toInt).getNeighbourTaxis
    } else if (args(1).toLowerCase().equalsIgnoreCase("b")) {
      stations(args(0).toInt).setNeighbourBuses(buffer.toSet)
      stations(args(0).toInt).getNeighbourBuses
    } else {
      stations(args(0).toInt).setNeighbourUndergrounds(buffer.toSet)
      stations(args(0).toInt).getNeighbourUndergrounds
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
