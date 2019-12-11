package de.htwg.se.scotlandyard.aview

import java.awt.{BasicStroke, Color, Graphics, Image, Shape, Stroke}
import java.io.File

import de.htwg.se.scotlandyard.controller.Controller
import de.htwg.se.scotlandyard.model.core.GameMaster

import scala.swing._
import scala.swing.ListView.Renderer
import scala.swing.event.MouseClicked

class Gui(controller: Controller) extends Frame {
  title = "Scotland Yard"
  centerOnScreen()

  import javax.imageio.ImageIO
  import java.awt.image.BufferedImage

  val image: BufferedImage = ImageIO.read(new File("./src/main/scala/de/htwg/se/scotlandyard/map_large.png"))

  preferredSize = new Dimension(500, 300)

  val testLabel = new Label {
    text = "TestLabel"
  }

  def gamePanel = new BorderPanel {

    setMax()

    val menuBar = buildMenuBar()
    val historyPanel = buildMrXHistoryPanel()
    val bottomPanel = buildBottomPanel()
    val mainPanel = buildMainPanel()

    add(menuBar, BorderPanel.Position.North)
    add(historyPanel, BorderPanel.Position.West)
    add(bottomPanel, BorderPanel.Position.South)
    add(mainPanel, BorderPanel.Position.Center)
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
      contents += new Label(GameMaster.getCurrentPlayer().name)
      contents += new Button("Taxi") {
        verticalTextPosition = Alignment.Bottom
        horizontalTextPosition = Alignment.Center
      }
      contents += new Button("Bus") {
        verticalTextPosition = Alignment.Bottom
        horizontalTextPosition = Alignment.Center
      }
      contents += new Button("Underground") {
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
          println("Mouse clicked at " + e.point)
      }
      override protected def paintComponent(g: Graphics2D): Unit = {
        super.paintComponent(g)
        g.drawImage(image, 0, 0, null)
        drawCenteredCircle(g, 272, 245, 50)
      }
    }
    new ScrollPane(panel)
  }

  def drawCenteredCircle(g: Graphics2D, x: Int, y: Int, r: Int): Unit = {
    g.setStroke(new BasicStroke(7.0f))
    g.setColor(Color.GREEN)
    g.drawOval(x - (r / 2), y - (r / 2), r, r)
  }


  val buttonStart = Button("Start") {
    println("button geht")
    this.contents = gamePanel
  }

  def buttonPanel = new BorderPanel {
      add(buttonStart, BorderPanel.Position.South)
  }

  contents = new BorderPanel {
    add(buttonPanel, BorderPanel.Position.South)
  }

  def setMax() = {
    this.maximize()
  }

  visible = true
}
