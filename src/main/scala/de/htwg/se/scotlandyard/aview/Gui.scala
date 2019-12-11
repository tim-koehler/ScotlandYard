package de.htwg.se.scotlandyard.aview

import de.htwg.se.scotlandyard.ScotlandYard
import de.htwg.se.scotlandyard.controller.Controller
import javafx.geometry.{Insets, Rectangle2D}
import javafx.scene.control.Menu
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.event.ActionEvent
import scalafx.scene.Scene
import scalafx.scene.control.{Button, Label, ListView, MenuBar, MenuItem, RadioButton, ScrollPane, TextField, ToggleGroup}
import scalafx.scene.layout.{AnchorPane, Background, BackgroundImage, BorderPane, HBox, Pane, VBox}
import scalafx.scene.paint.Color
import scalafx.Includes._
import scalafx.geometry.Pos
import scalafx.scene.AccessibleRole.TextField
import scalafx.scene.image.{Image, ImageView}
import java.io.{File, FileInputStream}

import javafx.event.EventHandler
import scalafx.scene.canvas.{Canvas, GraphicsContext}
import scalafx.scene.input.{MouseDragEvent, MouseEvent, TransferMode}
import scalafx.scene.shape.Circle

object Gui extends JFXApp{
  val controller = new Controller

  val thread = new Thread {
    override def run {
      ScotlandYard.run(controller)
    }
  }
  thread.start()

  //============================================================== GUI

  val map = new Image(new File("./src/main/scala/de/htwg/se/scotlandyard/map_large.png").toURI().toString)
  var scene2: Scene = _
  var scene4: Scene = _

  val frameWidth = 1000
  val frameHeight = 800

  val buttonWidth = 200
  val buttonHeight = 30

  val defSpacing = 20
  val vBoxHeight = 2 * buttonHeight + defSpacing
  val vBoxWidth = buttonWidth

  stage = new PrimaryStage {
    title = "ScotlandYard"
    width = frameWidth
    height = frameHeight
    resizable = true
    scene = new Scene {
      stylesheets = List(getClass.getResource("styles.css").toExternalForm)
      fill = Color.LightBlue
      var button1 = new Button("Start Game") {
        id = "big-yellow"
        prefWidth = buttonWidth
        prefHeight = buttonHeight
        onAction = (event: ActionEvent) => {
          stage.scene = scene2
        }
      }
      var button2 = new Button("End Game") {
        id = "big-yellow"
        prefWidth = buttonWidth
        prefHeight = buttonHeight
        onAction = (event: ActionEvent) => {
          System.exit(0)
        }
      }
      var titlePane = new BorderPane() {
        center = new Label("Scotland Yard") {
          id = "title"
        }
      }

      val rootPane = new BorderPane()
      val vbox = new VBox()

      vbox.setPadding(new Insets(frameHeight/2 - vBoxHeight/2, 0, 0, frameWidth/2 - vBoxWidth/2))
      vbox.setSpacing(defSpacing)
      vbox.getChildren.addAll(button1, button2)

      rootPane.setTop(titlePane)
      rootPane.setCenter(vbox)
      content = rootPane
    }
  }

  scene2 = new Scene {
    fill = Color.LightBlue
    var nPlayer = 2

    var label = new Label("Number of Player:")
    var rb1 = new RadioButton("2 Player") {
      selected = true
      onAction = (event: ActionEvent) => {
        nPlayer = 2
      }
    }
    var rb2 = new RadioButton("3 Player") {
      onAction = (event: ActionEvent) => {
        nPlayer = 3
      }
    }
    var rb3 = new RadioButton("4 Player") {
      onAction = (event: ActionEvent) => {
        nPlayer = 4
      }
    }
    var rb4 = new RadioButton("5 Player") {
      onAction = (event: ActionEvent) => {
        nPlayer = 5
      }
    }
    var rb5 = new RadioButton("6 Player") {
      onAction = (event: ActionEvent) => {
        nPlayer = 6
      }
    }
    var rb6 = new RadioButton("7 Player") {
      onAction = (event: ActionEvent) => {
        nPlayer = 7
      }
    }

    val radioGroup = new ToggleGroup()
    radioGroup.toggles = List(rb1, rb2, rb3, rb4, rb5, rb6)

    var setNumberButton = new Button("Set Number") {
      onAction = (event: ActionEvent) => {
        controller.initPlayers(nPlayer)
        changeNameButton.disable = false


        var list: List[String] = List()
        for((x,i) <- controller.getPlayersList().drop(1).view.zipWithIndex) {
          x.name :: list
        }

        playerList = new ListView[String](list)
        vboxList.getChildren.update(1, playerList)

      }
    }

    var changeNameButton = new Button("Change") {
      disable = true
    }

    var startButton = new Button("Start") {
      onAction = (event: ActionEvent) => {
        stage.scene = scene4
      }
    }
    var startButtonPane = new BorderPane()
    startButtonPane.setPadding(new Insets(20, 20, 20, 20))
    startButtonPane.setCenter(startButton)

    var setNumberPane = new BorderPane()
    setNumberPane.setPadding(new Insets(20, 20, 20, 20))
    setNumberPane.setCenter(setNumberButton)

    var playerList = new ListView[String](List("Dt1", "Dt2", "Dt3", "Dt4", "Dt5", "Dt6"))
    var changeNamelabel = new Label("Change Name:")
    var playerNamesLabel = new Label("Player Names:")
    var textField = new TextField()
    val vbox = new VBox()
    var hbox = new HBox()

    hbox.setPadding(new Insets(20, 20, 20, 20))
    hbox.setSpacing(defSpacing)
    hbox.getChildren.addAll(changeNamelabel, textField, changeNameButton)

    vbox.setPadding(new Insets(20, 20, 20, 20))
    vbox.setSpacing(defSpacing)
    vbox.getChildren.addAll(label, rb1, rb2, rb3, rb4, rb5, rb6)


    var listPane = new BorderPane()
    var vboxList = new HBox()
    vboxList.setSpacing(defSpacing)
    vboxList.getChildren.addAll(playerNamesLabel, playerList)
    listPane.setRight(vboxList)

    listPane.setMaxHeight(200)
    listPane.setPadding(new Insets(20, 20, 20, 20))

    var bottomPane = new BorderPane()

    val contentPane = new BorderPane()

    bottomPane.setCenter(hbox)
    bottomPane.setRight(startButtonPane)
    bottomPane.setLeft(setNumberButton)
    bottomPane.alignmentInParent = Pos.BottomCenter

    contentPane.setRight(listPane)
    contentPane.setLeft(vbox)
    contentPane.setBottom(bottomPane)
    content = contentPane
  }

  scene4 = new Scene() {
    stylesheets = List(getClass.getResource("styles.css").toExternalForm)
    var rootPane = new BorderPane()
    var menuBar = new MenuBar()
    var fileMenu = new Menu("File")
    var optionMenu = new Menu("Options")
    var undoMenuItem = new MenuItem("Undo")
    var redoMenuItem = new MenuItem("Redo")
    optionMenu.items = List(undoMenuItem, redoMenuItem)
    var helpMenu = new Menu("Help")


    var currentPlayerLabel = new Label("Dt1")
    var taxiButton = new Button("T = 11") {
      onAction = (event: ActionEvent) => {
          
        }
      id = "defaultButton"
    }
    var busButton = new Button("B = 8") {
      id = "defaultButton"
    }
    var underButton = new Button("U = 4") {
      id = "defaultButton"
    }
    var currentRoundLabel = new Label(("Round: 3/24"))

    menuBar.menus = List(fileMenu, optionMenu, helpMenu)


    var scroll = new ScrollPane() {
      content = new ImageView {
        image = map
      }
      fitToHeight = true
    }


    var bottomBar = new BorderPane {
      left = new HBox {
        children.addAll(currentPlayerLabel, taxiButton, busButton, underButton)
        spacing = defSpacing
        padding = new Insets(20, 20, 20, 20)
      }

      right = new BorderPane {
        center = currentRoundLabel
        padding = new Insets(20, 20, 20, 20)
      }

    }


    rootPane.center = scroll
    rootPane.bottom = bottomBar
    rootPane.top = menuBar
    root = rootPane

  }

}
