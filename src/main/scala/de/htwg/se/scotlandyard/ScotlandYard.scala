package de.htwg.se.scotlandyard

import com.google.inject.{Guice, Injector}
import de.htwg.se.scotlandyard.aview.Gui
import de.htwg.se.scotlandyard.aview.tui.Tui
import de.htwg.se.scotlandyard.controllerComponent.ControllerInterface
import de.htwg.se.scotlandyard.controllerComponent.controllerBaseImpl.Controller
import de.htwg.se.scotlandyard.model.core._

import scala.io.StdIn.readLine

object ScotlandYard {

  val injector = Guice.createInjector(new ScotlandYardModule)

  val controller = injector.getInstance(classOf[ControllerInterface])

  GameMaster.startGame()

  val tui = new Tui(controller)
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
