package de.htwg.se.scotlandyard.aview

import de.htwg.se.scotlandyard.ScotlandYard
import de.htwg.se.scotlandyard.controller.Controller
import javafx.geometry.Insets
import javafx.scene.control.Menu
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.event.ActionEvent
import scalafx.scene.Scene
import scalafx.scene.control.{Button, Label, ListView, MenuBar, RadioButton, TextField, ToggleGroup}
import scalafx.scene.layout.{Background, BorderPane, HBox, VBox}
import scalafx.scene.paint.Color
import scalafx.Includes._
import scalafx.geometry.Pos
import scalafx.scene.AccessibleRole.TextField
import scalafx.scene.image.{Image, ImageView}

import java.io.{ File, FileInputStream }

object Gui extends JFXApp{
  val controller = new Controller

  val thread = new Thread {
    override def run {
      ScotlandYard.run(controller)
    }
  }
  thread.start()

  //============================================================== GUI

  //val map = new Image(new File("."+ File.separator +"src" + File.separator + "main" + File.separator + "scala" + File.separator + "de" + File.separator + "htwg" + File.separator + "se" + File.separator + "scotlandyard" + File.separator + "map_large.png").toURI().toString)
  val map = new Image(new File("./src/main/scala/de/htwg/se/scotlandyard/map_large.png").toURI().toString)
  var scene2: Scene = _
  var scene4: Scene = _

  val frameWidth = 600
  val frameHeight = 400

  val buttonWidth = 200
  val buttonHeight = 30

  val spacing = 10
  val vBoxHeight = 2 * buttonHeight + spacing
  val vBoxWidth = buttonWidth

  stage = new PrimaryStage {
    title = "ScotlandYard"
    width = frameWidth
    height = frameHeight
    resizable = true
    scene = new Scene {
      fill = Color.LightBlue
      var button1 = new Button("Start Game") {
        prefWidth = buttonWidth
        prefHeight = buttonHeight
        onAction = (event: ActionEvent) => {
          stage.scene = scene2
        }
      }
      var button2 = new Button("End Game") {
        prefWidth = buttonWidth
        prefHeight = buttonHeight
        onAction = (event: ActionEvent) => {
          System.exit(0)
        }
      }

      val rootPane = new BorderPane()
      val vbox = new VBox()

      vbox.setPadding(new Insets(frameHeight/2 - vBoxHeight/2, 0, 0, frameWidth/2 - vBoxWidth/2))
      vbox.setSpacing(spacing)
      vbox.getChildren.addAll(button1, button2)

      rootPane.setStyle("-fx-background-colour: #00F88F")

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

    var buttonContinue = new Button("Continue") {
      onAction = (event: ActionEvent) => {
        controller.initPlayers(nPlayer)
        stage.scene = new Scene {
          fill = Color.LightBlue

          var playerList = new ListView[String](List("Dt1", "Dt2", "Dt3", "Dt4", "Dt5", "Dt6"))
          var label = new Label("Change Name:")
          var textField = new TextField()
          var button = new Button("Change")
          var button2 = new Button("Start") {
            onAction = (event: ActionEvent) => {
              stage.scene = scene4
            }
          }

          var listPane = new BorderPane()
          listPane.setCenter(playerList)

          listPane.setMaxHeight(200)
          listPane.setPadding(new Insets(20, 20, 20, 20))

          var hbox = new HBox()

          hbox.setPadding(new Insets(20, 20, 20, 20))
          hbox.setSpacing(spacing)
          hbox.getChildren.addAll(label, textField, button)

          var bottomPane = new BorderPane()
          var buttonPane = new BorderPane()
          buttonPane.setPadding(new Insets(20, 20, 20, 20))
          buttonPane.setCenter(button2)
          bottomPane.setLeft(hbox)
          bottomPane.setRight(buttonPane)
          bottomPane.alignmentInParent = Pos.BottomCenter


          var borderPane = new BorderPane()
          borderPane.setLeft(listPane)
          borderPane.setBottom(bottomPane)

          content = borderPane
        }
      }
    }

    val radioGroup = new ToggleGroup()
    radioGroup.toggles = List(rb1, rb2, rb3, rb4, rb5, rb6)

    val border = new BorderPane()
    val buttonPane = new BorderPane()
    val vbox = new VBox()

    vbox.setPadding(new Insets(frameHeight/2 - vBoxHeight, 0, 0, frameWidth/2 - vBoxWidth/2))
    vbox.setSpacing(spacing)
    vbox.getChildren.addAll(label, rb1, rb2, rb3, rb4, rb5, rb6)

    buttonPane.setBottom(buttonContinue)

    border.setCenter(vbox)
    border.setRight(buttonPane)
    content = border
  }

  scene4 = new Scene() {
    var rootPane = new BorderPane()
    var menuBar = new MenuBar()
    var fileMenu = new Menu("File")
    var optionMenu = new Menu("Options")
    var helpMenu = new Menu("Help")

    menuBar.menus = List(fileMenu, optionMenu, helpMenu)

    var image = new ImageView(map)

    rootPane.center = image
    rootPane.top = menuBar
    root = rootPane

  }

}
