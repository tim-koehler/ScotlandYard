package de.htwg.se.scotlandyard

import com.google.inject.AbstractModule
import de.htwg.se.scotlandyard.controllerComponent.ControllerInterface
import de.htwg.se.scotlandyard.model.coreComponent.gameInitializerComponent.GameInitializerInterface
import de.htwg.se.scotlandyard.model.tuiMapComponent.TuiMapInterface
import net.codingwell.scalaguice.ScalaModule

class ScotlandYardModule extends AbstractModule with ScalaModule {
    override def configure(): Unit = {
      bind[ControllerInterface].to[controllerComponent.controllerBaseImpl.Controller]
      bind[TuiMapInterface].to[model.tuiMapComponent.tuiMapBaseImpl.TuiMap]
      bind[GameInitializerInterface].to[model.coreComponent.gameInitializerComponent.gameInitializerBaseImpl.GameInitializer]
    }
}
