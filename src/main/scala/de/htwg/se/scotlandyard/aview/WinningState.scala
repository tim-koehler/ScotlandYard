package de.htwg.se.scotlandyard.aview

class WinningState(tui: Tui) extends State(tui: Tui) {
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
    tui.buildOutputStringMrXWin()
  }

}
