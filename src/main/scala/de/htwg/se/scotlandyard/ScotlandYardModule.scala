package de.htwg.se.scotlandyard

import com.google.inject.AbstractModule
import de.htwg.se.scotlandyard.controller.ControllerInterface
import de.htwg.se.scotlandyard.fileio.FileIOInterface
import de.htwg.se.scotlandyard.fileio.fileIOJsonImpl.FileIO
import de.htwg.se.scotlandyard.gameinitializer.GameInitializerInterface
import de.htwg.se.scotlandyard.gameinitializer.gameInitializerBaseImpl.GameInitializer
import net.codingwell.scalaguice.ScalaModule

class ScotlandYardModule extends AbstractModule with ScalaModule {
    override def configure(): Unit = {
      bind[ControllerInterface].to[controller.controllerBaseImpl.Controller]
      bind[GameInitializerInterface].to[GameInitializer]
      bind[FileIOInterface].to[FileIO]
    }
}
