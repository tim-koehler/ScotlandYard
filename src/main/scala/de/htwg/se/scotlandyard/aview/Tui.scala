package de.htwg.se.scotlandyard.aview

import java.io.FileNotFoundException

import de.htwg.se.scotlandyard.controller.Controller
import de.htwg.se.scotlandyard.model.core.{GameMaster, MapRenderer}
import de.htwg.se.scotlandyard.model.player.Player
import de.htwg.se.scotlandyard.util.Observer

import scala.io.StdIn.readLine
import scala.io.{BufferedSource, Source}

class Tui(controller: Controller) extends Observer{
  controller.add(this)
  val menuTitles: List[String] = List("->Main Menu<-", "->Number of Players<-", "->Choose Names<-")
  val mainMenuEntries: List[String] = List("Start Game", "Settings", "End Game")
  val settingsMenuEntries: List[String] = List("2 Players", "3 Players", "4 Players", "5 Players", "6 Players", "7 Players")
  val chooseNameMenuEntries: List[String] = List("Detective1", "Detective2", "Detective3", "Detective4", "Detective5", "Detective6", "Start")
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
      MapRenderer.updateX(input.length, positive = false)
    } else if(input.matches("(d|D)+")) {
      MapRenderer.updateX(input.length , positive = true)
    } else if(input.matches("(w|W)+")) {
      MapRenderer.updateY(input.length, positive = false)
    } else if(input.matches("(s|S)+")) {
      MapRenderer.updateY(input.length, positive = true)
    } else if(input.equalsIgnoreCase("exit")) {
      tuiMode = TUIMODE_QUIT
      return tuiMode
    }
    else {
      tuiMode = TUIMODE_RUNNING
    }
    controller.notifyObservers
    tuiMode
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
      controller.notifyObservers
      tuiMode
    } else if(input == 2) {
      tuiMode = TUIMODE_SETTINGS
      controller.notifyObservers
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

    tuiMode = TUIMODE_MAINMENU
    input match {
      case 3 => controller.setPlayerNumber(3)
      case 4 => controller.setPlayerNumber(4)
      case 5 => controller.setPlayerNumber(5)
      case 6 => controller.setPlayerNumber(6)
      case 7 => controller.setPlayerNumber(7)
      case _ => tuiMode = TUIMODE_SETTINGS
    }
    controller.notifyObservers
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
      GameMaster.startGame()
      tuiMode = TUIMODE_RUNNING
    } else {
      readAndSetPlayerName(input - 1) // -1 because 1 is Start and 2 is the first Detective
    }
    controller.notifyObservers

    tuiMode
  }

  def readAndSetPlayerName(index: Int): Unit = {
    var inputName = ""
    inputName = readLine()
    setPlayerName(inputName, index)
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
      controller.setPlayerNames(checkAndAdjustPlayerName(inputName), index)
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
    if(tuiMode >= 1) {
      buildOutputStringForMenus()
    } else {
      buildOutputStringForRunningGame()
    }
  }

  /**
   * Builds the String for all menus
   * @return menu String
   */
  def buildOutputStringForMenus(): String = {
    val outputString = titleBanner + "\n\n"
    if (tuiMode == TUIMODE_MAINMENU) {
      buildOutputStringForMainMenu(outputString)
    } else if (tuiMode == TUIMODE_SETTINGS) {
      buildOutputStringForSettingsMenu(outputString)
    } else if(tuiMode == TUIMODE_CHOOSENAME) {
      buildOutputStringForChooseNameMenu(outputString)
    } else {
      outputString
    }
  }

  /**
   * Builds the String specific for main menu
   * @param banner
   * @return String for main menu
   */
  def buildOutputStringForMainMenu(banner: String): String = {
    var outputString = banner + menuTitles(0) + "\n"
    var index = 1
    for (x <- mainMenuEntries) {
      outputString = outputString + index.toString + ": " + x + "\n"
      index += 1
    }
    outputString
  }

  /**
   * Builds the String specific for settings menu
   * @param banner
   * @return String for settings
   */
  def buildOutputStringForSettingsMenu(banner: String): String = {
    var outputString = banner + menuTitles(1) + "\n"
    var index = 2
    for (x <- settingsMenuEntries) {
      outputString = outputString + index.toString + ": " + x + "\n"
      index += 1
    }
    outputString
  }

  /**
   * Builds the String specific for choose name menu
   * @param banner
   * @return String for choose name menu
   */
  def buildOutputStringForChooseNameMenu(banner: String): String = {
    var outputString = banner + menuTitles(2) + "\n"
    outputString = outputString + "1" + ": " + chooseNameMenuEntries(6) + "\n"

    for((x,i) <- controller.getPlayersList().drop(1).view.zipWithIndex) {
      outputString = outputString + (i + 2).toString + ": " + chooseNameMenuEntries(i) + ": " + x.name + "\n"
    }

    outputString
  }

  /**
   * Builds the String for the running game including the map
   * @return String for the running game
   */
  def buildOutputStringForRunningGame(): String = {
    var outputString = ""
    outputString = MapRenderer.renderMap()
    for(p <- controller.getPlayersList()) {
      outputString = outputString + p.toString + "\n"
    }
    outputString
  }

  override def update: Unit = {
    println(toString())
  }
}
