package de.htwg.se.scotlandyard.aview.gui

import java.awt.image.BufferedImage
import java.awt.{BasicStroke, Color}
import java.io.File

import de.htwg.se.scotlandyard.aview.Gui
import de.htwg.se.scotlandyard.aview.tui.WinningState
import de.htwg.se.scotlandyard.controller.Controller
import de.htwg.se.scotlandyard.model.core.GameMaster
import de.htwg.se.scotlandyard.model.player.TicketType
import javax.imageio.ImageIO

import scala.io.Source
import scala.swing.ListView.Renderer
import scala.swing.{Action, Alignment, BorderPanel, Button, Dialog, Dimension, FlowPanel, Graphics2D, Label, ListView, Menu, MenuBar, MenuItem, Point, ScrollPane}
import scala.swing.event.MouseClicked
import scala.util.{Failure, Success, Try}

class GuiMainBuilder (controller: Controller, gui: Gui) {

  val MRX_COLOR = Color.BLACK
  val DT1_COLOR = Color.BLUE
  val DT2_COLOR = Color.GREEN
  val DT3_COLOR = Color.ORANGE
  val DT4_COLOR = Color.MAGENTA
  val DT5_COLOR = Color.RED
  val DT6_COLOR = Color.CYAN

  val imagePath = "./src/main/scala/de/htwg/se/scotlandyard/map_large.png"
  val guiCoordinatesPath = "./src/main/scala/de/htwg/se/scotlandyard/gui_coordinates.txt"

  val image: BufferedImage = ImageIO.read(new File(imagePath))

  var coordinatesList = readCoordinatesFromFile(guiCoordinatesPath)

  def readCoordinatesFromFile(path: String): Option[List[Point]] = {
    var list: List[Point] = List()
    Try(Source.fromFile(path)) match {
      case Success(v) => for (line <- v.getLines()) {
        var coords = line.split(";")(0)
        var xPos = coords.split(",")(0).toInt
        var yPos = coords.split(",")(1).toInt
        println(xPos.toString + ", " + yPos.toString)
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
        println("distance: " + pos.distance(new Point(xPos, yPos)))
        guessedStation = index
      }
    }
    guessedStation
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
          Dialog.showMessage(null, "Not yet implemented", title=": (")
        })
        contents += new MenuItem(Action("Redo") {
          Dialog.showMessage(null, "Not yet implemented", title=": (")
        })
      }
    }
  }

  def buildMrXHistoryPanel(): ScrollPane = {

    new ScrollPane(new ListView(GameMaster.stations) {
      renderer = Renderer(_.number)
    })
  }

  def buildBottomPanel(): FlowPanel = {
    new FlowPanel() {
      contents += new Label(controller.getCurrentPlayer().name)
      contents += new Button("Taxi: " + controller.getCurrentPlayer().taxiTickets) {
        verticalTextPosition = Alignment.Bottom
        horizontalTextPosition = Alignment.Center
      }
      contents += new Button("Bus: " + controller.getCurrentPlayer().busTickets) {
        verticalTextPosition = Alignment.Bottom
        horizontalTextPosition = Alignment.Center
      }
      contents += new Button("Underground: " + controller.getCurrentPlayer().undergroundTickets) {
        verticalTextPosition = Alignment.Bottom
        horizontalTextPosition = Alignment.Center
      }
    }
  }

  def buildMainPanel(): ScrollPane = {

    val panel = new BorderPanel {
      preferredSize = new Dimension(image.getWidth, image.getHeight)

      listenTo(mouse.clicks)
      reactions += {
        case e: MouseClicked =>
          if(controller.validateMove(getStationFromCoordinates(e.point.x, e.point.y), TicketType.Taxi)) {
            controller.doMove(getStationFromCoordinates(e.point.x, e.point.y), TicketType.Taxi)
          }
          println("CurrentPlayer: " + controller.getCurrentPlayer().name)
          println("GuessedStation: " + getStationFromCoordinates(e.point.x, e.point.y))
          println("Mouse clicked at " + e.point)
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
    g.setStroke(new BasicStroke(7.0f))
    g.setColor(Color.BLUE)

    println("Mrx: " + GameMaster.players(0).getPosition().number)
    println("Dt1: " + GameMaster.players(1).getPosition().number)
    println("Dt2: " + GameMaster.players(2).getPosition().number)
    println(coordinatesList.get)

    //TODO: Match case Color to Player
    for (p <- controller.getPlayersList()) {
      if (p == controller.getPlayersList()(0)) {
        g.setColor(MRX_COLOR)
      } else if (p == controller.getPlayersList()(1)) {
        g.setColor(DT1_COLOR)
      } else if(p == controller.getPlayersList()(2)) {
        g.setColor(DT2_COLOR)
      } else if(p == controller.getPlayersList()(3)) {
        g.setColor(DT3_COLOR)
      } else if(p == controller.getPlayersList()(4)) {
        g.setColor(DT4_COLOR)
      } else if(p == controller.getPlayersList()(5)) {
        g.setColor(DT5_COLOR)
      } else {
        g.setColor(DT6_COLOR)
      }
      g.drawOval(coordinatesList.get(p.getPosition().number).x - (r / 2), coordinatesList.get(p.getPosition().number).y - (r / 2), r, r)
    }
  }
}


