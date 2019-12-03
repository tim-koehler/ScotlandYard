package de.htwg.se.scotlandyard.aview

import de.htwg.se.scotlandyard.model.core.GameMaster

class RunningState(tui: Tui) extends State(tui: Tui) {
  override def evaluateInput(input: String): Int = {
    if(input.matches("[0-9]{1,3} ((T|t)|(B|b)|(U|u))")) {
      tui.evaluateNextPositionInput(input)
    } else if(input.equals("undo")) {
      tui.evaluateUndo()
    } else {
      tui.evaluateMoveMapInput(input)
    }
  }

  override def toString: String = {
    tui.buildOutputStringForRunningGame()
  }
}
