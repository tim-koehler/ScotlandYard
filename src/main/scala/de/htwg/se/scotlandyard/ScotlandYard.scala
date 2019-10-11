package de.htwg.se.scotlandyard

import de.htwg.se.scotlandyard.model.Player
import javax.swing.JLayeredPane

object ScotlandYard {
  def main(args: Array[String]): Unit = {
    val player1 = Player("Tim")
    val player2 = Player("Roli")
    println("Hello, " + player1.name + "How are you doing?")
    println("Hello, " + player2.name + "How are you doing?")
  }
}
