package de.htwg.se.scotlandyard.view.tui

class RevealMrX2State(tui: Tui) extends State {
  override def evaluateInput(input: String): Int = {
    tui.revealMrX2()
    tui.TUIMODE_RUNNING
  }

  override def toString: String = {
    tui.getMrXStartingPositionStringAndStartGame()
  }
}
