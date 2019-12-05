package de.htwg.se.scotlandyard.aview

import scala.util.{Failure, Success, Try}

abstract class State(tui: Tui) {
  def evaluateInput(input: String): Int

  def isInputNumberAndNotEmpty(input: String): Boolean = {
    Try(input.toInt) match {
      case Success(v) => true
      case Failure(e) => false
    }
  }
}
