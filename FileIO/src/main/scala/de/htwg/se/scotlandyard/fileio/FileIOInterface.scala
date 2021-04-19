package de.htwg.se.scotlandyard.fileio

import de.htwg.se.scotlandyard.gameinitializer.GameInitializerInterface
import de.htwg.se.scotlandyard.model.GameModel
import de.htwg.se.scotlandyard.model.players.MrX

trait FileIOInterface {
  var gameInitializer: GameInitializerInterface

  def load(stationsFileContent: String): GameModel

  def save(gameModel: GameModel): Boolean
}
