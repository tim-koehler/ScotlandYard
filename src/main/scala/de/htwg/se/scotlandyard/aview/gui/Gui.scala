package de.htwg.se.scotlandyard.aview

import java.awt.Toolkit

import de.htwg.se.scotlandyard.controller.{Controller, NumberOfPlayersChanged, PlayerMoved, PlayerNameChanged, PlayerWin, StartGame}

import scala.swing._
import de.htwg.se.scotlandyard.aview.gui.{GuiMainBuilder, GuiSettingsBuilder}
import javax.swing.ImageIcon

class Gui(controller: Controller) extends Frame {
  listenTo(controller)

  val EXIT_ON_CLOSE = 3
  title = "Scotland Yard"
  preferredSize = new Dimension(600, 400)
  centerOnScreen()
  peer.setDefaultCloseOperation(EXIT_ON_CLOSE)

  iconImage = new ImageIcon("./src/main/scala/de/htwg/se/scotlandyard/Icon.png").getImage



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
  }

  def changeToGamePanel(): Unit = {
    val screenSize = Toolkit.getDefaultToolkit.getScreenSize
    preferredSize = new Dimension(screenSize.width, screenSize.height - 50)
    contents = gamePanel()
    centerOnScreen()
    Dialog.showMessage(null, "Be Ready, MrX Position will now be revealed!", "MrX Starting Position")
    Dialog.showMessage(null, "MrX is at Station: " + controller.getCurrentPlayer().station.number, "MrX Position")
  }

  def showWinningDialog(): Unit = {
    if (controller.getWinningPlayer().name.equals("MrX")) {
      var winningMessage = controller.getPlayersList()(0).name + " was at Station " + controller.getWinningPlayer().getPosition().number + " !!!"
      Dialog.showMessage(null, winningMessage, "WIN")

    } else {
      var winningMessage = controller.getWinningPlayer().name + " has caught " + controller.getPlayersList()(0).name + " at Station " + controller.getWinningPlayer().getPosition().number + " !!!"
      Dialog.showMessage(null, winningMessage, "WIN")
    }
    this.dispose()
  }

  contents = settingsPanel

  visible = true

  reactions += {
    case event: PlayerNameChanged => updateSettings()
    case event: NumberOfPlayersChanged => updateSettings()
    case event: PlayerMoved => updateGame()
    case event: StartGame => changeToGamePanel()
    case event: PlayerWin => showWinningDialog()
  }
}
