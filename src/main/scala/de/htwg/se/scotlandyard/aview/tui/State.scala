package de.htwg.se.scotlandyard.aview.tui

import scala.util.{Failure, Success, Try}

trait State {
  def evaluateInput(input: String): Int

  def isInputNumberAndNotEmpty(input: String): Boolean = {
    Try(input.toInt) match {
      case Success(v) => true
      case Failure(e) => false
    }
  }
}
