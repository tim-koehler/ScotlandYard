package de.htwg.se.scotlandyard.aview

class MainMenuState(tui: Tui) extends State(tui: Tui) {
  override def evaluateInput(input: String): Int = {
    tui.evaluateMainMenu(input)
  }

  override def toString: String = {
    tui.titleBanner + "\n\n" + "->Main Menu<-" + "\n" + "1: Start Game\n" + "2: End Game"
  }
}