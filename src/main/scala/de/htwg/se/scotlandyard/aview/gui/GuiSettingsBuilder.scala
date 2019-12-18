package de.htwg.se.scotlandyard.aview.gui

import de.htwg.se.scotlandyard.aview.Gui
import de.htwg.se.scotlandyard.controller.Controller

import scala.collection.mutable
import scala.swing.Swing._
import scala.swing.ListView.Renderer
import scala.swing.Swing.{CompoundBorder, EmptyBorder, EtchedBorder, TitledBorder}
import scala.swing.{Action, BorderPanel, BoxPanel, Button, ButtonGroup, Component, Dialog, Dimension, FlowPanel, Label, ListView, Orientation, RadioButton, ScrollPane, TextField}
import scala.swing.event.{ButtonClicked, SelectionChanged}

class GuiSettingsBuilder(controller: Controller, gui: Gui) extends GuiBuilder {
  var selelctedListIndex = 1
  var btnGroup = new ButtonGroup()
  val settingsComponentsFactory =  new SettingsComponentFactory(controller, gui)

  override def initPanel(): BorderPanel = {

    components += buildBottomPanel()
    components += buildRbBox()
    components += buildPanelPlayerList()

    new BorderPanel {
      add(components(0), BorderPanel.Position.South)
      add(components(1), BorderPanel.Position.West)
      add(components(2), BorderPanel.Position.East)
    }
  }

  override def updatePanel(): BorderPanel = {
    new BorderPanel() {
      add(components(0), BorderPanel.Position.South)
      add(components(1), BorderPanel.Position.West)
      add(buildPanelPlayerList(), BorderPanel.Position.East)
    }
  }

  def buildPanelPlayerList(): FlowPanel = {
    new FlowPanel(new ScrollPane(new ListView(controller.getPlayersList()) {
      this.peer.setSelectedIndex(selelctedListIndex)
      preferredSize = new Dimension(150, 80)
      renderer = Renderer(_.name)
      listenTo(this.selection)
      reactions += {
        case e: SelectionChanged => if(this.peer.getModel.getElementAt(0) == this.peer.getSelectedValue) this.peer.setSelectedIndex(selelctedListIndex) else selelctedListIndex = this.peer.getSelectedIndex
      }})) {
      border = TitledBorder(EmptyBorder(5, 5, 5, 5), "Player Names:")
    }
  }

  def buildRbBox(): BoxPanel = {
    new BoxPanel(Orientation.Vertical) {
      preferredSize = new Dimension(140, 50)
      maximumSize = new Dimension(150, 50)
      contents ++= List(
        settingsComponentsFactory.createRadioButton("2 Player", btnGroup),
        settingsComponentsFactory.createRadioButton("3 Player", btnGroup),
        settingsComponentsFactory.createRadioButton("4 Player", btnGroup),
        settingsComponentsFactory.createRadioButton("5 Player", btnGroup),
        settingsComponentsFactory.createRadioButton("6 Player", btnGroup),
        settingsComponentsFactory.createRadioButton("7 Player", btnGroup))
      border = CompoundBorder(TitledBorder(EtchedBorder, "Number of Player"), EmptyBorder(10, 10, 10, 10))
    }
  }

  var textField = new TextField()

  def buildChooseNameBox(): BoxPanel = {
    new BoxPanel(Orientation.Horizontal) {
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
  }

  def buildBottomPanel(): BorderPanel = {
    new BorderPanel {
      border = EmptyBorder(10, 10, 10, 10)
      add(buildChooseNameBox(), BorderPanel.Position.Center)
      add(new Button("Start") {
        listenTo(this)
        reactions += {
          case e: ButtonClicked => controller.startGame()
        }
      }, BorderPanel.Position.East)
    }
  }
}
