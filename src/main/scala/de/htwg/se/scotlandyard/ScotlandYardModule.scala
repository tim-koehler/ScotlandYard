package de.htwg.se.scotlandyard

import com.google.inject.AbstractModule
import de.htwg.se.scotlandyard.controllerComponent.ControllerInterface
import net.codingwell.scalaguice.ScalaModule

class ScotlandYardModule extends AbstractModule with ScalaModule {
    override def configure(): Unit = {
      bind[ControllerInterface].to[controllerComponent.controllerBaseImpl.Controller]
    }
}
