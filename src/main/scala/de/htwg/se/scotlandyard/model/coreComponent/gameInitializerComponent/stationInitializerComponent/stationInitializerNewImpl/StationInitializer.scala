package de.htwg.se.scotlandyard.model.coreComponent.gameInitializerComponent.stationInitializerComponent.stationInitializerNewImpl

import de.htwg.se.scotlandyard.model.coreComponent.gameInitializerComponent.stationInitializerComponent.StationInitializerInterface
import de.htwg.se.scotlandyard.model.{Station, StationType}
import play.api.libs.json.{JsArray, JsValue, Json}

import scala.collection.mutable.ListBuffer
import scala.io.Source
import scala.swing.Point
import scala.util.{Failure, Success, Try}

class StationInitializer extends StationInitializerInterface {

  val guiStationCoordinateFilePath = "./resources/coordsMap.txt"
  val neighboursFilePath = "./resources/neighbours.txt"
  val tuiMapPath = "./resources/ScotlandYardMap.txt"

  val stationsJsonFilePath = "./resources/stations.json"

  override def initStations(): List[Station] = {
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
}
