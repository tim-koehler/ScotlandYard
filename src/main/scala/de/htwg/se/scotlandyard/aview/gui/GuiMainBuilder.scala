package de.htwg.se.scotlandyard.aview.gui

import java.awt.image.BufferedImage
import java.awt.{BasicStroke, Color}
import java.io.File

import de.htwg.se.scotlandyard.aview.Gui
import de.htwg.se.scotlandyard.aview.tui.WinningState
import de.htwg.se.scotlandyard.controller.Controller
import de.htwg.se.scotlandyard.model.core.GameMaster
import de.htwg.se.scotlandyard.model.player.TicketType.TicketType
import de.htwg.se.scotlandyard.model.player.{MrX, TicketType}
import javax.imageio.ImageIO

import scala.io.Source
import scala.swing.ListView.Renderer
import scala.swing.{Action, Alignment, BorderPanel, Button, ButtonGroup, Dialog, Dimension, FlowPanel, Graphics2D, Label, ListView, Menu, MenuBar, MenuItem, Panel, Point, ScrollPane, ToggleButton}
import scala.swing.event.MouseClicked
import scala.util.{Failure, Success, Try}

class GuiMainBuilder (controller: Controller, gui: Gui) {

  val imagePath = "./src/main/scala/de/htwg/se/scotlandyard/map_large.png"
  val guiCoordinatesPath = "./src/main/scala/de/htwg/se/scotlandyard/gui_coordinates.txt"

  val image: BufferedImage = ImageIO.read(new File(imagePath))
  var coordinatesList = readCoordinatesFromFile(guiCoordinatesPath)
  var btnGroup = new ButtonGroup()

  def readCoordinatesFromFile(path: String): Option[List[Point]] = {
    var list: List[Point] = List()
    Try(Source.fromFile(path)) match {
      case Success(v) => for (line <- v.getLines()) {
        var coords = line.split(";")(0)
        var xPos = coords.split(",")(0).toInt
        var yPos = coords.split(",")(1).toInt
        list = new Point(xPos, yPos) :: list
      }; v.close()
      case Failure(e) => None
    }
    Some(list.reverse)
  }

  def getStationFromCoordinates(xPos: Int, yPos: Int): Int = {
    var distance = 9999.0
    var guessedStation = 1
    for((pos, index) <- coordinatesList.get.view.zipWithIndex) {
      if(pos.distance(new Point(xPos, yPos)) < distance) {
        distance = pos.distance(new Point(xPos, yPos))
        guessedStation = index
      }
    }
    guessedStation
  }

  def getCurrentTicket(): TicketType = {
    var btn = btnGroup.selected.get
    if(btn.text.contains("Taxi")) {
      TicketType.Taxi
    } else if(btn.text.contains("Bus")) {
      TicketType.Bus
    } else {
      TicketType.Underground
    }
  }

  def getPanel(): BorderPanel = {

    new BorderPanel() {
      val menuBar = buildMenuBar()
      val historyPanel = buildMrXHistoryPanel()
      val bottomPanel = buildBottomPanel()
      val mainPanel = buildMainPanel()

      add(menuBar, BorderPanel.Position.North)
      add(historyPanel, BorderPanel.Position.West)
      add(bottomPanel, BorderPanel.Position.South)
      add(mainPanel, BorderPanel.Position.Center)
    }
  }

  def buildMenuBar(): MenuBar = {
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
        contents += new MenuItem(Action("") {
          Dialog.showMessage(null, "Not yet implemented", ": (")
        })
      }
    }
  }

  def buildMrXHistoryPanel(): ScrollPane = {
    new ScrollPane(new ListView(controller.getPlayersList()(0).asInstanceOf[MrX].getHistory()) {
      renderer = Renderer(_.toString)
      println("history: " + controller.getPlayersList()(0).asInstanceOf[MrX].getHistory())
    })
  }

  def buildBottomPanel(): FlowPanel = {
    new FlowPanel() {
      contents += new Panel {
        background = controller.getCurrentPlayer().color
      }
      contents += new Label(controller.getCurrentPlayer().name)
      contents += new ToggleButton("Taxi: " + controller.getCurrentPlayer().taxiTickets) {
        selected = true
        btnGroup.buttons.add(this)
        verticalTextPosition = Alignment.Bottom
        horizontalTextPosition = Alignment.Center
      }
      contents += new ToggleButton("Bus: " + controller.getCurrentPlayer().busTickets) {
        btnGroup.buttons.add(this)
        verticalTextPosition = Alignment.Bottom
        horizontalTextPosition = Alignment.Center
      }
      contents += new ToggleButton("Underground: " + controller.getCurrentPlayer().undergroundTickets) {
        btnGroup.buttons.add(this)
        verticalTextPosition = Alignment.Bottom
        horizontalTextPosition = Alignment.Center
      }
      contents += new Label("Round: " + controller.getTotalRound() + "/24")
    }
  }

  def buildMainPanel(): ScrollPane = {
    val panel = new BorderPanel {
      preferredSize = new Dimension(image.getWidth, image.getHeight)

      listenTo(mouse.clicks)
      reactions += {
        case e: MouseClicked =>
          if(controller.validateMove(getStationFromCoordinates(e.point.x, e.point.y), getCurrentTicket())) {
            controller.doMove(getStationFromCoordinates(e.point.x, e.point.y), getCurrentTicket())
          }
          println("CurrentPlayer: " + controller.getCurrentPlayer().name)
          println("GuessedStation: " + getStationFromCoordinates(e.point.x, e.point.y))
          gui.updateGame()
          repaint()
      }
      override protected def paintComponent(g: Graphics2D): Unit = {
        super.paintComponent(g)
        g.drawImage(image, 0, 0, null)
        drawAllCenteredCircles(g, 50)
      }
    }
    new ScrollPane(panel)
  }

  def drawAllCenteredCircles(g: Graphics2D, r: Int): Unit = {
    g.setStroke(new BasicStroke(10.0f))
    g.setColor(Color.BLUE)

    println("Mrx: " + GameMaster.players(0).getPosition().number)
    println("Dt1: " + GameMaster.players(1).getPosition().number)
    println("Dt2: " + GameMaster.players(2).getPosition().number)

    var mrx = controller.getPlayersList()(0).asInstanceOf[MrX]
    if (mrx.isVisible) {
      g.setColor(mrx.color)
      g.drawOval(coordinatesList.get(mrx.getPosition().number).x - (r / 2), coordinatesList.get(mrx.getPosition().number).y - (r / 2), r, r)
    }

    for (p <- controller.getPlayersList().drop(1)) {
      g.setColor(p.color)
      g.drawOval(coordinatesList.get(p.getPosition().number).x - (r / 2), coordinatesList.get(p.getPosition().number).y - (r / 2), r, r)
    }
  }
}


