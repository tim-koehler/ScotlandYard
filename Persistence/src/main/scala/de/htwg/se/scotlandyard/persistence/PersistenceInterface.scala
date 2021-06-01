package de.htwg.se.scotlandyard.persistence

import de.htwg.se.scotlandyard.model.PersistenceGameModel

import scala.concurrent.Future

trait PersistenceInterface {
  def load(): Future[PersistenceGameModel]

  def save(persistenceGameModel: PersistenceGameModel): Future[Boolean]

  def update(persistenceGameModel: PersistenceGameModel): Future[Boolean]

  def delete(): Future[Boolean]
}
