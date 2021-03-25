package de.htwg.se.scotlandyard.controller.fileIOComponent

import de.htwg.se.scotlandyard.model.GameModel
import de.htwg.se.scotlandyard.model.gameInitializerComponent.GameInitializerInterface

trait FileIOInterface {
  var gameInitializer: GameInitializerInterface
  def load(): GameModel
  def save(gameModel: GameModel): Boolean
}
