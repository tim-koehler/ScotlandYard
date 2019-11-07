package de.htwg.se.scotlandyard.model.core

import de.htwg.se.scotlandyard.model.map.Map

import scala.io.StdIn.readLine
import scala.io.{BufferedSource, Source}

import java.io.FileNotFoundException

class Tui {
  val menuTitles: List[String] = List("->Main Menu<-", "->Number of Players<-", "->Choose Names<-")
  val mainMenuEntries: List[String] = List("Start Game", "Settings", "End Game")
  val settingsMenuEntries: List[String] = List("2 Players", "3 Players", "4 Players")
  val chooseNameMenuEntries: List[String] = List("Detective1", "Detective2", "Detective3", "Start")
  val titleBanner = getTitleBanner()
  // Depending on which Mode the tui is set to, different evaluation
  // Methods will be called
  val TUIMODE_QUIT: Int = -1
  val TUIMODE_RUNNING: Int = 0
  val TUIMODE_MAINMENU: Int = 1
  val TUIMODE_SETTINGS: Int = 2
  val TUIMODE_CHOOSENAME: Int = 3
  val INVALID_INPUT = -99
  var tuiMode = TUIMODE_MAINMENU

  /**
   * Reads the fancy "Scotland Yard" Banner from a file
   * @return titleBanner
   */
  private def getTitleBanner(): String = {
    var bufferedSource: BufferedSource = null
    try {
      bufferedSource = Source.fromFile("src\\main\\scala\\de\\htwg\\se\\scotlandyard\\titleBanner.txt")
    } catch {
      case e: FileNotFoundException => bufferedSource = Source.fromFile("./src/main/scala/de/htwg/se/scotlandyard/titleBanner.txt")
      case _: Throwable =>
    }

    val titleBanner = bufferedSource.mkString
    bufferedSource.close
    titleBanner
  }

  /**
   * Evaluates the input depending on the current tuiMode
   * @param input input String read from console
   * @return current tuiMode
   */
  def evaluateInput(input: String): Int = {
    tuiMode match {
      case TUIMODE_RUNNING => evaluateRunning(input)
      case TUIMODE_MAINMENU => evaluateMainMenu(input)
      case TUIMODE_SETTINGS => evaluateSettings(input)
      case TUIMODE_CHOOSENAME => evaluateChooseName(input)
    }
  }

  /**
   * Evaluates the input while the game is running
   * @param input String from the console
   * @return tuiMode or a number not -1
   */
  def evaluateRunning(input: String): Int = {
    if(input.matches("(a|A)+")) {
      MapRenderer.updateX(input.length * -5)
    } else if(input.matches("(d|D)+")) {
      MapRenderer.updateX(input.length * 5)
    } else if(input.matches("(w|W)+")) {
      MapRenderer.updateY(input.length * -5)
    } else if(input.matches("(s|S)+")) {
      MapRenderer.updateY(input.length * 5)
    }
    99
  }

  /**
   * Evaluates the input while the game is in the main menu
   * @param inputStr String read from console
   * @return tuiMode
   */
  def evaluateMainMenu(inputStr: String): Int = {
    var input = 0
    try {
      input = inputStr.toInt
    } catch {
      case e: NumberFormatException => INVALID_INPUT
    }
    if(input == 1) {
      tuiMode = TUIMODE_CHOOSENAME
      tuiMode
    } else if(input == 2) {
      tuiMode = TUIMODE_SETTINGS
      tuiMode
    } else if(input == 3) {
      TUIMODE_QUIT
    } else {
      INVALID_INPUT
    }
  }

  /**
   * Evaluates the input while the game is in the settings menu
   * @param inputStr String read from console
   * @return tuiMode
   */
  def evaluateSettings(inputStr: String): Int = {
    var input = 0
    try {
      input = inputStr.toInt
    } catch {
      case e: NumberFormatException => INVALID_INPUT
    }
    if(input == 3) {
      GameMaster.numberOfPlayers = 3
    } else if(input == 4) {
      GameMaster.numberOfPlayers = 4
    }
    tuiMode = TUIMODE_MAINMENU
    tuiMode
  }

  /**
   * Evaluates the input while the game is in the choose name menu
   * @param inputStr String read from console
   * @return tuiMode
   */
  def evaluateChooseName(inputStr: String): Int = {
    var input = 0
    var inputName = ""
    try {
      input = inputStr.toInt
    } catch {
      case e: NumberFormatException => INVALID_INPUT
    }
    if(input == 1) {
      inputName = readLine()
      setPlayerName(inputName, 1)
    } else if((input == 2) && (GameMaster.numberOfPlayers == 3 || GameMaster.numberOfPlayers == 4)) {
      inputName = readLine()
      setPlayerName(inputName, 2)
    } else if((input == 3) && GameMaster.numberOfPlayers == 4) {
      inputName = readLine()
      setPlayerName(inputName, 3)
    } else if(input == GameMaster.numberOfPlayers) {
      GameMaster.startGame()
      tuiMode = TUIMODE_RUNNING
    }
    tuiMode
  }

  /**
   * Sets the name in the players list in GameMaster
   * @param inputName is the raw input
   * @param index is the index of the current Player
   * @return true if the default name was changed
   */
  def setPlayerName(inputName: String, index: Int): Boolean = {
    if(checkAndAdjustPlayerName(inputName).equals("")) {
      false
    } else {
      val playerNamesBuffer = GameMaster.playerNames.toBuffer
      playerNamesBuffer(index) = checkAndAdjustPlayerName(inputName)
      GameMaster.playerNames = playerNamesBuffer.toList
      true
    }
  }

  /**
   * Adjust the names length to 0 < name.length < 4
   * @param inputName raw input
   * @return the name with fixed length or empty String
   */
  def checkAndAdjustPlayerName(inputName: String): String = {
    if(inputName.length < 3) {
      ""
    } else {
      inputName.substring(0, 3)
    }
  }

  /**
   * Builds the tui String depending on the current tui mode
   * @return the whole tui String
   */
  override def toString() : String = {
    var outputString = ""

    if(tuiMode >= 1) {
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
          outputString = outputString + index.toString + ": " + chooseNameMenuEntries(x) + ": " + GameMaster.playerNames(index).toString + "\n"
          index += 1
        }
        outputString = outputString + index.toString + ": " + chooseNameMenuEntries(3) + "\n"
      }
    } else if(tuiMode == TUIMODE_RUNNING) {
      outputString = MapRenderer.renderMap()
      for(p <- GameMaster.players) {
        outputString = outputString + p.toString + "\n"
      }
    }
    outputString
  }
}
