package de.htwg.se.scotlandyard.fileio.fileIOMockImpl

import de.htwg.se.scotlandyard.fileio.FileIOInterface
import de.htwg.se.scotlandyard.model.GameModel
import de.htwg.se.scotlandyard.model.players.MrX

class FileIO() extends FileIOInterface{
  override def load(stationsFileContent: String): GameModel = GameModel()

  override def save(gameModel: GameModel): Boolean = true
}
