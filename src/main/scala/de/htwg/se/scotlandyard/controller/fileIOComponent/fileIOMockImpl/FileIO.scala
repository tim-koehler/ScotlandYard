package de.htwg.se.scotlandyard.controller.fileIOComponent.fileIOMockImpl

import de.htwg.se.scotlandyard.controller.fileIOComponent.FileIOInterface
import de.htwg.se.scotlandyard.model.GameModel
import de.htwg.se.scotlandyard.model.gameInitializerComponent.GameInitializerInterface

class FileIO(override var gameInitializer: GameInitializerInterface) extends FileIOInterface{
  override def load(): GameModel = GameModel()

  override def save(gameModel: GameModel): Boolean = true
}
