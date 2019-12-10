package de.htwg.se.scotlandyard

import de.htwg.se.scotlandyard.aview.Tui
import de.htwg.se.scotlandyard.controller.Controller
import de.htwg.se.scotlandyard.model.core._

import scala.io.StdIn.readLine

object ScotlandYard {
  val controller = new Controller
  val tui = new Tui(controller)
  controller.notifyObservers

  def main(args: Array[String]): Unit = {

    GameMaster.startGame()

    var input: String = ""
    do {
      input = readLine()
    } while (tui.evaluateInput(input) != -1)

    println("Spiel beendet")
  }
}
