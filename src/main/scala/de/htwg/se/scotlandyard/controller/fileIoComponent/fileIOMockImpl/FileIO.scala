package de.htwg.se.scotlandyard.controller.fileIoComponent.fileIOMockImpl

import de.htwg.se.scotlandyard.controller.fileIoComponent.FileIOInterface
import de.htwg.se.scotlandyard.model.GameModel
import de.htwg.se.scotlandyard.controller.gameInitializerComponent.GameInitializerInterface
import de.htwg.se.scotlandyard.model.players.MrX

class FileIO(override var gameInitializer: GameInitializerInterface) extends FileIOInterface{
  override def load(): GameModel = GameModel()

  override def save(gameModel: GameModel, mrX: MrX): Boolean = true
}
