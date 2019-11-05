package de.htwg.se.scotlandyard

import util.control.Breaks._
import de.htwg.se.scotlandyard.model.core._
import de.htwg.se.scotlandyard.model.player.Player
import jdk.nashorn.internal.ir.BreakableNode

import scala.io.StdIn.readLine

object ScotlandYard {
  val tui = new Tui()

  def main(args: Array[String]): Unit = {
    var input: String = ""

    breakable {
    do {
      println(tui.toString())
      input = readLine()
    } while (tui.evaluateInput(input) != -1)
  }

    println("Spiel beendet")
  }


}
