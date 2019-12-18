package de.htwg.se.scotlandyard.aview.tui

class ChooseNameMenuState(tui: Tui) extends State(tui: Tui) {
  override def evaluateInput(input: String): Int = {
    if(isChooseNameInputCorrect(input)) {
      tui.evaluateNameMenu(input)
    } else {
      tui.updateScreen()
      tui.TUIMODE_RUNNING
    }
  }

  def isChooseNameInputCorrect(input: String): Boolean = {
    if(isInputNumberAndNotEmpty(input)) {
      if(input.toInt >= 1 && input.toInt <= 7) {
        return true
      }
    }
    false
  }

  override def toString: String = {
    tui.buildOutputStringForChooseNameMenu()
  }

}
