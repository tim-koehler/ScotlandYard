package de.htwg.se.scotlandyard

import de.htwg.se.scotlandyard.aview.{Tui, TuiNew}
import de.htwg.se.scotlandyard.controller.Controller
import de.htwg.se.scotlandyard.model.core._

import scala.io.StdIn.readLine

object ScotlandYard {
  val controller = new Controller
  val tui = new Tui(controller)
  //val tui2 = new TuiNew(controller)
  controller.notifyObservers

  val isDebugMode = true

  def main(args: Array[String]): Unit = {

    GameMaster.startGame()

    var input: String = ""
    do {
      input = readLine()
    } while (tui.evaluateInput(input) != -1)

    println("Spiel beendet")
  }
}
