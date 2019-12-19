package de.htwg.se.scotlandyard.aview.gui.main

import java.awt.event.AdjustmentEvent
import java.awt.image.BufferedImage

import de.htwg.se.scotlandyard.aview.Gui
import de.htwg.se.scotlandyard.controller.Controller
import de.htwg.se.scotlandyard.model.player.MrX
import de.htwg.se.scotlandyard.util.TicketType.TicketType

import scala.swing.ListView.Renderer
import scala.swing.Swing.{EmptyBorder, HStrut}
import scala.swing.event.MouseClicked
import scala.swing.{Action, BorderPanel, BoxPanel, ButtonGroup, Component, Dialog, Dimension, FlowPanel, Font, Graphics2D, Label, ListView, Menu, MenuBar, MenuItem, Orientation, Panel, ScrollPane, ToggleButton}

class GuiMainComponentFactory(controller: Controller, gui: Gui) {

  val mainListeners = new GuiMainListeners(controller, gui)
  val btnGroup = new ButtonGroup()
  val guiMainMapDrawer = new GuiMainMapDrawer(controller)

  def createRoundPanel(fontSize: Integer): BorderPanel = {
    new BorderPanel() {
      add(new Label("Round: " + controller.getTotalRound() + "/24") {
        font = Font.apply(this.font.getName, Font.Bold, fontSize)
      }, BorderPanel.Position.Center)
      border = EmptyBorder(8, 8, 8, 8)
    }
  }

  def createPlayerStatsPanel(fontSize: Integer): BoxPanel = {
    new BoxPanel(Orientation.Horizontal) {
      contents += new Label("Current Player:") {
        font = Font.apply(this.font.getName, Font.Bold, fontSize)
      }
      contents += HStrut(10)
      contents += new Panel {
        background = controller.getCurrentPlayer().color
        preferredSize = new Dimension(30, 30)
      }
      contents += HStrut(5)
      contents += new Label(controller.getCurrentPlayer().name) {
        font = Font.apply(this.font.getName, Font.Bold, fontSize)
      }
      border = EmptyBorder(8, 8, 8, 8)
    }
  }

  def createMapScrollPanel(panel: BorderPanel, guiMainBuilder: GuiMainBuilder): ScrollPane = {
    new ScrollPane(panel) {
      this.verticalScrollBar.maximum = 10000
      this.horizontalScrollBar.maximum = 10000
      this.verticalScrollBar.unitIncrement = 16
      this.horizontalScrollBar.unitIncrement = 16

      this.horizontalScrollBar.value = guiMainBuilder.scrollBarOffsetX
      this.verticalScrollBar.value = guiMainBuilder.scrollBarOffsetY

      mainListeners.addHorizontalScrollBarListener(this, guiMainBuilder)
      mainListeners.addVerticalScrollBarListener(this, guiMainBuilder)
    }
  }

  def createMapPanel(image: BufferedImage): BorderPanel = {
    val guiMainComponentFactory = this
    new BorderPanel {
      preferredSize = new Dimension(image.getWidth, image.getHeight)

      listenTo(mouse.clicks)
      mainListeners.addMapPanelClickListener(reactions, guiMainComponentFactory)

      override protected def paintComponent(g: Graphics2D): Unit = {
        super.paintComponent(g)
        guiMainMapDrawer.drawOnMap(g, image)
      }
    }
  }

  def createHistoryPanelListView(fontSize: Integer): ListView[TicketType] = {
    new ListView(controller.getPlayersList()(0).asInstanceOf[MrX].getHistory()) {
      renderer = Renderer(_.toString)
      font = Font.apply(this.font.getName, Font.Bold, fontSize)
    }
  }

  def createMenuBar(): MenuBar = {
    new MenuBar {
      contents += new Menu("Files") {
        contents += new MenuItem(Action("Save") {
          Dialog.showMessage(null, "Not yet implemented", ": (")
        })
        contents += new MenuItem(Action("Load") {
          Dialog.showMessage(null, "Not yet implemented", ": (")
        })
      }
      contents += new Menu("Options") {
        contents += new MenuItem(Action("Undo") {
          controller.undoValidateAndMove()
          gui.updateGame()
        })
        contents += new MenuItem(Action("Redo") {
          controller.redoValidateAndMove()
          gui.updateGame()
        })
      }
      contents += new Menu("Help") {
        contents += new MenuItem(Action("Info") {
          Dialog.showMessage(null, "All possible starting Stations for Mr.X are:\n35, 45, 51, 71, 78, 104, 106, 127, 132, 146, 166, 170 and 172", "Info")
        })
        contents += new MenuItem(Action("www.github.com/tim-koehler/ScotlandYard") {
          Dialog.showMessage(null, "Not yet implemented", ": (")
        })
        contents += new MenuItem(Action("www.github.com/roland-burke") {
          Dialog.showMessage(null, "Not yet implemented", ": (")
        })
      }
    }
  }

  def createToggleButtons(fontSize: Integer): FlowPanel = {
    new FlowPanel() {
      contents += new ToggleButton("Taxi: " + controller.getCurrentPlayer().taxiTickets) {
        selected = true
        btnGroup.buttons.add(this)
        font = Font.apply(this.font.getName, Font.Bold, fontSize)
      }
      contents += new ToggleButton("Bus: " + controller.getCurrentPlayer().busTickets) {
        btnGroup.buttons.add(this)
        font = Font.apply(this.font.getName, Font.Bold, fontSize)
      }
      contents += new ToggleButton("Underground: " + controller.getCurrentPlayer().undergroundTickets) {
        btnGroup.buttons.add(this)
        font = Font.apply(this.font.getName, Font.Bold, fontSize)
      }
      contents += new ToggleButton("Black Ticket: " + controller.getPlayersList()(0).asInstanceOf[MrX].blackTickets) {
        enabled = false
        btnGroup.buttons.add(this)
        font = Font.apply(this.font.getName, Font.Bold, fontSize)
      }
      contents += new ToggleButton("Double Turn: " + controller.getPlayersList()(0).asInstanceOf[MrX].doubleTurn) {
        enabled = false
        font = Font.apply(this.font.getName, Font.Bold, fontSize)
      }
    }
  }

}
