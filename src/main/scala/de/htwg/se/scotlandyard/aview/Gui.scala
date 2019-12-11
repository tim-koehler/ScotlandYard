package de.htwg.se.scotlandyard.aview

import java.awt.{BasicStroke, Color, Graphics, Image, Shape, Stroke}
import java.io.File

import de.htwg.se.scotlandyard.controller.Controller
import de.htwg.se.scotlandyard.model.core.GameMaster

import scala.swing._
import scala.swing.Swing._
import scala.swing.event._
import scala.io.Source._
import scala.reflect.internal.util.Position
import scala.swing
import scala.swing.ListView.Renderer
import scala.swing.Reactions.Reaction
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

  val chooseNameLabel = new Label("New Player Name:")

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
    this.contents = gamePanel
  }

  var nameTextBox = new TextField()

  var selelctedListIndex = 1

  val buttonChangeName = Button("Change Name") {
    controller.setPlayerNames(nameTextBox.text, selelctedListIndex)
    update()
  }

  var panelPlayerList = buildPanelPlayerList()


  def buildPanelPlayerList(): FlowPanel = {
    new FlowPanel(new ScrollPane(new ListView(controller.getPlayersList().drop(1)) {
      renderer = Renderer(_.name)
      listenTo(this.selection)
      reactions += {
        case e: SelectionChanged => println(this.peer.getSelectedIndex)
      }}))
  }

  def setMax() = {
    this.maximize()
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
