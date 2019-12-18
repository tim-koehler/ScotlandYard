package de.htwg.se.scotlandyard

import de.htwg.se.scotlandyard.aview.Gui
import de.htwg.se.scotlandyard.aview.tui.Tui
import de.htwg.se.scotlandyard.controller.Controller
import de.htwg.se.scotlandyard.model.core._

import scala.io.StdIn.readLine

object ScotlandYard {
  val controller = new Controller
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
