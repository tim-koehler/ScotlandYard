package de.htwg.se.scotlandyard.aview.gui.settings

import de.htwg.se.scotlandyard.aview.Gui
import de.htwg.se.scotlandyard.controllerComponent.ControllerInterface
import de.htwg.se.scotlandyard.model.playersComponent.DetectiveInterface
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

  def addListViewListener(reactions: Reactions, listView: ListView[DetectiveInterface]): Unit = {
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

  def addLoadButtonListener(reactions: Reactions): Unit = {
    reactions += {
      case e: ButtonClicked =>
        Try(controller.load()) match {
          case Success(v) => Dialog.showMessage(null, "Game successfully Loaded!", "Load");

            controller.startGame()
          case Failure(e) => Dialog.showMessage(null, "An Error occured! The game was not loaded!", "Load", Dialog.Message.Error);
            e.printStackTrace() // for debug purpose
        }
    }
  }
}
