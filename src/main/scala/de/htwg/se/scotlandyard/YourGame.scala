package de.htwg.se.scotlandyard

import de.htwg.se.scotlandyard.model.Player

object YourGame {
  def main(args: Array[String]): Unit = {
    val student = Player("Your Name")
    println("Hello, " + student.name)
  }
}
