package de.htwg.se.scotlandyard.aview

import de.htwg.se.scotlandyard.controller.Controller
import jdk.nashorn.internal.ir.Labels

import scala.swing._
import scala.swing.Swing._
import scala.swing.event._
import scala.io.Source._
import scala.reflect.internal.util.Position
import scala.swing
import scala.swing.ListView.Renderer
import scala.swing.Reactions.Reaction

class Gui(controller: Controller) extends Frame {
  title = "Scotland Yard"

  val testLabel = new Label {
    text = "TestLabel"
  }

  val chooseNameLabel = new Label("New Player Name:")

  def gamePanel = new BorderPanel {
    add(testLabel, BorderPanel.Position.Center)
  }

  val buttonStart = Button("Start") {
    this.contents = gamePanel
  }

  var nameTextBox = new TextField()

  val buttonChangeName = Button("Change Name") {
    listenTo(this)
    reactions += {
      case e: ButtonClicked => controller.setPlayerNames(nameTextBox.text, selelctedListIndex)
        update()
    }
  }

  var panelPlayerList = buildPanelPlayerList()

  var selelctedListIndex = 1

  def buildPanelPlayerList(): FlowPanel = {
    new FlowPanel(new ScrollPane(new ListView(controller.getPlayersList().drop(1)) {
      selectIndices(0)
      renderer = Renderer(_.name)
    }))
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

  def settingsPanel = new BorderPanel {
    add(bottomPanel, BorderPanel.Position.South)
    add(rbBox, BorderPanel.Position.West)
    add(panelPlayerList, BorderPanel.Position.East)
  }

  def update(): Unit = {
    contents = settingsPanel
    this.repaint()
  }

  contents = settingsPanel
  visible = true
}
