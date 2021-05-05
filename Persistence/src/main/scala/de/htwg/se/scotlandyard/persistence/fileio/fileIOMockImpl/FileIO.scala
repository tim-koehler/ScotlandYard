package de.htwg.se.scotlandyard.persistence.fileio.fileIOMockImpl

import de.htwg.se.scotlandyard.model.GameModel
import de.htwg.se.scotlandyard.model.players.MrX
import de.htwg.se.scotlandyard.persistence.PersistenceInterface

class FileIO() extends PersistenceInterface{
  override def load(): GameModel = GameModel()

  override def save(gameModel: GameModel): Boolean = true
}
