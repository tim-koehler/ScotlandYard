package de.htwg.se.scotlandyard.aview.gui.main

import java.awt.image.BufferedImage
import java.io.File

import de.htwg.se.scotlandyard.aview.Gui
import de.htwg.se.scotlandyard.aview.gui.GuiBuilder
import de.htwg.se.scotlandyard.controllerComponent.ControllerInterface
import javax.imageio.ImageIO

import scala.swing.Swing.{CompoundBorder, EmptyBorder, EtchedBorder, TitledBorder}
import scala.swing.{BorderPanel, BoxPanel, Dimension, Font, MenuBar, Orientation, ScrollPane}

class GuiMainBuilder (controller: ControllerInterface, gui: Gui) extends GuiBuilder {

  val fontSize = 20

  val mapImagePath = "resources/map_large.png"
  val image: BufferedImage = ImageIO.read(new File(mapImagePath))

  var scrollBarOffsetY = 0
  var scrollBarOffsetX = 0

  val mainComponentsFactory =  new GuiMainComponentFactory(controller, gui)

  override def initPanel(): BorderPanel = {
    components = components.empty
    components += buildMenuBar()
    components += buildMrXHistoryPanel()
    components += buildBottomPanel()
    components += buildMainPanel()
    new BorderPanel() {
      add(components(0), BorderPanel.Position.North)
      add(components(1), BorderPanel.Position.West)
      add(components(2), BorderPanel.Position.South)
      add(components(3), BorderPanel.Position.Center)
    }
  }

  override def updatePanel(): BorderPanel = initPanel()

  private def buildMenuBar(): MenuBar = {
    mainComponentsFactory.createMenuBar()
  }

  private def buildMrXHistoryPanel(): BoxPanel = new BoxPanel(Orientation.Vertical) {
    contents += {
      new ScrollPane(mainComponentsFactory.createHistoryPanelListView(fontSize))
    }
    var tb = TitledBorder(EtchedBorder, "MrX History")
    tb.setTitleFont(Font.apply(this.font.getName, Font.Bold, fontSize - 5))
    border = CompoundBorder(EmptyBorder(5, 5, 0, 5), tb)
    preferredSize = new Dimension(120, gui.peer.getHeight)
  }

  private def buildBottomPanel(): BorderPanel = new BorderPanel {
    add(mainComponentsFactory.createPlayerStatsPanel(fontSize), BorderPanel.Position.West)
    add(mainComponentsFactory.createToggleButtons(fontSize), BorderPanel.Position.Center)
    add(mainComponentsFactory.createRoundPanel(fontSize), BorderPanel.Position.East)
  }

  private def buildMainPanel(): ScrollPane = {
    val panel = mainComponentsFactory.createMapPanel(image)
    mainComponentsFactory.createMapScrollPanel(panel, this)
  }
}


