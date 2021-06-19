package de.htwg.se.scotlandyard.controller.controllerBaseImpl.rest.restBaseImpl

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpMethods, HttpRequest, HttpResponse}
import de.htwg.se.scotlandyard.controller.controllerBaseImpl.rest.RestInterface
import de.htwg.se.scotlandyard.model.GameModel
import de.htwg.se.scotlandyard.model.JsonProtocol.GameModelJsonFormat.PersistenceGameModelJsonFormat
import spray.json.enrichAny

import scala.concurrent.Future

class Rest extends  RestInterface{
  implicit val system = ActorSystem(Behaviors.empty, "SingleRequest")
  implicit val executionContext = system.executionContext

  override def callStations(): Future[HttpResponse] = {
    Http().singleRequest(HttpRequest(
      uri = "http://gameinitializer:8080/stations"))
  }

  override def callInitialize(nPlayers: Int): Future[HttpResponse] = {
    Http().singleRequest(HttpRequest(
      uri = "http://gameinitializer:8080/initialize?nPlayer=" + nPlayers))
  }
  override def callLoad(): Future[HttpResponse] = {
    Http().singleRequest(HttpRequest(
      uri = "http://persistence:8080/load"))
  }

  override def callSave(model: GameModel): Future[HttpResponse] = {
    Http().singleRequest(HttpRequest(
      uri = "http://persistence:8080/save",
      method = HttpMethods.POST,
      entity = HttpEntity(ContentTypes.`application/json`, model.toPersistenceGameModel.toJson.toString())))
  }
}
