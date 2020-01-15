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
      border = CompoundBorder(EmptyBorder(10, 10, 0, 10), TitledBorder(EtchedBorder, "Number of Player"))
      preferredSize = new Dimension(140, 50)
      maximumSize = new Dimension(150, 50)
      contents ++= List(
        settingsComponentsFactory.createRadioButtons("2 Player", btnGroup),
        settingsComponentsFactory.createRadioButtons("3 Player", btnGroup),
        settingsComponentsFactory.createRadioButtons("4 Player", btnGroup),
        settingsComponentsFactory.createRadioButtons("5 Player", btnGroup),
        settingsComponentsFactory.createRadioButtons("6 Player", btnGroup),
        settingsComponentsFactory.createRadioButtons("7 Player", btnGroup))
    }
  }

  def buildBottomPanel(): BorderPanel = {
    new BorderPanel {
      border = EmptyBorder(10, 10, 10, 10)
      add(buildChooseNameBox(), BorderPanel.Position.Center)
      add(buildSartLoadGameBoxPanel(), BorderPanel.Position.South)
    }
  }

  def buildSartLoadGameBoxPanel(): BorderPanel = {
    new BorderPanel {
      border = EmptyBorder(10, 0, 0, 0)
      add(new BoxPanel(Orientation.Horizontal) {
        contents += settingsComponentsFactory.createLoadButton("Load Game")
        contents += HStrut(10)
        contents += settingsComponentsFactory.createStartButton("Start new Game")
      }, BorderPanel.Position.East)
    }
  }

  def buildChooseNameBox(): BoxPanel = {
    new BoxPanel(Orientation.Horizontal) {
      border = CompoundBorder(TitledBorder(EtchedBorder, "Change Player Name"), EmptyBorder(10, 10, 10, 10))
      contents += new Label("New Player Name:")
      contents += HStrut(10)
      contents += new TextField()
      contents += HStrut(10)
      contents += settingsComponentsFactory.createChangeNameButton("Change Name")
    }
  }
}
