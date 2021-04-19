package de.htwg.se.scotlandyard.fileio

import de.htwg.se.scotlandyard.model.GameModel
import de.htwg.se.scotlandyard.model.players.MrX

trait FileIOInterface {
  def load(stationsFileContent: String): GameModel
  def save(gameModel: GameModel): Boolean
}
