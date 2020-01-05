package de.htwg.se.scotlandyard

import com.google.inject.Guice
import de.htwg.se.scotlandyard.aview.Gui
import de.htwg.se.scotlandyard.aview.tui.Tui
import de.htwg.se.scotlandyard.controllerComponent.ControllerInterface
import de.htwg.se.scotlandyard.model.coreComponent._
import de.htwg.se.scotlandyard.model.coreComponent.gameInitializerComponent.GameInitializerInterface
import de.htwg.se.scotlandyard.model.tuiMapComponent.TuiMapInterface

import scala.io.StdIn.readLine

object ScotlandYard {

  val injector = Guice.createInjector(new ScotlandYardModule)

  val controller = injector.getInstance(classOf[ControllerInterface])
  val tuiMap = injector.getInstance(classOf[TuiMapInterface])
  val gameInitializer = injector.getInstance(classOf[GameInitializerInterface])

  GameMaster.initialize(gameInitializer = gameInitializer)
  val tui = new Tui(controller, tuiMap)
  val gui = new Gui(controller)

  def main(args: Array[String]): Unit = {

    var input: String = ""
    do {
      input = readLine()
    } while (tui.evaluateInput(input) != -1)

    println("Spiel beendet")
    System.exit(0)
  }
}
