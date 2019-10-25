package de.htwg.se.scotlandyard.model.core

object GameMaster {
  def StartGame(): Boolean = {
    if(!GameInitializer.Initialize()) {
      false
    }
    true
  }
}
