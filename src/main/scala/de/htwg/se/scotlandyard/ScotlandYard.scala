package de.htwg.se.scotlandyard

import com.google.inject.{Guice, Injector}
import de.htwg.se.scotlandyard.aview.Gui
import de.htwg.se.scotlandyard.controller.ControllerInterface
import de.htwg.se.scotlandyard.model.gameInitializerComponent.GameInitializerInterface

object ScotlandYard {
  val injector: Injector = Guice.createInjector(new ScotlandYardModule)
  val controller: ControllerInterface = injector.getInstance(classOf[ControllerInterface])
  var gameInitializer: GameInitializerInterface = Guice.createInjector(new ScotlandYardModule).getInstance(classOf[GameInitializerInterface])

  def main(args: Array[String]): Unit = {
    controller.initialize()
    new Gui(controller)
  }
}
