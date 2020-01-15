package de.htwg.se.scotlandyard.aview.tui

import de.htwg.se.scotlandyard.controllerComponent._
import de.htwg.se.scotlandyard.model.playersComponent.playersBaseImpl.MrX
import de.htwg.se.scotlandyard.model.tuiMapComponent.TuiMapInterface
import de.htwg.se.scotlandyard.model.tuiMapComponent.tuiMapBaseImpl.TuiMap
import de.htwg.se.scotlandyard.util.TicketType

import scala.io.{BufferedSource, Source}
import scala.swing.Reactor
import scala.util.{Failure, Success, Try}

class Tui(controller: ControllerInterface, tuiMap: TuiMapInterface) extends Reactor {
  listenTo(controller)
  var state: State = new SelectNumberOfPlayerMenuState(this)

  val chooseNameMenuEntries: List[String] = List("Start", "Detective1", "Detective2", "Detective3", "Detective4", "Detective5", "Detective6")
  val titleBanner = getBanner("./resources/titleBanner.txt")
  var detectiveWinningBanner = getBanner("./resources/detectiveWinBanner.txt")
  var mrXWinningBanner = getBanner("./resources/mrXWinBanner.txt")
  val chooseNameMenuString = titleBanner + "\n\n" + "->Choose Names<-" + "\n"

  val TUIMODE_QUIT: Int = -1
  val TUIMODE_RUNNING: Int = 0

  var indexOfPlayerWhichNameToChange = 1

  tuiMap.updatePlayerPositions()

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
      tuiMap.updateViewOffsetX(input.length, positive = false)
    } else if(input.matches("(d|D)+")) {
      tuiMap.updateViewOffsetX(input.length , positive = true)
    } else if(input.matches("(w|W)+")) {
      tuiMap.updateViewOffsetY(input.length, positive = false)
    } else if(input.matches("(s|S)+")) {
      tuiMap.updateViewOffsetY(input.length, positive = true)
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
        controller.doMove(newStation, TicketType.Taxi)
        if(controller.getWin()) controller.winGame()
      } else {
        updateScreen()
      }
    } else if (transport.equals('b')) {
      if(controller.validateMove(newStation, TicketType.Bus)) {
        controller.doMove(newStation, TicketType.Bus)
        if (controller.getWin()) controller.winGame()
      } else {
        updateScreen()
      }
    } else if (transport.equals('u')) {
      if (controller.validateMove(newStation, TicketType.Underground)) {
        controller.doMove(newStation, TicketType.Underground)
        if (controller.getWin()) controller.winGame()
      } else {
        updateScreen()
      }
    } else {
      if (controller.validateMove(newStation, TicketType.Black)) {
        controller.doMove(newStation, TicketType.Black)
        if (controller.getWin()) controller.winGame()
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

  //TODO: Error handling, save path
  def evaluateSave(): Int = {
    controller.save()
    TUIMODE_RUNNING
  }

  def evaluateLoad(): Int = {
    controller.load()
    TUIMODE_RUNNING
  }

  def evaluateSettings(input: String): Int = {
    changeState(new ChooseNameMenuState(this))
    controller.initPlayers(input.toInt)
  }

  def evaluateNameMenu(input: String): Int = {
    if (input.toInt == 1) {
      controller.startGame()
    } else if (input.toInt > 1) {
      changeState(new EnterNameState(this))
      updateScreen()
      indexOfPlayerWhichNameToChange = input.toInt - 1 // -1 because 1 is Start and 2 is the first Player
    }
    input.toInt
  }

  def evaluateEnterName(input: String): Boolean = {
    changeState(new ChooseNameMenuState(this))
    if(controller.setPlayerName(input, indexOfPlayerWhichNameToChange)) {
      true
    } else {
      updateScreen()
      false
    }
  }

  def evaluateWinning(input: String): Int = {
    System.exit(0)
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
    var outputString = tuiMap.toString()
    outputString = outputString + "Round: " + controller.getTotalRound() + "\nMrX History: "
    for(t <- controller.getMrX().getHistory()) {
      outputString = outputString + t.toString + ", "
    }
    outputString = outputString + "\n"
    for(p <- controller.getPlayersList()) {
      outputString = outputString + p.toString + "\n"
    }
    outputString = outputString + "\n" + "Player" + " " + controller.getCurrentPlayer().name.substring(0, 3) + " " + "Enter your next Station:"
    outputString
  }

  def getMrXStartingPositionString(): String = {
    changeState(new RunningState(this))
    "MrX is at Station: " + controller.getCurrentPlayer().getPosition().number + "\n\n\n\n\n"
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
    if (controller.getWinningPlayer().name.equals("MrX")) {
      mrXWinningBanner + "\n\n" +
        controller.getPlayersList()(0).name +
        " was at Station " + controller.getWinningPlayer().getPosition().number + " !!!\n\n" +
        "Enter any key to exit..."
    } else {
      detectiveWinningBanner + "\n\n" +
        controller.getWinningPlayer().name + " has caught " + controller.getPlayersList()(0).name +
        " at Station " + controller.getWinningPlayer().getPosition().number + " !!!\n\n" +
        "Enter any key to exit..."
    }
  }

  override def toString() : String = {
    state.toString()
  }

  def updateScreen(): Unit = {
    println(toString())
  }

  def update: Unit = {
    updateScreen()
  }

  updateScreen()

  reactions += {
    case event: PlayerNameChanged => updateScreen()
    case event: NumberOfPlayersChanged => changeState(new ChooseNameMenuState(this))
      updateScreen()
    case event: PlayerMoved => updateScreen()
    case event: PlayerWin => changeState(new WinningState(this))
      updateScreen()
    case event: StartGame => changeState(new RunningState(this))
      updateScreen()
  }
}