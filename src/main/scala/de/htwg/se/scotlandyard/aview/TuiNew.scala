package de.htwg.se.scotlandyard.aview

import java.io.FileNotFoundException

import de.htwg.se.scotlandyard.controller.Controller
import de.htwg.se.scotlandyard.model.core.{GameMaster, MapRenderer}
import de.htwg.se.scotlandyard.model.player.TicketType
import de.htwg.se.scotlandyard.util.Observer

import scala.io.StdIn.readLine
import scala.io.{BufferedSource, Source}
//67 lines are comments in the old Tui class
class TuiNew(controller: Controller) extends Observer{
  controller.add(this)

  val menuTitles: List[String] = List("->Main Menu<-", "->Number of Players<-", "->Choose Names<-", "->START<-")
  val mainMenuEntries: List[String] = List("Start Game", "End Game")
  val settingsMenuEntries: List[String] = List("2 Players", "3 Players", "4 Players", "5 Players", "6 Players", "7 Players")
  val chooseNameMenuEntries: List[String] = List("Start", "Detective1", "Detective2", "Detective3", "Detective4", "Detective5", "Detective6")
  val titleBanner = getTitleBanner()

  var mainMenuString = titleBanner + "\n\n" + "->Main Menu<-" + "\n" + "1: Start Game\n" + "2: End Game"
  var SettingsString = titleBanner + "\n\n" + "->Number of Players<-" + "\n" + "2: 2 Players\n" +
    "3: 3 Players\n" + "4: 4 Players\n" + "5: 5 Players\n" + "6: 6 Players\n" + "7: 7 Players"

  val MODE_QUIT: Int = -1
  val MODE_RUNNING: Int = 0
  val MODE_MENU: Int = 1
  var mode = MODE_MENU

  var menuCounter = 0 // 0 is main Menu, 1 is settings, 2 is choose name, ....

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

  def evaluateInput(input: String): Int = {
    mode match {
      case MODE_RUNNING => evaluateRunning(input)
      case MODE_MENU => evaluateMenu(input)
    }
  }

  def evaluateRunning(input: String): Int = {
    if(input.matches("[0-9]{1,3} ((T|t)|(B|b)|(U|u))")) {
      evaluateNextPositionInput(input)
    } else {
      evaluateMoveMapInput(input)
    }
  }

  def evaluateMoveMapInput(input: String): Int = {
    if(input.matches("(a|A)+")) {
      MapRenderer.updateX(input.length, positive = false)
    } else if(input.matches("(d|D)+")) {
      MapRenderer.updateX(input.length , positive = true)
    } else if(input.matches("(w|W)+")) {
      MapRenderer.updateY(input.length, positive = false)
    } else if(input.matches("(s|S)+")) {
      MapRenderer.updateY(input.length, positive = true)
    } else if(input.equalsIgnoreCase("exit")) {
      return MODE_QUIT
    }
    updateScreen()
    mode
  }

  def evaluateNextPositionInput(input: String): Int = {
    val index = input.indexOf(" ")
    val newStation = input.substring(0, index).toInt
    val transport = input.substring(index + 1).toCharArray.head.toLower

    if(transport.equals('t')) {
      controller.validateAndMove(newStation, TicketType.Taxi)
    } else if (transport.equals('b')) {
      controller.validateAndMove(newStation, TicketType.Bus)
    } else if (transport.equals('u')) {
      controller.validateAndMove(newStation, TicketType.Underground)
    }

    mode
  }

  def evaluateMenu(input: String): Int = {
    menuCounter match {
      case 0 => evalMainMenu(input)
      case 1 => controller.setPlayerNumber(input.toInt)
      case 2 =>evalNameMenu(input)
    }
  }

  def evalMainMenu(input: String): Int = {
    if(input.toInt == 1) {
      menuCounter += 1
      menuCounter
    } else {
      MODE_QUIT
    }
  }

  def evalNameMenu(inputStr: String): Int = {
    var input = 0
    try {
      input = inputStr.toInt
    } catch {
      case e: NumberFormatException => -99
    }

    if(input == 1) {
      //TODO: display MrX
      updateScreen()
    } else if(!readAndSetPlayerName(input - 1)) { // -1 because 1 is Start and 2 is the first Detective
      menuCounter
    }
      mode
  }

  def readAndSetPlayerName(index: Int): Boolean = {
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

  override def toString() : String = {
    if(mode == 1) {
      buildOutputStringForMenu()
    } else {
      buildOutputStringForRunningGame()
    }
  }

  def buildOutputStringForRunningGame(): String = {
    var outputString = ""
    outputString = MapRenderer.renderMap()
    for(p <- controller.getPlayersList()) {
      outputString = outputString + p.toString + "\n"
    }
    outputString = outputString + "\n" + "Player" + " " + controller.getCurrentPlayer().name + " " + "Enter your next Station:"
    //TODO: remove debug
    outputString = outputString + "\nTotal Round: " + GameMaster.totalRound // DEBUG
    outputString
  }

  def buildOutputStringForMenu(): String = {
    mainMenuString

  }

  def updateScreen(): Unit = {
    println(toString())
  }

  override def update: Unit = {
    updateScreen()
  }
}
