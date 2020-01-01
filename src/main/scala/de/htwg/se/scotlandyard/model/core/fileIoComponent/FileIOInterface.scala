package de.htwg.se.scotlandyard.model.core.fileIoComponent

trait FileIOInterface {
  def load(): Unit
  def save(): Unit
}
