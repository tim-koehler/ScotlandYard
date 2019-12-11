package de.htwg.se.scotlandyard.aview.tui

class SelectNumberOfPlayerMenuState(tui: Tui) extends State(tui: Tui) {
  override def evaluateInput(input: String): Int = {
    if(isSelectNumberOfPlayerInputCorrect(input)) {
      tui.evaluateSettings(input)
    } else {
      tui.updateScreen()
      tui.TUIMODE_RUNNING
    }
  }

  def isSelectNumberOfPlayerInputCorrect(input: String): Boolean = {
    if(isInputNumberAndNotEmpty(input)) {
      if(input.toInt >= 2 || input.toInt <= 7) {
        return true
      }
    }
    false
  }

  override def toString: String = {
    tui.titleBanner + "\n\n" + "->Number of Players<-" + "\n" + "2: 2 Players\n" +
      "3: 3 Players\n" + "4: 4 Players\n" + "5: 5 Players\n" + "6: 6 Players\n" + "7: 7 Players"
  }
}
