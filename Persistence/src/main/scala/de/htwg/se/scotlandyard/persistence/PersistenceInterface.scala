package de.htwg.se.scotlandyard.persistence

import de.htwg.se.scotlandyard.model.PersistenceGameModel

trait PersistenceInterface {
  def load(): PersistenceGameModel

  def save(persistenceGameModel: PersistenceGameModel): Boolean

  def update(persistenceGameModel: PersistenceGameModel): Boolean

  def delete(): Boolean
}
