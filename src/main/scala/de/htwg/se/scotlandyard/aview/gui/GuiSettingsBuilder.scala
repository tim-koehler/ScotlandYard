package de.htwg.se.scotlandyard.aview.gui

import de.htwg.se.scotlandyard.aview.Gui
import de.htwg.se.scotlandyard.controller.Controller

import scala.swing.ListView.Renderer
import scala.swing.Swing.{CompoundBorder, EmptyBorder, EtchedBorder, TitledBorder}
import scala.swing.{Action, BorderPanel, BoxPanel, Button, ButtonGroup, Dialog, Dimension, FlowPanel, Label, ListView, Orientation, RadioButton, ScrollPane, TextField}
import scala.swing.event.{ButtonClicked, SelectionChanged}

class GuiSettingsBuilder(controller: Controller, gui: Gui) {
  var selelctedListIndex = 1

  var panelPlayerList = buildPanelPlayerList()

  def buildPanelPlayerList(): FlowPanel = {
    new FlowPanel(new ScrollPane(new ListView(controller.getPlayersList().drop(1)) {
      preferredSize = new Dimension(80, 80)
      renderer = Renderer(_.name)
      listenTo(this.selection)
      reactions += {
        case e: SelectionChanged => selelctedListIndex = this.peer.getSelectedIndex + 1
      }})) {
      border = TitledBorder(EmptyBorder(5, 5, 5, 5), "Player Names:")
    }
  }

  var rb1 = new RadioButton("2 Player") {
    action = new Action("2 Player") {
      override def apply(): Unit = {
        controller.initPlayers(2)
        panelPlayerList = buildPanelPlayerList()
        gui.updateSettings()
      }
    }
  }

  var rb2 = new RadioButton("3 Player") {
    //selected = true
    action = new Action("3 Player") {
      override def apply(): Unit = {
        controller.initPlayers(3)
        panelPlayerList = buildPanelPlayerList()
        gui.updateSettings()
      }
    }
  }

  var rb3 = new RadioButton("4 Player") {
    action = new Action("4 Player") {
      override def apply(): Unit = {
        controller.initPlayers(4)
        panelPlayerList = buildPanelPlayerList()
        gui.updateSettings()
      }
    }
  }

  var rb4 = new RadioButton("5 Player") {
    action = new Action("5 Player") {
      override def apply(): Unit = {
        controller.initPlayers(5)
        panelPlayerList = buildPanelPlayerList()
        gui.updateSettings()
      }
    }
  }

  var rb5 = new RadioButton("6 Player") {
    action = new Action("6 Player") {
      override def apply(): Unit = {
        controller.initPlayers(6)
        panelPlayerList = buildPanelPlayerList()
        gui.updateSettings()
      }
    }
  }

  var rb6 = new RadioButton("7 Player") {
    action = new Action("7 Player") {
      override def apply(): Unit = {
        controller.initPlayers(7)
        panelPlayerList = buildPanelPlayerList()
        gui.updateSettings()
      }
    }
  }

  val buttongroup = new ButtonGroup {
    buttons ++= List(rb1, rb2, rb3, rb4, rb5, rb6)
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
    contents += textField
    contents += new Button("Change Name") {
      listenTo(this)
      reactions += {
        case e: ButtonClicked => controller.setPlayerNames(textField.text, selelctedListIndex)
          gui.updateSettings()
      }
    }
  }

  def bottomPanel = new BorderPanel {
    border = EmptyBorder(10, 10, 10, 10)
    add(chooseNameBox, BorderPanel.Position.Center)
    add(new Button("Start") {
      listenTo(this)
      reactions += {
        case e: ButtonClicked => gui.changeToGamePanel()
          Dialog.showMessage(null, "MrX is at Station: " + controller.getCurrentPlayer().station.number, "MrX Position")
      }
    }, BorderPanel.Position.East)
  }

  def getPanel(): BorderPanel = {
    new BorderPanel {
      add(bottomPanel, BorderPanel.Position.South)
      add(rbBox, BorderPanel.Position.West)
      add(panelPlayerList, BorderPanel.Position.East)
    }
  }

}
