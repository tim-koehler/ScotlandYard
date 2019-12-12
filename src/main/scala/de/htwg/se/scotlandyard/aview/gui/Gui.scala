package de.htwg.se.scotlandyard.aview

import de.htwg.se.scotlandyard.controller.Controller

import scala.swing._
import de.htwg.se.scotlandyard.aview.gui.{GuiMainBuilder, GuiSettingsBuilder}

class Gui(controller: Controller) extends Frame {
  val EXIT_ON_CLOSE = 3
  title = "Scotland Yard"
  preferredSize = new Dimension(600, 400)
  centerOnScreen()
  peer.setDefaultCloseOperation(EXIT_ON_CLOSE)

  def gamePanel(): BorderPanel = {
    val mainBuiler = new GuiMainBuilder(controller, this)
    mainBuiler.getPanel()
  }

  def settingsPanel(): BorderPanel = {
    val settingsBuilder = new GuiSettingsBuilder(controller, this)
    settingsBuilder.getPanel()
  }

  def updateSettings(): Unit = {
    contents = settingsPanel
    this.repaint()
  }

  //TODO: Screen Max
  def updateGame(): Unit = {
    contents = gamePanel()
    this.repaint()
    setMax()
  }

  def changeToGamePanel(): Unit = {
    contents = gamePanel()
    setMax()
  }

  def setMax() = {
    this.maximize()
  }

  contents = settingsPanel

  visible = true
}
