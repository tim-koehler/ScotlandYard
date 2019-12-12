package de.htwg.se.scotlandyard.aview.gui

import de.htwg.se.scotlandyard.aview.Gui
import de.htwg.se.scotlandyard.controller.Controller

import scala.swing.Swing._
import scala.swing.ListView.Renderer
import scala.swing.Swing.{CompoundBorder, EmptyBorder, EtchedBorder, TitledBorder}
import scala.swing.{Action, BorderPanel, BoxPanel, Button, ButtonGroup, Dialog, Dimension, FlowPanel, Label, ListView, Orientation, RadioButton, ScrollPane, TextField}
import scala.swing.event.{ButtonClicked, SelectionChanged}

class GuiSettingsBuilder(controller: Controller, gui: Gui) {
  var selelctedListIndex = 1

  def buildPanelPlayerList(): FlowPanel = {
    new FlowPanel(new ScrollPane(new ListView(controller.getPlayersList().drop(1)) {
      preferredSize = new Dimension(150, 80)
      renderer = Renderer(_.name)
      listenTo(this.selection)
      reactions += {
        case e: SelectionChanged => selelctedListIndex = this.peer.getSelectedIndex + 1
      }})) {
      border = TitledBorder(EmptyBorder(5, 5, 5, 5), "Player Names:")
    }
  }

  //TODO: Buttongroup not working correctly
  var buttongroup = new ButtonGroup()

  var rb1 = new RadioButton("2 Player") {
    listenTo(this)
    buttongroup.buttons.add(this)
    reactions += {
      case e: ButtonClicked => controller.initPlayers(2)
        gui.updateSettings()
    }
  }

  var rb2 = new RadioButton("3 Player") {
    listenTo(this)
    buttongroup.buttons.add(this)
    selected = true
    reactions += {
      case e: ButtonClicked => controller.initPlayers(3)
        gui.updateSettings()
    }
  }

  var rb3 = new RadioButton("4 Player") {
    listenTo(this)
    buttongroup.buttons.add(this)
    reactions += {
      case e: ButtonClicked => controller.initPlayers(4)
        gui.updateSettings()
    }
  }

  var rb4 = new RadioButton("5 Player") {
    buttongroup.buttons.add(this)
    listenTo(this)
    reactions += {
      case e: ButtonClicked => controller.initPlayers(5)
        gui.updateSettings()
    }
  }

  var rb5 = new RadioButton("6 Player") {
    buttongroup.buttons.add(this)
    listenTo(this)
    reactions += {
      case e: ButtonClicked => controller.initPlayers(6)
        gui.updateSettings()
    }
  }

  var rb6 = new RadioButton("7 Player") {
    buttongroup.buttons.add(this)
    listenTo(this)
    reactions += {
      case e: ButtonClicked => controller.initPlayers(7)
        gui.updateSettings()
    }
  }

  def rbBox = new BoxPanel(Orientation.Vertical) {
    preferredSize = new Dimension(140, 50)
    maximumSize = new Dimension(150, 50)
    contents ++= List(rb1, rb2, rb3, rb4, rb5, rb6)
    border = CompoundBorder(TitledBorder(EtchedBorder, "Number of Player"), EmptyBorder(10, 10, 10, 10))
  }

  var textField = new TextField()

  def chooseNameBox = new BoxPanel(Orientation.Horizontal) {
    contents += new Label("New Player Name:")
    contents += HStrut(10)
    contents += textField
    contents += HStrut(10)
    contents += new Button("Change Name") {
      listenTo(this)
      reactions += {
        case e: ButtonClicked => controller.setPlayerName(textField.text, selelctedListIndex)
          gui.updateSettings()
      }
    }
    contents += HStrut(10)
  }


  def bottomPanel = new BorderPanel {
    border = EmptyBorder(10, 10, 10, 10)
    add(chooseNameBox, BorderPanel.Position.Center)
    add(new Button("Start") {
      listenTo(this)
      reactions += {
        case e: ButtonClicked => gui.changeToGamePanel()
          Dialog.showMessage(null, "Be Ready, MrX Position will now be revealed!", "MrX Starting Position")
          Dialog.showMessage(null, "MrX is at Station: " + controller.getCurrentPlayer().station.number, "MrX Position")
      }
    }, BorderPanel.Position.East)
  }

  def getPanel(): BorderPanel = {
    var panelPlayerList = buildPanelPlayerList()
    new BorderPanel {
      add(bottomPanel, BorderPanel.Position.South)
      add(rbBox, BorderPanel.Position.West)
      add(panelPlayerList, BorderPanel.Position.East)
    }
  }

}
