package de.htwg.se.scotlandyard.aview.gui
import de.htwg.se.scotlandyard.aview.Gui
import de.htwg.se.scotlandyard.controller.Controller
import javax.swing.{JList, JPanel, JScrollPane, JTextField, JViewport}
import scala.swing.{Button, ButtonGroup, FlowPanel, ListView, RadioButton, TextField}
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

  def createStartButton(displayName: String): Button = {
    new Button(displayName) {
      listenTo(this)
      this.reactions += {
        case e: ButtonClicked =>
          controller.startGame()
      }
    }
  }

  def createChangeNameButton(displayName: String): Button = {
      new Button(displayName) {
        listenTo(this)
        this.reactions += {
          case e: ButtonClicked =>
            val textField = e.source.peer.getParent.getComponents()(2).asInstanceOf[JTextField]
            val flowPanel = e.source.peer.getParent.getParent.getParent.getComponent(2).asInstanceOf[JPanel]
            val listView = flowPanel.
              getComponent(0).asInstanceOf[JScrollPane].
              getComponent(0).asInstanceOf[JViewport].
              getComponent(0).asInstanceOf[JList[String]]
            controller.setPlayerName(textField.getText, listView.getSelectedIndex)
            gui.updateSettings()
        }
    }
  }
}
