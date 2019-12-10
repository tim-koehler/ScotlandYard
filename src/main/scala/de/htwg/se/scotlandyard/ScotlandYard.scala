package de.htwg.se.scotlandyard

import de.htwg.se.scotlandyard.aview.{Gui, Tui}
import de.htwg.se.scotlandyard.controller.Controller
import de.htwg.se.scotlandyard.model.core._

import scala.io.StdIn.readLine

object ScotlandYard {
  
  def run(controller: Controller): Unit = {
    val tui = new Tui(controller)
    controller.notifyObservers

    GameMaster.startGame()

    var input: String = ""
    do {
      input = readLine()
    } while (tui.evaluateInput(input) != -1)

    println("Spiel beendet")
    System.exit(0)
  }
}
