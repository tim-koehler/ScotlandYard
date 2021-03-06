package de.htwg.se.scotlandyard.aview.tui

class WinningState(tui: Tui) extends State {
  override def evaluateInput(input: String): Int = {
    if(input.equals("undo")) {
      tui.evaluateUndo()
    } else if(input.equals("redo")) {
      tui.evaluateRedo()
    } else {
      tui.evaluateWinning(input)
    }
  }

  override def toString: String = {
    tui.buildOutputStringWin()
  }
}
