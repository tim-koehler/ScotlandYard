package de.htwg.se.scotlandyard

import com.google.inject.AbstractModule
import de.htwg.se.scotlandyard.aview.tui.tuiMapComponent.TuiMapInterface
import de.htwg.se.scotlandyard.aview.tui.tuiMapComponent.tuiMapBaseImpl.TuiMap
import de.htwg.se.scotlandyard.controller.ControllerInterface
import de.htwg.se.scotlandyard.controller.fileIOComponent.fileIOJsonImpl.FileIO
import de.htwg.se.scotlandyard.controller.fileIOComponent.FileIOInterface
import de.htwg.se.scotlandyard.controller.gameInitializerComponent.GameInitializerInterface
import de.htwg.se.scotlandyard.controller.gameInitializerComponent.gameInitializerBaseImpl.GameInitializer
import de.htwg.se.scotlandyard.model.players.{Detective, MrX}
import net.codingwell.scalaguice.ScalaModule

class ScotlandYardModule extends AbstractModule with ScalaModule {
    override def configure(): Unit = {
      bind[ControllerInterface].to[controller.controllerBaseImpl.Controller]
      bind[TuiMapInterface].to[TuiMap]
      bind[GameInitializerInterface].to[GameInitializer]
      bind[FileIOInterface].to[FileIO]
    }
}
