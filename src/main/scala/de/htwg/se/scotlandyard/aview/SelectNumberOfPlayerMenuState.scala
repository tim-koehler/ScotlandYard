package de.htwg.se.scotlandyard.aview

class SelectNumberOfPlayerMenuState(tui: Tui) extends State(tui: Tui) {
  override def evaluateInput(input: String): Int = {
    tui.evaluateSettings(input)
  }

  override def toString: String = {
    tui.titleBanner + "\n\n" + "->Number of Players<-" + "\n" + "2: 2 Players\n" +
      "3: 3 Players\n" + "4: 4 Players\n" + "5: 5 Players\n" + "6: 6 Players\n" + "7: 7 Players"
  }
}
