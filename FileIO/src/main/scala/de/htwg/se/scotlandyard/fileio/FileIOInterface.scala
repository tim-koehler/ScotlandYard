package de.htwg.se.scotlandyard.fileio

import de.htwg.se.scotlandyard.model.GameModel

trait FileIOInterface {
  def load(stationsFileContent: String): GameModel
  def save(gameModel: GameModel): Boolean
}
