package de.htwg.se.scotlandyard.aview.gui

import de.htwg.se.scotlandyard.aview.Gui
import de.htwg.se.scotlandyard.controller.Controller

import scala.swing.{Button, ButtonGroup, RadioButton, TextField}
import scala.swing.event.ButtonClicked

class SettingsComponentFactory(controller: Controller, gui: Gui) {

  def createRadioButton(displayName: String, buttonGroup: ButtonGroup): RadioButton = {
    new RadioButton(displayName) {
      if(text.equals("3 Player"))
        this.selected = true
      buttonGroup.buttons.add(this)
      listenTo(this)
      this.reactions += {
        case e: ButtonClicked =>
          e.source.selected = true
          e.source.text match {
            case "2 Player" => controller.initPlayers(2)
            case "3 Player" => controller.initPlayers(3)
            case "4 Player" => controller.initPlayers(4)
            case "5 Player" => controller.initPlayers(5)
            case "6 Player" => controller.initPlayers(6)
            case "7 Player" => controller.initPlayers(7)
          }
          gui.updateSettings()
      }
    }
  }

  def createButton(displayName: String, newNameTextField: TextField, selectedListIndex: Int): Button = {
    new Button(displayName) {
      listenTo(this)
      this.reactions += {
        case e: ButtonClicked =>
          e.source.text match {
            case "Change Name" =>
              controller.setPlayerName(newNameTextField.text, selectedListIndex)
              gui.updateSettings()
            case "Start" => controller.startGame()
          }
      }
    }
  }
}
