package de.htwg.se.scotlandyard.aview

import java.io.BufferedReader

import de.htwg.se.scotlandyard.ScotlandYard
import de.htwg.se.scotlandyard.controller.Controller
import de.htwg.se.scotlandyard.model.core.MapRenderer
import de.htwg.se.scotlandyard.model.player.TicketType
import de.htwg.se.scotlandyard.util.Observer

import scala.io.{BufferedSource, Source}
import scala.util.{Failure, Success, Try}
class Tui(controller: Controller) extends Observer{
  controller.add(this)
  var state: State = new MainMenuState(this)

  val chooseNameMenuEntries: List[String] = List("Start", "Detective1", "Detective2", "Detective3", "Detective4", "Detective5", "Detective6")
  val titleBanner = getBanner("./src/main/scala/de/htwg/se/scotlandyard/titleBanner.txt")
  var detectiveWinningBanner = getBanner("./src/main/scala/de/htwg/se/scotlandyard/detectiveWinBanner.txt")
  var mrXWinningBanner = getBanner("./src/main/scala/de/htwg/se/scotlandyard/mrXWinBanner.txt")
  val chooseNameMenuString = titleBanner + "\n\n" + "->Choose Names<-" + "\n"

  val TUIMODE_QUIT: Int = -1
  val TUIMODE_RUNNING: Int = 0

  var indexOfPlayerWhichNameToChange = 1

  def changeState(state: State): Unit = {
    this.state = state
  }

  private def getBanner(path: String): String = {
    var banner = ""
    Try(Source.fromFile(path)) match {
      case Success(v) => banner = v.asInstanceOf[BufferedSource].mkString; v.asInstanceOf[BufferedSource].close()
      case Failure(e) => banner = "<Error while reading from " + "\"" + path + "\">"
    }
    banner
  }

  def evaluateInput(input: String): Int = {
    state.evaluateInput(input)
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
    TUIMODE_RUNNING
  }

  def evaluateNextPositionInput(input: String): Int = {
    val index = input.indexOf(" ")
    val newStation = input.substring(0, index).toInt
    val transport = input.substring(index + 1).toCharArray.head.toLower
    if(transport.equals('t')) {
      if(controller.validateMove(newStation, TicketType.Taxi)) {
        if(controller.getWin()) changeState(new WinningState(this))
        controller.doMove(newStation, TicketType.Taxi)
      } else {
        updateScreen()
      }
    } else if (transport.equals('b')) {
      if(controller.validateMove(newStation, TicketType.Bus)) {
        if (controller.getWin()) changeState(new WinningState(this))
        controller.doMove(newStation, TicketType.Bus)
      } else {
        updateScreen()
      }
    } else if (transport.equals('u')) {
      if(controller.validateMove(newStation, TicketType.Underground)) {
        if (controller.getWin()) changeState(new WinningState(this))
        controller.doMove(newStation, TicketType.Underground)
      } else {
        updateScreen()
      }
    }
    TUIMODE_RUNNING
  }

  def evaluateUndo(): Int = {
    controller.undoValidateAndMove()
    TUIMODE_RUNNING
  }

  def evaluateRedo(): Int = {
    controller.redoValidateAndMove()
    TUIMODE_RUNNING
  }

  def evaluateMainMenu(input: Int, isDebugMode: Boolean = ScotlandYard.isDebugMode): Int = {
    if(input == 1) {
      if(isDebugMode) {
        changeState(new ChooseNameMenuState(this))
        controller.initPlayers(2)
      } else {
        changeState(new SelectNumberOfPlayerMenuState(this))
        updateScreen()
        TUIMODE_RUNNING
      }
    } else {
      TUIMODE_QUIT
    }
  }

  def evaluateSettings(input: String): Int = {
    controller.initPlayers(input.toInt)
  }

  def evaluateNameMenu(input: String): Int = {
    if (input.toInt == 1) {
      changeState(new RevealMrX1State(this))
      updateScreen()
    } else if (input.toInt > 1) {
      changeState(new EnterNameState(this))
      updateScreen()
      indexOfPlayerWhichNameToChange = input.toInt - 1 // -1 because 1 is Start and 2 is the first Detective
    }
    input.toInt
  }

  def evaluateEnterName(input: String): Boolean = {
    if(controller.setPlayerNames(input, indexOfPlayerWhichNameToChange)) {
      changeState(new ChooseNameMenuState(this))
      updateScreen()
      true
    } else {
      changeState(new ChooseNameMenuState(this))
      updateScreen()
      false
    }
  }

  def evaluateWinning(input: String): Int = {
    changeState(new MainMenuState(this))
    updateScreen()
    TUIMODE_RUNNING
  }

  def revealMrX1(): Int = {
    changeState(new RevealMrX2State(this))
    updateScreen()
    TUIMODE_RUNNING
  }

  def revealMrX2(): Int = {
    changeState(new RunningState(this))
    updateScreen()
    TUIMODE_RUNNING
  }

  def buildOutputStringForRunningGame(): String = {
    var outputString = MapRenderer.renderMap()
    if(ScotlandYard.isDebugMode) {
      outputString = outputString + "DEBUG MODE\n"
    }
    outputString = outputString + "Round: " + controller.getTotalRound() + "\n"
    for(p <- controller.getPlayersList()) {
      outputString = outputString + p.toString + "\n"
    }
    outputString = outputString + "\n" + "Player" + " " + controller.getCurrentPlayer().name + " " + "Enter your next Station:"
    outputString
  }

  def getMrXStartingPositionString(): String = {
    changeState(new RunningState(this))
    "MrX is at Station: " + controller.getCurrentPlayer().getPosition().number
  }

  def buildOutputStringForChooseNameMenu(): String = {
    var outputString = chooseNameMenuString
    outputString = outputString + "1" + ": " + chooseNameMenuEntries(0) + "\n"

    for((x,i) <- controller.getPlayersList().drop(1).view.zipWithIndex) {
      outputString = outputString + (i + 2).toString + ": " + chooseNameMenuEntries(i + 1) + ": " + x.name + "\n"
    }
    outputString
  }

  def buildOutputStringWin(): String = {
    if(controller.getWinningPlayer().name.equals("MrX")) {
      mrXWinningBanner + "\n\n" +
        controller.getPlayersList()(0).name +
        " was at Station " + controller.getWinningPlayer().getPosition().number + " !!!\n\n" +
        "Enter any key to go back to Main Menu..."
    } else {
      detectiveWinningBanner + "\n\n" +
        controller.getWinningPlayer().name + " has caught " + controller.getPlayersList()(0).name +
        " at Station " + controller.getWinningPlayer().getPosition().number + " !!!\n\n" +
        "Enter any key to go back to Main Menu..."
    }
  }

  override def toString() : String = {
    state.toString()
  }

  def updateScreen(): Unit = {
    println(toString())
  }

  override def update: Unit = {
    updateScreen()
  }
}