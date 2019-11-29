package de.htwg.se.scotlandyard.aview

class EnterNameState(tui: Tui) extends State(tui: Tui) {
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
