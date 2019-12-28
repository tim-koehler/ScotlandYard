package de.htwg.se.scotlandyard.aview.tui

class RunningState(tui: Tui) extends State(tui: Tui) {
  override def evaluateInput(input: String): Int = {
    if(input.matches("[0-9]{1,3} ((T|t)|(B|b)|(U|u)|(X|x))")) {
      tui.evaluateNextPositionInput(input)
    } else if(input.equals("undo")) {
      tui.evaluateUndo()
    } else if(input.equals("redo")) {
      tui.evaluateRedo()
    } else {
      tui.evaluateMoveMapInput(input)
    }
  }

  override def toString: String = {
    tui.buildOutputStringForRunningGame()
  }
}
