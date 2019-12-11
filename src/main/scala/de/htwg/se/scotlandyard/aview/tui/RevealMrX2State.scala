package de.htwg.se.scotlandyard.aview.tui

class RevealMrX2State(tui: Tui) extends State (tui: Tui){
  override def evaluateInput(input: String): Int = {
    tui.revealMrX2()
    tui.TUIMODE_RUNNING
  }

  override def toString: String = {
    tui.getMrXStartingPositionString()
  }
}
