package de.htwg.se.scotlandyard.controller.controllerBaseImpl.rest.restMockImpl

import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse, StatusCodes}
import de.htwg.se.scotlandyard.controller.controllerBaseImpl.rest.RestInterface
import de.htwg.se.scotlandyard.model.JsonProtocol.GameModelJsonFormat.PersistenceGameModelJsonFormat
import de.htwg.se.scotlandyard.model.{GameModel, PersistenceGameModel, Station, StationType, Tickets}
import de.htwg.se.scotlandyard.model.JsonProtocol.StationJsonFormat
import de.htwg.se.scotlandyard.model.players.{Detective, MrX, Player}
import spray.json.DefaultJsonProtocol.{BooleanJsonFormat, vectorFormat}
import spray.json.enrichAny

import scala.concurrent.Future

class Rest extends RestInterface{
  val station0 = Station(0)
  var station1 = Station(1, StationType.Underground, blackStation = false, Set(6), Set(), Set())
  var station2 = Station(2, StationType.Taxi, blackStation = false, Set(1), Set(), Set())
  val station3 = Station(3, StationType.Taxi, blackStation = false, Set(1), Set(), Set())
  val station4 = Station(4, StationType.Bus, blackStation = false, Set(1), Set(), Set())
  var station5 = Station(5, StationType.Underground, blackStation = false, Set(1, 2), Set(), Set())
  val station6 = Station(6, StationType.Underground, blackStation = true, Set(7), Set(), Set())
  var station7 = Station(7, StationType.Underground, blackStation = true, Set(6, 2), Set(), Set())
  station1 = station1.copy(neighbourTaxis = Set(2, 3, 4, 5), neighbourBuses = Set(5, 4), neighbourUndergrounds = Set(5))
  station2 = station2.copy(neighbourTaxis = Set(2, 3, 4, 5), neighbourBuses = Set(5, 4), neighbourUndergrounds = Set(5))
  station5 = station5.copy(neighbourTaxis = Set(1, 2, 3, 4), neighbourBuses = Set(1, 4), neighbourUndergrounds = Set(1))

  val player1 = MrX(station = 1)
  val player2 = Detective(station = 2, tickets = Tickets(11, 8, 4))
  val player3 = Detective(station = 3, tickets = Tickets(11, 8, 3))
  val players: Vector[Player] = Vector(player1, player2, player3)
  val stations: Vector[Station] = Vector(station0, station1, station2, station3, station4, station5)
  val persistenceGameModel = PersistenceGameModel(players = players)

  override def callStations(): Future[HttpResponse] = Future.successful(HttpResponse(StatusCodes.OK, Seq(), HttpEntity.apply(ContentTypes.`application/json`, stations.toJson.toString().getBytes())))

  override def callInitialize(nPlayers: Int): Future[HttpResponse] = Future.successful(HttpResponse(StatusCodes.OK, Seq(), HttpEntity.apply(ContentTypes.`application/json`, persistenceGameModel.toJson.toString().getBytes())))

  override def callLoad(): Future[HttpResponse] = Future.successful(HttpResponse(StatusCodes.OK, Seq(), HttpEntity.apply(ContentTypes.`application/json`, persistenceGameModel.toJson.toString().getBytes())))

  override def callSave(model: GameModel): Future[HttpResponse] = Future.successful(HttpResponse(StatusCodes.OK, Seq(), HttpEntity.apply(ContentTypes.`application/json`, true.toJson.toString().getBytes())))
}
