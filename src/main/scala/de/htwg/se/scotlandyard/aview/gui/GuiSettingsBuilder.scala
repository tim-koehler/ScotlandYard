package de.htwg.se.scotlandyard.aview.gui

import scala.swing.ListView.Renderer
import scala.swing.Swing.{CompoundBorder, EmptyBorder, EtchedBorder, TitledBorder}
import scala.swing.{Action, BorderPanel, BoxPanel, Button, ButtonGroup, FlowPanel, Label, ListView, Orientation, RadioButton, ScrollPane, TextField}
import scala.swing.event.SelectionChanged

class GuiSettingsBuilder {

  val buttonStart = Button("Start") {
    this.contents = gamePanel
  }

  def getPanel(): BorderPanel = {
    new BorderPanel {


      add(bottomPanel, BorderPanel.Position.South)
      add(rbBox, BorderPanel.Position.West)
      add(panelPlayerList, BorderPanel.Position.East)
    }
  }

  var nameTextBox = new TextField()

  var selelctedListIndex = 1

  val buttonChangeName = Button("Change Name") {
    controller.setPlayerNames(nameTextBox.text, selelctedListIndex)
    update()
  }

  var panelPlayerList = buildPanelPlayerList()

  val chooseNameLabel = new Label("New Player Name:")

  def buildPanelPlayerList(): FlowPanel = {
    new FlowPanel(new ScrollPane(new ListView(controller.getPlayersList().drop(1)) {
      renderer = Renderer(_.name)
      listenTo(this.selection)
      reactions += {
        case e: SelectionChanged => println(this.peer.getSelectedIndex)
      }}))
  }

  var rb1 = new RadioButton("2 Player") {
    action = new Action("2 Player") {
      override def apply(): Unit = {
        controller.initPlayers(2)
        panelPlayerList = buildPanelPlayerList()
        update()
      }
    }
  }

  var rb2 = new RadioButton("3 Player") {
    selected = true
    action = new Action("3 Player") {
      override def apply(): Unit = {
        controller.initPlayers(3)
        panelPlayerList = buildPanelPlayerList()
        update()
      }
    }
  }

  var rb3 = new RadioButton("4 Player") {
    action = new Action("4 Player") {
      override def apply(): Unit = {
        controller.initPlayers(4)
        panelPlayerList = buildPanelPlayerList()
        update()
      }
    }
  }

  var rb4 = new RadioButton("5 Player") {
    action = new Action("5 Player") {
      override def apply(): Unit = {
        controller.initPlayers(5)
        panelPlayerList = buildPanelPlayerList()
        update()
      }
    }
  }

  var rb5 = new RadioButton("6 Player") {
    action = new Action("6 Player") {
      override def apply(): Unit = {
        controller.initPlayers(6)
        panelPlayerList = buildPanelPlayerList()
        update()
      }
    }
  }

  var rb6 = new RadioButton("7 Player") {
    action = new Action("7 Player") {
      override def apply(): Unit = {
        controller.initPlayers(7)
        panelPlayerList = buildPanelPlayerList()
        update()
      }
    }
  }

  val buttongroup = new ButtonGroup {
    buttons ++= List(rb1, rb2, rb3, rb4, rb5, rb6)
  }

  def rbBox = new BoxPanel(Orientation.Vertical) {
    border = CompoundBorder(TitledBorder(EtchedBorder, "Number of Player"), EmptyBorder(5,5,5,5))
    contents ++= List(rb1, rb2, rb3, rb4, rb5, rb6)
  }

  def chooseNameBox = new BoxPanel(Orientation.Horizontal) {
    contents ++= List(chooseNameLabel, nameTextBox, buttonChangeName)
  }

  def bottomPanel = new BorderPanel {
    add(chooseNameBox, BorderPanel.Position.Center)
    add(buttonStart, BorderPanel.Position.East)
  }

}
