package de.htwg.se.scotlandyard.aview

import java.awt.Toolkit

import de.htwg.se.scotlandyard.controller.{Controller, NumberOfPlayersChanged, PlayerMoved, PlayerNameChanged, PlayerWin}

import scala.swing._
import de.htwg.se.scotlandyard.aview.gui.{GuiMainBuilder, GuiSettingsBuilder}

class Gui(controller: Controller) extends Frame {
  listenTo(controller)

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

  def updateGame(): Unit = {
    contents = gamePanel()
    this.repaint()
    setMax()
  }

  def changeToGamePanel(): Unit = {
    val screenSize = Toolkit.getDefaultToolkit.getScreenSize
    preferredSize = new Dimension(screenSize.width, screenSize.height)
    contents = gamePanel()
    centerOnScreen()
  }

  //TODO: fix maxmimize bug
  def setMax() = {
    this.maximize()
  }

  contents = settingsPanel

  visible = true

  reactions += {
    case event: PlayerNameChanged => updateSettings()
    case event: NumberOfPlayersChanged => updateSettings()
    case event: PlayerMoved => updateGame()
    case event: PlayerWin => updateGame()
  }
}
