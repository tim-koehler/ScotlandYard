package de.htwg.se.scotlandyard.aview

abstract class State(tui: Tui) {
  def evaluateInput(input: String): Int
}
