package de.htwg.se.scotlandyard.controller.controllerBaseImpl.rest

import akka.http.scaladsl.model.HttpResponse
import de.htwg.se.scotlandyard.model.GameModel

import scala.concurrent.Future

trait RestInterface {
  def callStations(): Future[HttpResponse]
  def callInitialize(nPlayers: Int): Future[HttpResponse]
  def callLoad(): Future[HttpResponse]
  def callSave(model: GameModel): Future[HttpResponse]
}
