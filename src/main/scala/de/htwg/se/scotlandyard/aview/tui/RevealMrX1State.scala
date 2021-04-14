package de.htwg.se.scotlandyard.aview.tui

class RevealMrX1State(tui: Tui) extends State {
  override def evaluateInput(input: String): Int = {
    tui.revealMrX1()
  }

  override def toString: String = {
    tui.titleBanner + "\n\n" + "->Start<-" + "\n" + "Reveal MrX starting Position: (Press Enter to continue...)"
  }
}
