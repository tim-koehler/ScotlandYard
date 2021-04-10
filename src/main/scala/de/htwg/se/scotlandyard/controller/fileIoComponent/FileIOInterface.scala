package de.htwg.se.scotlandyard.controller.fileIoComponent

import de.htwg.se.scotlandyard.gameinitializer.GameInitializerInterface
import de.htwg.se.scotlandyard.model.GameModel
import de.htwg.se.scotlandyard.model.players.MrX

trait FileIOInterface {
  var gameInitializer: GameInitializerInterface

  def load(): GameModel

  def save(gameModel: GameModel, mrX: MrX): Boolean
}
