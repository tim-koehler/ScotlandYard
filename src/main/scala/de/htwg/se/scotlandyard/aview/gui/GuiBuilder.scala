package de.htwg.se.scotlandyard.aview.gui

import scala.collection.mutable
import scala.swing.{BorderPanel, Component}

trait GuiBuilder {
  var components = mutable.Queue[Component]()
  def initPanel(): BorderPanel
  def updatePanel(): BorderPanel
}
