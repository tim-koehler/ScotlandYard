package de.htwg.se.scotlandyard.model.core.fileIoComponent.fileIOXmlImpl

import de.htwg.se.scotlandyard.model.core.GameMaster
import de.htwg.se.scotlandyard.model.core.fileIoComponent.FileIOInterface
import de.htwg.se.scotlandyard.model.playersComponent.playersBaseImpl.MrX

class FileIO extends FileIOInterface {
  override def load(): Unit = {

  }

  override def save(): Unit = {
    val players = GameMaster.players
    
  }

}
