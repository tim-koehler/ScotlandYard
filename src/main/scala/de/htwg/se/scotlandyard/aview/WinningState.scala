package de.htwg.se.scotlandyard.aview

class WinningState(tui: Tui) extends State(tui: Tui) {
  override def evaluateInput(input: String): Int = {
    tui.TUIMODE_RUNNING
  }

  override def toString: String = {
    "Win\nPress any key to go back to Main Menu"
  }

}
