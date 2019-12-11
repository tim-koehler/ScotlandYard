package de.htwg.se.scotlandyard.aview.tui

class MainMenuState(tui: Tui) extends State(tui: Tui) {
  override def evaluateInput(input: String): Int = {
    if(isMainMenuInputIsCorrect(input)) {
      tui.evaluateMainMenu(input.toInt)
    } else {
      tui.updateScreen()
      tui.TUIMODE_RUNNING
    }
  }

  def isMainMenuInputIsCorrect(input: String): Boolean = {
    if(isInputNumberAndNotEmpty(input)) {
      if(input.toInt == 1 || input.toInt == 2) {
        return true
      }
    }
    false
  }

  override def toString: String = {
    tui.titleBanner + "\n\n" + "->Main Menu<-" + "\n" + "1: Start Game\n" + "2: End Game"
  }
}