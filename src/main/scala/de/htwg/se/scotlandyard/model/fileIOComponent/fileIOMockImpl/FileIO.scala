package de.htwg.se.scotlandyard.model.fileIOComponent.fileIOMockImpl

import de.htwg.se.scotlandyard.model.fileIOComponent.FileIOInterface
import de.htwg.se.scotlandyard.model.gameInitializerComponent.GameInitializerInterface
import de.htwg.se.scotlandyard.model.gameInitializerComponent.gameInitializerMockImpl.GameInitializer

class FileIO(override var gameInitializer: GameInitializerInterface) extends FileIOInterface{
  override def load(): Boolean = true

  override def save(): Boolean = true
}
