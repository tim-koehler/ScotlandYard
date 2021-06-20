package de.htwg.se.scotlandyard

import com.google.inject.AbstractModule
import de.htwg.se.scotlandyard.controller.ControllerInterface
import de.htwg.se.scotlandyard.controller.controllerBaseImpl.rest.restBaseImpl.Rest
import net.codingwell.scalaguice.ScalaModule

class ScotlandYardModule extends AbstractModule with ScalaModule {
    override def configure(): Unit = {
      bind[ControllerInterface].to[controller.controllerBaseImpl.Controller]
      bind[Rest].to[controller.controllerBaseImpl.rest.restBaseImpl.Rest]
    }
}
