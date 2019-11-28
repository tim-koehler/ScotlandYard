package de.htwg.se.scotlandyard.aview

class ChooseNameMenuState(tui: Tui) extends State(tui: Tui) {
  override def evaluateInput(input: String): Int = {
    tui.evaluateNameMenu(input)
    tui.TUIMODE_RUNNING
  }

  override def toString: String = {
    tui.buildOutputStringForChooseNameMenu()
  }

}
