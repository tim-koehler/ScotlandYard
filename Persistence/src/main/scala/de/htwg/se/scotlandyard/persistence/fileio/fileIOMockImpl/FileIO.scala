package de.htwg.se.scotlandyard.persistence.fileio.fileIOMockImpl

import de.htwg.se.scotlandyard.model.{GameModel, PersistenceGameModel}
import de.htwg.se.scotlandyard.model.players.MrX
import de.htwg.se.scotlandyard.persistence.PersistenceInterface

import scala.concurrent.Future

class FileIO() extends PersistenceInterface{
  override def load(): Future[PersistenceGameModel] = Future.successful(PersistenceGameModel())

  override def save(persistenceGameModel: PersistenceGameModel): Future[Boolean] = Future.successful(true)

  override def update(persistenceGameModel: PersistenceGameModel): Future[Boolean] = Future.successful(true)

  override def delete(): Future[Boolean] = Future.successful(true)
}
