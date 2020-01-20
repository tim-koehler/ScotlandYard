package de.htwg.se.scotlandyard.model.fileIOComponent.fileIOMockImpl

import de.htwg.se.scotlandyard.model.coreComponent.gameInitializerComponent.GameInitializerInterface
import de.htwg.se.scotlandyard.model.coreComponent.gameInitializerComponent.gameInitializerMockImpl.GameInitializer
import de.htwg.se.scotlandyard.model.fileIOComponent.FileIOInterface

class FileIO(override var gameInitializer: GameInitializerInterface) extends FileIOInterface{
  override def load(): Boolean = true

  override def save(): Boolean = true
}
