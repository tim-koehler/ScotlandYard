package de.htwg.se.scotlandyard.aview

import java.awt.Toolkit

import de.htwg.se.scotlandyard.aview.gui.main.GuiMainBuilder
import de.htwg.se.scotlandyard.aview.gui.settings.GuiSettingsBuilder
import de.htwg.se.scotlandyard.controller._
import javax.swing.ImageIcon

import scala.swing._

class Gui(controller: ControllerInterface) extends Frame {
  listenTo(controller)

  val EXIT_ON_CLOSE = 3
  title = "Scotland Yard"
  preferredSize = new Dimension(600, 400)
  centerOnScreen()
  peer.setDefaultCloseOperation(EXIT_ON_CLOSE)

  iconImage = new ImageIcon("./resources/Icon.png").getImage

  var mainBuiler = new GuiMainBuilder(controller, this)
  var settingsBuilder = new GuiSettingsBuilder(controller, this)

  def initGamePanel(): BorderPanel = {
    mainBuiler.initPanel()
  }

  def initSettingsPanel(): BorderPanel = {
    val panel = settingsBuilder.initPanel()
    peer.setDefaultCloseOperation(EXIT_ON_CLOSE)
    panel
  }

  def updateSettings(): Unit = {
    contents = settingsBuilder.updatePanel()
  }

  def updateGame(): Unit = {
    contents = mainBuiler.updatePanel()
  }

  def changeToGamePanel(): Unit = {
    val screenSize = Toolkit.getDefaultToolkit.getScreenSize
    preferredSize = new Dimension(screenSize.width - 10, screenSize.height - 60)
    contents = initGamePanel()
    centerOnScreen()
    Dialog.showMessage(this, "Be Ready, MrX Position will now be revealed!", "MrX Starting Position")
    Dialog.showMessage(this, "MrX is at Station: " + controller.getMrX.station.number, "MrX Position")
  }

  def showWinningDialog(): Unit = {
    var winningMessage = ""
    if (controller.getWinningPlayer().name.equals("MrX")) {
      winningMessage = "MrX was at Station " + controller.getWinningPlayer().station.number + " !!!"
    } else {
      winningMessage = controller.getWinningPlayer().name + " has caught MrX at Station " + controller.getWinningPlayer().station.number + " !!!"
    }
    Dialog.showMessage(this, winningMessage, "WIN")
    this.dispose()
  }

  contents = initSettingsPanel()
  visible = true

  reactions += {
    case event: PlayerNameChanged => updateSettings()
    case event: NumberOfPlayersChanged => updateSettings()
    case event: PlayerMoved => updateGame()
    case event: StartGame => changeToGamePanel()
    case event: PlayerWin => showWinningDialog()
  }
}
