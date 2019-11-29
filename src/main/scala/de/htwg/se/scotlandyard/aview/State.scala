package de.htwg.se.scotlandyard.aview

abstract class State(tui: Tui) {
  def evaluateInput(input: String): Int

  def isInputNumberAndNotEmpty(input: String): Boolean = {
    if(!input.isEmpty) {
      if(input forall Character.isDigit) {
        return true
      }
    }
    false
  }
}
