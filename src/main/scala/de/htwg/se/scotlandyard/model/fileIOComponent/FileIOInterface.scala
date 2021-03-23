package de.htwg.se.scotlandyard.model.fileIOComponent

import de.htwg.se.scotlandyard.model.gameInitializerComponent.GameInitializerInterface

trait FileIOInterface {
  var gameInitializer: GameInitializerInterface
  def load(): Boolean
  def save(): Boolean
}
