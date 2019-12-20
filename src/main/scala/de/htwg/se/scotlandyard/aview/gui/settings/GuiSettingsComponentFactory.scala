package de.htwg.se.scotlandyard.aview.gui.settings

import de.htwg.se.scotlandyard.aview.Gui
import de.htwg.se.scotlandyard.controllerComponent.controllerBaseImpl.Controller
import de.htwg.se.scotlandyard.model.player.Player

import scala.swing.ListView.Renderer
import scala.swing.{Button, ButtonGroup, Dimension, ListView, RadioButton}

class GuiSettingsComponentFactory(controller: Controller, gui: Gui) {

  val settingsListeners = new GuiSettingsListeners(controller, gui)

  def createRadioButtons(displayName: String, buttonGroup: ButtonGroup): RadioButton = {
    new RadioButton(displayName) {
      if (text.equals("3 Player"))
        this.selected = true
      buttonGroup.buttons.add(this)
      listenTo(this)
      settingsListeners.addRadioButtonsListener(reactions)
    }
  }

  def createStartButton(displayName: String): Button = {
    new Button(displayName) {
      listenTo(this)
      settingsListeners.addStartButtonListener(reactions)
    }
  }

  def createChangeNameButton(displayName: String): Button = {
    new Button(displayName) {
      listenTo(this)
      settingsListeners.addChangeNameButtonListener(reactions)
    }
  }

  def createListView(): ListView[Player] = {
    new ListView(controller.getPlayersList()) {
      this.peer.setSelectedIndex(1)
      preferredSize = new Dimension(150, 80)
      renderer = Renderer(_.name)
      listenTo(this.selection)
      settingsListeners.addListViewListener(reactions, this)
    }
  }
}
