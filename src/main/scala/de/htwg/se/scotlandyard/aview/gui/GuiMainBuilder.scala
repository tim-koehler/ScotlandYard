package de.htwg.se.scotlandyard.aview.gui

import java.awt.event.{AdjustmentEvent, AdjustmentListener}
import java.awt.image.BufferedImage
import java.awt.{BasicStroke, Color}
import java.io.File

import de.htwg.se.scotlandyard.aview.Gui
import de.htwg.se.scotlandyard.controller.Controller
import de.htwg.se.scotlandyard.model.map.station.Station
import de.htwg.se.scotlandyard.util.TicketType.TicketType
import de.htwg.se.scotlandyard.model.player.MrX
import de.htwg.se.scotlandyard.util.TicketType
import javax.imageio.ImageIO

import scala.collection.mutable
import scala.swing.Swing._
import scala.swing.ListView.Renderer
import scala.swing.Swing.{CompoundBorder, EmptyBorder, EtchedBorder, TitledBorder}
import scala.swing.{Action, Alignment, BorderPanel, BoxPanel, ButtonGroup, Dialog, Dimension, FlowPanel, Font, Graphics2D, Label, ListView, Menu, MenuBar, MenuItem, Orientation, Panel, Point, ScrollPane, ToggleButton}
import scala.swing.event.{AdjustingEvent, MouseClicked}

class GuiMainBuilder (controller: Controller, gui: Gui) extends GuiBuilder {

  val mapImagePath = "./src/main/scala/de/htwg/se/scotlandyard/map_large.png"

  val fontSize = 20
  val image: BufferedImage = ImageIO.read(new File(mapImagePath))
  var btnGroup = new ButtonGroup()

  var scrollBarOffsetY = 0
  var scrollBarOffsetX = 0


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

  private def getStationNextToClickedCoords(xPos: Int, yPos: Int): Station = {
    var distance = 9999.0
    var guessedStation: Station = controller.getStations()(0)
    for((station, index) <- controller.getStations().zipWithIndex) {
      val clickedPoint = new Point(xPos, yPos)
      if(station.guiCoords.distance(clickedPoint) < distance) {
        distance = station.guiCoords.distance(clickedPoint)
        guessedStation = station
      }
    }
    guessedStation
  }

  private def getCurrentTicketType(): TicketType = {
    val btn = btnGroup.selected.get
    if(btn.text.contains("Taxi")) {
      TicketType.Taxi
    } else if(btn.text.contains("Bus")) {
      TicketType.Bus
    } else {
      TicketType.Underground
    }
  }

  private def buildMenuBar(): MenuBar = new MenuBar {
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
      contents += new MenuItem(Action("www.github.com/tim-koehler/ScotlandYard") {
        Dialog.showMessage(null, "Not yet implemented", ": (")
      })
      contents += new MenuItem(Action("www.github.com/roland-burke") {
        Dialog.showMessage(null, "Not yet implemented", ": (")
      })
    }
  }

  private def buildMrXHistoryPanel(): BoxPanel = new BoxPanel(Orientation.Vertical) {
    contents += {
      new ScrollPane(new ListView(controller.getPlayersList()(0).asInstanceOf[MrX].getHistory()) {
        renderer = Renderer(_.toString)
        font = Font.apply(this.font.getName, Font.Bold, fontSize)
      })
    }
    var tb = TitledBorder(EtchedBorder, "MrX History")
    tb.setTitleFont(Font.apply(this.font.getName, Font.Bold, fontSize - 5))
    border = CompoundBorder(tb, EmptyBorder(8, 8, 8, 8))
    preferredSize = new Dimension(100, gui.peer.getHeight)
  }

  private def buildBottomPanel(): BorderPanel = new BorderPanel {
    add(new BoxPanel(Orientation.Horizontal) {
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
    }, BorderPanel.Position.West)

    add(new FlowPanel() {
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
    }, BorderPanel.Position.Center)

    add(new BorderPanel() {
      add(new Label("Round: " + controller.getTotalRound() + "/24") {
        font = Font.apply(this.font.getName, Font.Bold, fontSize)
      }, BorderPanel.Position.Center)
      border = EmptyBorder(8, 8, 8, 8)
    }, BorderPanel.Position.East)
  }

  private def buildMainPanel(): ScrollPane = {
    val panel = new BorderPanel {
      preferredSize = new Dimension(image.getWidth, image.getHeight)

      listenTo(mouse.clicks)
      reactions += {
        case e: MouseClicked =>
          if(controller.validateMove(getStationNextToClickedCoords(e.point.x, e.point.y).number, getCurrentTicketType())) {
            controller.doMove(getStationNextToClickedCoords(e.point.x, e.point.y).number, getCurrentTicketType())
            if(controller.getWin())
              controller.winGame()
            gui.updateGame()
          }
      }
      override protected def paintComponent(g: Graphics2D): Unit = {
        super.paintComponent(g)
        g.drawImage(image, 0, 0, null)
        drawPlayerCirclesOnMap(g, 50)
      }
    }
    new ScrollPane(panel) {
      this.verticalScrollBar.maximum = 10000
      this.horizontalScrollBar.maximum = 10000
      this.verticalScrollBar.unitIncrement = 16
      this.horizontalScrollBar.unitIncrement = 16

      this.horizontalScrollBar.value = scrollBarOffsetX
      this.verticalScrollBar.value = scrollBarOffsetY

      this.horizontalScrollBar.peer.addAdjustmentListener((_: AdjustmentEvent) => {
        scrollBarOffsetX = horizontalScrollBar.value
        scrollBarOffsetY = verticalScrollBar.value
      })
      this.verticalScrollBar.peer.addAdjustmentListener((_: AdjustmentEvent) => {
        scrollBarOffsetX = horizontalScrollBar.value
        scrollBarOffsetY = verticalScrollBar.value
      })
    }
  }

  private def drawPlayerCirclesOnMap(g: Graphics2D, r: Int): Unit = {
    g.setStroke(new BasicStroke(10.0f))
    g.setColor(Color.BLUE)

    val mrx = controller.getPlayersList()(0).asInstanceOf[MrX]
    if (mrx.isVisible) {
      g.setColor(mrx.color)
      g.drawOval(mrx.getPosition().guiCoords.x - (r / 2), mrx.getPosition().guiCoords.y - (r / 2), r, r)
    }

    for (p <- controller.getPlayersList().drop(1)) {
      g.setColor(p.color)
      g.drawOval(p.getPosition().guiCoords.x - (r / 2), p.getPosition().guiCoords.y - (r / 2), r, r)
    }
  }
}


