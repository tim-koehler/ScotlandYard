package de.htwg.se.scotlandyard

import util.control.Breaks._
import de.htwg.se.scotlandyard.model.core._

import scala.io.StdIn.readLine

object ScotlandYard {
  val tui = new Tui()

  def main(args: Array[String]): Unit = {

    MapRenderer.init()

    var input: String = ""
    do {
      println(tui.toString())
      input = readLine()
    } while (tui.evaluateInput(input) != -1)

    println("Spiel beendet")
  }

}
