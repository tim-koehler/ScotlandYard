package de.htwg.se.scotlandyard.model.core

import java.io.FileNotFoundException

import de.htwg.se.scotlandyard.ScotlandYard
import de.htwg.se.scotlandyard.model.map.Map

import scala.io.{BufferedSource, Source}

class Tui {
  val menuTitles: List[String] = List("->Main Menu<-", "->Number of Players<-")
  val mainMenuEntries: List[String] = List("Start Game", "Settings", "End Game")
  val settingsEntries: List[String] = List("2 Players", "3 Players", "4 Players")
  val TUIMODE_QUIT: Int = -1
  val TUIMODE_RUNNING: Int = 0
  val TUIMODE_MAINMENU: Int = 1
  val TUIMODE_SETTINGS: Int = 2
  var tuiMode = TUIMODE_MAINMENU

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

  def evaluateInput(inputStr: String): Int = {
    var input = inputStr.toInt
    tuiMode match {
      case TUIMODE_RUNNING => evaluateRunning(input)
      case TUIMODE_MAINMENU => evaluateMainMenu(input)
      case TUIMODE_SETTINGS => evaluateSettings(input)
    }
  }

  def evaluateRunning(input: Int): Int = {

    if(input == 7)
      MapRenderer.updateX(-5)
    else if(input == 8)
      MapRenderer.updateX(5)
    else if(input == 5)
      MapRenderer.updateY(-5)
    else if(input == 6)
      MapRenderer.updateY(5)
    99
  }

  def evaluateMainMenu(input: Int): Int = {
    if(input == 1) {
      GameMaster.startGame()
      tuiMode = TUIMODE_RUNNING
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

  def evaluateSettings(input: Int): Int = {
    if(input == 3) {
      GameMaster.numberOfPlayer = 3
    } else if(input == 4) {
      GameMaster.numberOfPlayer = 4
    }
    tuiMode = TUIMODE_MAINMENU
    TUIMODE_SETTINGS
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
        for (x <- settingsEntries) {
          outputString = outputString + index.toString + ": " + x + "\n"
          index += 1
        }
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
