package de.htwg.se.scotlandyard.controller.fileIOComponent.fileIOMockImpl

import de.htwg.se.scotlandyard.controller.fileIOComponent.FileIOInterface
import de.htwg.se.scotlandyard.model.gameInitializerComponent.GameInitializerInterface

class FileIO(override var gameInitializer: GameInitializerInterface) extends FileIOInterface{
  override def load(): Boolean = true

  override def save(): Boolean = true
}
