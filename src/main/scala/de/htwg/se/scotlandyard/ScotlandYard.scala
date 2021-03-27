package de.htwg.se.scotlandyard

import com.google.inject.{Guice, Injector}
import de.htwg.se.scotlandyard.aview.Gui
import de.htwg.se.scotlandyard.controller.ControllerInterface

object ScotlandYard {
  val injector: Injector = Guice.createInjector(new ScotlandYardModule)
  val controller: ControllerInterface = injector.getInstance(classOf[ControllerInterface])

  def main(args: Array[String]): Unit = {
    new Gui(controller)
  }
}
