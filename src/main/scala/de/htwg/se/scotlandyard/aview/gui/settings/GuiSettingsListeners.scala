package de.htwg.se.scotlandyard.aview.gui.settings

import de.htwg.se.scotlandyard.aview.Gui
import de.htwg.se.scotlandyard.controller.ControllerInterface
import de.htwg.se.scotlandyard.model.playersComponent.Player

import javax.swing._
import scala.swing.event.{ButtonClicked, SelectionChanged}
import scala.swing.{Dialog, ListView, Reactions}
import scala.util.{Failure, Success, Try}

class GuiSettingsListeners(controller: ControllerInterface, gui: Gui) {

  def addStartButtonListener(reactions: Reactions): Unit = {
    reactions += {
      case e: ButtonClicked =>
        controller.startGame()
    }
  }

  def addListViewListener(reactions: Reactions, listView: ListView[Player]): Unit = {
    reactions += {
      case _: SelectionChanged =>
        if (listView.peer.getModel.getElementAt(0) == listView.peer.getSelectedValue)
          listView.peer.setSelectedIndex(1)
    }
  }

  def addChangeNameButtonListener(reactions: Reactions): Unit = {
    reactions += {
      case e: ButtonClicked =>
        val textField = e.source.peer.getParent.getComponents()(2).asInstanceOf[JTextField]
        val listView = e.source.peer.getParent.getParent.getParent.
          getComponent(2).asInstanceOf[JPanel].
          getComponent(0).asInstanceOf[JScrollPane].
          getComponent(0).asInstanceOf[JViewport].
          getComponent(0).asInstanceOf[JList[String]]
        controller.setPlayerName(textField.getText, listView.getSelectedIndex)
        textField.setText("")
        gui.updateSettings()
    }
  }

  def addRadioButtonsListener(reactions: Reactions): Unit = {
    reactions += {
      case e: ButtonClicked =>
        e.source.selected = true
        e.source.text match {
          case "2 Player" => controller.initialize(2)
          case "3 Player" => controller.initialize(3)
          case "4 Player" => controller.initialize(4)
          case "5 Player" => controller.initialize(5)
          case "6 Player" => controller.initialize(6)
          case "7 Player" => controller.initialize(7)
        }
        gui.updateSettings()
    }
  }

  def addLoadButtonListener(reactions: Reactions): Unit = {
    reactions += {
      case e: ButtonClicked =>
        Try(controller.load()) match {
          case Success(v) => Dialog.showMessage(gui, "Game successfully Loaded!", "Load");

            controller.startGame()
          case Failure(e) => Dialog.showMessage(gui, "An Error occured! The game was not loaded!", "Load", Dialog.Message.Error);
            e.printStackTrace() // for debug purpose
        }
    }
  }
}
