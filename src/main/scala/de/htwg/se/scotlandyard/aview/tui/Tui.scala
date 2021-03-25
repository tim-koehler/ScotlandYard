package de.htwg.se.scotlandyard.aview.tui

import de.htwg.se.scotlandyard.aview.tui.tuiMapComponent.TuiMapInterface
import de.htwg.se.scotlandyard.controller._
import de.htwg.se.scotlandyard.model.TicketType

import scala.io.{BufferedSource, Source}
import scala.swing.Reactor
import scala.util.{Failure, Success, Try}

class Tui(controller: ControllerInterface, tuiMap: TuiMapInterface) extends Reactor {
  listenTo(controller)
  var state: State = new SelectNumberOfPlayerMenuState(this)

  val chooseNameMenuEntries: List[String] = List("Start", "Detective1", "Detective2", "Detective3", "Detective4", "Detective5", "Detective6")
  val titleBanner: String = getBanner("./resources/titleBanner.txt")
  var detectiveWinningBanner: String = getBanner("./resources/detectiveWinBanner.txt")
  var mrXWinningBanner: String = getBanner("./resources/mrXWinBanner.txt")
  val chooseNameMenuString: String = titleBanner + "\n\n" + "->Choose Names<-" + "\n"

  val TUIMODE_QUIT: Int = -1
  val TUIMODE_RUNNING: Int = 0

  var indexOfPlayerWhichNameToChange = 1

  tuiMap.updatePlayerPositions(controller)

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
    if(input.equals("exit")) {
      return TUIMODE_QUIT
    } else if(input.equals("load") && !state.isInstanceOf[RunningState]) {
      evaluateLoad()
    }
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
    }
    updateScreen()
    TUIMODE_RUNNING
  }

  def evaluateNextPositionInput(input: String): Int = {
    val index = input.indexOf(" ")
    val newStation = input.substring(0, index).toInt
    val transport = input.substring(index + 1).toCharArray.head.toLower
    if(transport.equals('t')) {
        controller.move(newStation, TicketType.of(transport))
        updateScreen()
    } else if (transport.equals('b')) {
      controller.move(newStation, TicketType.of(transport))
      updateScreen()
    } else if (transport.equals('u')) {
      controller.move(newStation, TicketType.of(transport))
      updateScreen()
    } else {
      controller.move(newStation, TicketType.of(transport))
      updateScreen()
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
    updateScreen()
    TUIMODE_RUNNING
  }

  def evaluateLoad(): Int = {
    controller.load()
    controller.startGame()
    TUIMODE_RUNNING
  }

  def evaluateSettings(input: String): Int = {
    changeState(new ChooseNameMenuState(this))
    controller.initialize(input.toInt)
  }

  def evaluateNameMenu(input: String): Int = {
    if (input.toInt == 1) {
      controller.startGame()
    } else if (input.toInt > 1 &&  input.toInt <= controller.getPlayersList().length) {
      changeState(new EnterNameState(this))
      updateScreen()
      indexOfPlayerWhichNameToChange = input.toInt - 1 // -1 because 1 is Start and 2 is the first Player
    } else {
      updateScreen()
    }
    input.toInt
  }

  def evaluateEnterName(input: String): Boolean = {
    changeState(new ChooseNameMenuState(this))
    controller.setPlayerName(input, indexOfPlayerWhichNameToChange)
  }

  def evaluateWinning(input: String): Int = {
    TUIMODE_QUIT
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
    var outputString = getTuiMap()
    outputString = outputString + "Round: " + controller.getTotalRound() + "\nMrX History: "
    for(t <- controller.getMrX.getHistory().reverse) {
      outputString = outputString + t.toString + ", "
    }
    outputString = outputString + "\n"
    for(p <- controller.getPlayersList()) {
      outputString = outputString + p.toString + "\n"
    }
    outputString = outputString + "\n" + "Player" + " " + controller.getCurrentPlayer.name.substring(0, 3) + " " + "Enter your next Station:"
    outputString
  }

  def getMrXStartingPositionStringAndStartGame(): String = {
    changeState(new RunningState(this))
    getMrXStartingPositionString()
  }

  def getMrXStartingPositionString(): String = {
    "MrX is at Station: " + controller.getCurrentPlayer.station.number + "\n\n\n\n\n"
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
        " was at Station " + controller.getWinningPlayer().station.number + " !!!\n\n" +
        "Enter any key to exit..."
    } else {
      detectiveWinningBanner + "\n\n" +
        controller.getWinningPlayer().name + " has caught " + controller.getPlayersList()(0).name +
        " at Station " + controller.getWinningPlayer().station.number + " !!!\n\n" +
        "Enter any key to exit..."
    }
  }

  override def toString() : String = {
    state.toString()
  }

  def getTuiMap(): String = {
    tuiMap.updatePlayerPositions(controller)
    tuiMap.toString()
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