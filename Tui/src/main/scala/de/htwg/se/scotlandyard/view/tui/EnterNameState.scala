package de.htwg.se.scotlandyard.view.tui

import de.htwg.se.scotlandyard.view.tui.State

class EnterNameState(tui: Tui) extends State {
  override def evaluateInput(input: String): Int = {
    if(tui.evaluateEnterName(input)) {
      tui.indexOfPlayerWhichNameToChange
    } else {
      tui.TUIMODE_RUNNING
    }
  }

  override def toString: String = {
    tui.titleBanner + "\n\n" + "->Enter a Name<-"
  }
}
