package de.htwg.se.scotlandyard.model.core

import de.htwg.se.scotlandyard.ScotlandYard
import de.htwg.se.scotlandyard.model.map.Map

import scala.io.Source

class Tui {
  val menuTitles: List[String] = List("->Main Menu<-", "->Number of Players<-", "->Choose Names<-")
  val mainMenuEntries: List[String] = List("Start Game", "Settings", "End Game")
  val settingsMenuEntries: List[String] = List("2 Players", "3 Players", "4 Players")
  val chooseNameMenuEntries: List[String] = List("Detective1", "Detective2", "Detective3", "Start")
  val TUIMODE_QUIT: Int = -1
  val TUIMODE_RUNNING: Int = 0
  val TUIMODE_MAINMENU: Int = 1
  val TUIMODE_SETTINGS: Int = 2
  val TUIMODE_CHOOSENAME: Int = 3
  var tuiMode = TUIMODE_MAINMENU

  private def getTitleBanner(): String = {
    val bufferedSource = Source.fromFile("src\\main\\scala\\de\\htwg\\se\\scotlandyard\\titleBanner.txt")
    val titleBanner = bufferedSource.mkString
    bufferedSource.close
    titleBanner
  }

  def evaluateInput(input: String): Int = {
    tuiMode match {
      case TUIMODE_RUNNING => evaluateRunning(input)
      case TUIMODE_MAINMENU => evaluateMainMenu(input)
      case TUIMODE_SETTINGS => evaluateSettings(input)
      case TUIMODE_CHOOSENAME => evaluateChooseName(input)
    }
  }

  def evaluateRunning(input: String): Int = {
    99
  }

  def evaluateMainMenu(inputStr: String): Int = {
    var input = 0
    try {
      input = inputStr.toInt
    } catch {
      case e: NumberFormatException => -99
    }
    if(input == 1) {
      tuiMode = TUIMODE_CHOOSENAME
      //GameMaster.startGame()
      //tuiMode = TUIMODE_RUNNING
      // wird hier aufgerufen, weil die players List schon gefüllt sein muss, um die Namen einzutragen
      GameInitializer.initPlayers() //!!!!!!!!!!!!!
      tuiMode
    } else if(input == 2) {
      tuiMode = TUIMODE_SETTINGS
      tuiMode
    } else if(input == 3) {
      TUIMODE_QUIT
    } else {
      -99
    }
  }

  def evaluateSettings(inputStr: String): Int = {
    var input = 0
    try {
      input = inputStr.toInt
    } catch {
      case e: NumberFormatException => -99
    }
    if(input == 3) {
      GameMaster.numberOfPlayers = 3
    } else if(input == 4) {
      GameMaster.numberOfPlayers = 4
    }
    tuiMode = TUIMODE_MAINMENU
    TUIMODE_SETTINGS
  }

  def evaluateChooseName(inputStr: String): Int = {
    9
  }

  override def toString() : String = {
    var outputString = ""

    if(tuiMode >= 1) {
      val titleBanner = getTitleBanner()
      outputString = outputString + titleBanner + "\n\n"
      if (tuiMode == TUIMODE_MAINMENU) {
        outputString = outputString + menuTitles(0) + "\n"
        var index = 1
        for (x <- mainMenuEntries) {
          outputString = outputString + index.toString + ": " + x + "\n"
          index += 1
        }
      } else if (tuiMode == TUIMODE_SETTINGS) {
        outputString = outputString + menuTitles(1) + "\n"
        var index = 2
        for (x <- settingsMenuEntries) {
          outputString = outputString + index.toString + ": " + x + "\n"
          index += 1
        }
      } else if(tuiMode == TUIMODE_CHOOSENAME) {
        var index = 1
        outputString = outputString + menuTitles(2) + "\n"
        for (x <- 0 until (GameMaster.numberOfPlayers - 1)) {
          outputString = outputString + index.toString + ": " + chooseNameMenuEntries(x) + ": " + GameMaster.players(index).name.toString + "\n"
          index += 1
        }
        outputString = outputString + index.toString + ": " + chooseNameMenuEntries(3) + "\n"
      }
    } else if(tuiMode == TUIMODE_RUNNING) {
      outputString = Map.toString
      for(p <- GameMaster.players) {
        outputString = outputString + p.toString + "\n"
      }
    }
    outputString
  }
}
