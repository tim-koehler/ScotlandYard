package de.htwg.se.scotlandyard.aview

import java.io.FileNotFoundException

import de.htwg.se.scotlandyard.controller.Controller
import de.htwg.se.scotlandyard.model.core.MapRenderer
import de.htwg.se.scotlandyard.model.player.TicketType
import de.htwg.se.scotlandyard.util.Observer

import scala.io.StdIn.readLine
import scala.io.{BufferedSource, Source}
//67 lines are comments in the old Tui class
class Tui(controller: Controller) extends Observer{
  controller.add(this)

  val chooseNameMenuEntries: List[String] = List("Start", "Detective1", "Detective2", "Detective3", "Detective4", "Detective5", "Detective6")
  val titleBanner = getTitleBanner()

  val mainMenuString = titleBanner + "\n\n" + "->Main Menu<-" + "\n" + "1: Start Game\n" + "2: End Game"
  val settingsString = titleBanner + "\n\n" + "->Number of Players<-" + "\n" + "2: 2 Players\n" +
    "3: 3 Players\n" + "4: 4 Players\n" + "5: 5 Players\n" + "6: 6 Players\n" + "7: 7 Players"
  val chooseNameMenuString = titleBanner + "\n\n" + "->Choose Names<-" + "\n"
  val dispMrXstartPositionString = titleBanner + "\n\n" + "->Start<-" + "\n" + "Reveal MrX starting Position: (Press Enter to continue...)"

  val TUIMODE_QUIT: Int = -1
  val TUIMODE_RUNNING: Int = 0
  val TUIMODE_MENU: Int = 1
  var tuiMode = TUIMODE_MENU

  var menuCounter = 0 // 0 is main Menu, 1 is settings, 2 is choose name, 3 is Reaveal MrX start postition, 4 is MrX start position

  private def getTitleBanner(): String = {
    val bufferedSource = Source.fromFile("./src/main/scala/de/htwg/se/scotlandyard/titleBanner.txt")
    val titleBanner = bufferedSource.mkString
    bufferedSource.close
    titleBanner
  }

  def evaluateInput(input: String): Int = {
    tuiMode match {
      case TUIMODE_RUNNING => evaluateRunning(input)
      case TUIMODE_MENU => evaluateMenu(input)
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
      return TUIMODE_QUIT
    }
    updateScreen()
    tuiMode
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
    tuiMode
  }

  def evaluateMenu(inputStr: String): Int = {
    if(inputStr forall Character.isDigit) {
      menuCounter match {
        case 0 => evaluateMainMenu(inputStr.toInt)
        case 1 => evaluateSettings(inputStr.toInt)
        case 2 => evaluateNameMenu(inputStr.toInt)
        case 3 => dispMrXstartingPosition(inputStr)
      }
    }
    tuiMode
  }

  def evaluateMainMenu(input: Int): Int = {
    if(input == 1) {
      menuCounter += 1
      updateScreen()
      menuCounter
    } else if (input == 2) {
      TUIMODE_QUIT
    } else {
      TUIMODE_MENU
    }
  }

  def evaluateSettings(input: Int): Int = {
    menuCounter += 1
    controller.initPlayers(input)
    menuCounter
  }

  def evaluateNameMenu(input: Int): Int = {
    if(input == 1) {
      menuCounter += 1
      updateScreen()
    } else if(input > 1) { // -1 because 1 is Start and 2 is the first Detective
      val inputName = readLine()
      controller.setPlayerNames(inputName, input - 1)
    }
      menuCounter
  }

  def dispMrXstartingPosition(input: String): Int = {
    menuCounter += 1
    updateScreen()
    tuiMode = TUIMODE_RUNNING
    menuCounter
  }

  def buildOutputStringForRunningGame(): String = {
    var outputString = MapRenderer.renderMap()
    outputString = outputString + "Round: " + controller.getTotalRound() + "\n"
    for(p <- controller.getPlayersList()) {
      outputString = outputString + p.toString + "\n"
    }
    outputString = outputString + "\n" + "Player" + " " + controller.getCurrentPlayer().name + " " + "Enter your next Station:"
    outputString
  }

  def buildOutputStringForMenus(): String = {
    if(menuCounter == 0) {
      mainMenuString
    } else if(menuCounter == 1) {
      settingsString
    } else if(menuCounter == 2) {
      buildOutputStringForChooseNameMenu()
    } else if(menuCounter == 3) {
      dispMrXstartPositionString
    } else {
      "MrX is at Station: " + controller.getCurrentPlayer().getPosition().number
    }
  }

  def buildOutputStringForChooseNameMenu(): String = {
    var outputString = chooseNameMenuString
    outputString = outputString + "1" + ": " + chooseNameMenuEntries(0) + "\n"

    for((x,i) <- controller.getPlayersList().drop(1).view.zipWithIndex) {
      outputString = outputString + (i + 2).toString + ": " + chooseNameMenuEntries(i + 1) + ": " + x.name + "\n"
    }
    outputString
  }

  override def toString() : String = {
    if(tuiMode == 1) {
      buildOutputStringForMenus()
    } else {
      buildOutputStringForRunningGame()
    }
  }

  def updateScreen(): Unit = {
    println(toString())
  }

  override def update: Unit = {
    updateScreen()
  }
}