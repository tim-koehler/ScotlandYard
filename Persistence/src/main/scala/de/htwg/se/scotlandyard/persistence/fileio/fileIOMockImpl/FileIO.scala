package de.htwg.se.scotlandyard.persistence.fileio.fileIOMockImpl

import de.htwg.se.scotlandyard.model.{GameModel, PersistenceGameModel}
import de.htwg.se.scotlandyard.model.players.MrX
import de.htwg.se.scotlandyard.persistence.PersistenceInterface

class FileIO() extends PersistenceInterface{
  override def load(): PersistenceGameModel = PersistenceGameModel()

  override def save(persistenceGameModel: PersistenceGameModel): Boolean = true

  override def update(persistenceGameModel: PersistenceGameModel): Boolean = true

  override def delete(): Boolean = true
}
