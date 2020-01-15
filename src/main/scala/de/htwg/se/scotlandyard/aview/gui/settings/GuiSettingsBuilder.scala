package de.htwg.se.scotlandyard.aview.gui.settings

import de.htwg.se.scotlandyard.aview.Gui
import de.htwg.se.scotlandyard.aview.gui.GuiBuilder
import de.htwg.se.scotlandyard.controllerComponent.ControllerInterface

import scala.swing.Swing.{CompoundBorder, EmptyBorder, EtchedBorder, TitledBorder, _}
import scala.swing.{BorderPanel, BoxPanel, ButtonGroup, Dimension, FlowPanel, Label, Orientation, ScrollPane, TextField}

class GuiSettingsBuilder(controller: ControllerInterface, gui: Gui) extends GuiBuilder {
  var btnGroup = new ButtonGroup()
  val settingsComponentsFactory =  new GuiSettingsComponentFactory(controller, gui)

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
    new FlowPanel(new ScrollPane(settingsComponentsFactory.createListView())) {
      border = TitledBorder(EmptyBorder(5, 5, 5, 5), "Player Names:")
    }
  }

  def buildRbBox(): BoxPanel = {
    new BoxPanel(Orientation.Vertical) {
      preferredSize = new Dimension(140, 50)
      maximumSize = new Dimension(150, 50)
      contents ++= List(
        settingsComponentsFactory.createRadioButtons("2 Player", btnGroup),
        settingsComponentsFactory.createRadioButtons("3 Player", btnGroup),
        settingsComponentsFactory.createRadioButtons("4 Player", btnGroup),
        settingsComponentsFactory.createRadioButtons("5 Player", btnGroup),
        settingsComponentsFactory.createRadioButtons("6 Player", btnGroup),
        settingsComponentsFactory.createRadioButtons("7 Player", btnGroup))
      border = CompoundBorder(TitledBorder(EtchedBorder, "Number of Player"), EmptyBorder(10, 10, 10, 10))
    }
  }

  def buildBottomPanel(): BorderPanel = {
    new BorderPanel {
      border = EmptyBorder(10, 10, 10, 10)
      add(buildChooseNameBox(), BorderPanel.Position.Center)
      add(buildSartLoadGameBoxPanel(), BorderPanel.Position.East)
    }
  }

  def buildSartLoadGameBoxPanel(): BoxPanel = {
    new BoxPanel(Orientation.Horizontal) {
      contents += settingsComponentsFactory.createLoadButton("Load Game")
      contents += HStrut(10)
      contents += settingsComponentsFactory.createStartButton("Start new Game")
      contents += HStrut(10)
    }
  }

  def buildChooseNameBox(): BoxPanel = {
    new BoxPanel(Orientation.Horizontal) {
      contents += new Label("New Player Name:")
      contents += HStrut(10)
      contents += new TextField()
      contents += HStrut(10)
      contents += settingsComponentsFactory.createChangeNameButton("Change Name")
      contents += HStrut(10)
    }
  }
}
