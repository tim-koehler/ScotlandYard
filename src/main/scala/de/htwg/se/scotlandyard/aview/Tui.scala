package de.htwg.se.scotlandyard.aview

import java.io.FileNotFoundException

import de.htwg.se.scotlandyard.ScotlandYard
import de.htwg.se.scotlandyard.controller.Controller
import de.htwg.se.scotlandyard.model.core.MapRenderer
import de.htwg.se.scotlandyard.model.player.TicketType
import de.htwg.se.scotlandyard.util.Observer

import scala.io.StdIn.readLine
import scala.io.Source
//67 lines are comments in the old Tui class
class Tui(controller: Controller) extends Observer{
  controller.add(this)
  var state: State = new MainMenuState(this)

  val chooseNameMenuEntries: List[String] = List("Start", "Detective1", "Detective2", "Detective3", "Detective4", "Detective5", "Detective6")
  val titleBanner = getTitleBanner()
  val chooseNameMenuString = titleBanner + "\n\n" + "->Choose Names<-" + "\n"

  val TUIMODE_QUIT: Int = -1
  val TUIMODE_RUNNING: Int = 0

  var indexOfPlayerWhichNameToChange = 1

  def changeState(state: State): Unit = {
    this.state = state
  }

  private def getTitleBanner(): String = {
    val bufferedSource = Source.fromFile("./src/main/scala/de/htwg/se/scotlandyard/titleBanner.txt")
    val titleBanner = bufferedSource.mkString
    bufferedSource.close
    titleBanner
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
      controller.validateAndMove(newStation, TicketType.Taxi)
    } else if (transport.equals('b')) {
      controller.validateAndMove(newStation, TicketType.Bus)
    } else if (transport.equals('u')) {
      controller.validateAndMove(newStation, TicketType.Underground)
    }
    TUIMODE_RUNNING
  }

  //TODO: This Method needs to be refactored
  def evaluateMainMenu(input: String): Int = {
    if(input.isEmpty) {updateScreen(); return TUIMODE_RUNNING}
    if(stringIsNumber(input)) {
      if(input.toInt == 1) {
        if(ScotlandYard.isDebugMode) {
          changeState(new ChooseNameMenuState(this))
          controller.initPlayers(2)
        } else {
          changeState(new SelectNumberOfPlayerMenuState(this))
          updateScreen()
          return TUIMODE_RUNNING
        }
      } else if(input.toInt == 2) {
          return TUIMODE_QUIT
      } else {
        updateScreen()
         return TUIMODE_RUNNING
      }
    } else {
      updateScreen()
      return TUIMODE_RUNNING
    }
  }

  def stringIsNumber(input: String): Boolean = {
    if(input forall Character.isDigit) true else false
  }

  def evaluateSettings(input: String): Int = {
    if(input.isEmpty) {updateScreen(); return TUIMODE_RUNNING}
    controller.initPlayers(input.toInt)
  }

  def evaluateNameMenu(input: String): String = {
    if(!stringIsNumber(input)) return input
    if (input.toInt == 1) {
      changeState(new RevealMrX1State(this))
      updateScreen()
    } else if (input.toInt > 1) {
      changeState(new EnterNameState(this))
      updateScreen()
      indexOfPlayerWhichNameToChange = input.toInt - 1 // -1 because 1 is Start and 2 is the first Detective
    }
    input
  }

  def evaluateEnterName(input: String): Boolean = {
    changeState(new ChooseNameMenuState(this))
    controller.setPlayerNames(input, indexOfPlayerWhichNameToChange)
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