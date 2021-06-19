package de.htwg.se.scotlandyard.controller.controllerBaseImpl.rest.restMockImpl

import akka.http.scaladsl.model.{HttpResponse, ResponseEntity, StatusCodes}
import de.htwg.se.scotlandyard.controller.controllerBaseImpl.rest.RestInterface
import de.htwg.se.scotlandyard.model.GameModel

import scala.concurrent.Future

class Rest extends  RestInterface{
  override def callStations(): Future[HttpResponse] = Future.successful(HttpResponse.apply(StatusCodes.OK, entity = ResponseEntity(()))

  override def callInitialize(nPlayers: Int): Future[HttpResponse] = ???

  override def callLoad(): Future[HttpResponse] = ???

  override def callSave(model: GameModel): Future[HttpResponse] = ???
}
