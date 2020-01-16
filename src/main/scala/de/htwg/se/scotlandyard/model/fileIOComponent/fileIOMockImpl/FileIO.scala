package de.htwg.se.scotlandyard.model.fileIOComponent.fileIOMockImpl

import de.htwg.se.scotlandyard.model.coreComponent.gameInitializerComponent.GameInitializerInterface
import de.htwg.se.scotlandyard.model.coreComponent.gameInitializerComponent.gameInitializerMockImpl.GameInitializer
import de.htwg.se.scotlandyard.model.fileIOComponent.FileIOInterface

class FileIO extends FileIOInterface{
  override var gameInitializer: GameInitializerInterface = new GameInitializer()

  override def load(): Boolean = true

  override def save(): Boolean = true
}
