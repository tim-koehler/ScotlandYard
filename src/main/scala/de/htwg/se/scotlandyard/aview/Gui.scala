package de.htwg.se.scotlandyard.aview

import de.htwg.se.scotlandyard.ScotlandYard
import de.htwg.se.scotlandyard.controller.Controller
import javafx.geometry.Insets
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.event.ActionEvent
import scalafx.scene.Scene
import scalafx.scene.control.{Button, Label, RadioButton, ToggleGroup}
import scalafx.scene.layout.{BorderPane, VBox}
import scalafx.scene.paint.Color
import scalafx.Includes._

object Gui extends JFXApp{
  val controller = new Controller

  val thread = new Thread {
    override def run {
      ScotlandYard.run(controller)
    }
  }
  thread.start()

  //============================================================== GUI

  var scene2: Scene = _

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

      val border = new BorderPane()
      val vbox = new VBox()

      vbox.setPadding(new Insets(frameHeight/2 - vBoxHeight/2, 0, 0, frameWidth/2 - vBoxWidth/2))
      vbox.setSpacing(spacing)
      vbox.getChildren.addAll(button1, button2)

      border.setCenter(vbox)
      content = border
    }
  }

  scene2 = new Scene {
    fill = Color.LightBlue
    var button1 = new Button("Start Game") {
      prefWidth = buttonWidth
      prefHeight = buttonHeight
      onAction = (event: ActionEvent) => {

      }
    }

    var nPlayer = 2

    var label = new Label("Number of Player:")

    var rb1 = new RadioButton("2 Player") {
      nPlayer = 2
    }
    var rb2 = new RadioButton("3 Player") {
      nPlayer = 3
    }
    var rb3 = new RadioButton("4 Player") {
      nPlayer = 4
    }
    var rb4 = new RadioButton("5 Player") {
      nPlayer = 5
    }
    var rb5 = new RadioButton("6 Player") {
      nPlayer = 6
    }
    var rb6 = new RadioButton("7 Player") {
      nPlayer = 7
    }

    var buttonContinue = new Button("Continue") {
      onAction = (event: ActionEvent) => {
        controller.initPlayers(nPlayer)
      }
    }

    val radioGroup = new ToggleGroup()
    radioGroup.toggles = List(rb1, rb2, rb3, rb4, rb5, rb6)

    val border = new BorderPane()
    val vbox = new VBox()

    vbox.setPadding(new Insets(frameHeight/2 - vBoxHeight, 0, 0, frameWidth/2 - vBoxWidth/2))
    vbox.setSpacing(spacing)
    vbox.getChildren.addAll(label, rb1, rb2, rb3, rb4, rb5, rb6)

    border.setCenter(vbox)
    border.setBottom(buttonContinue)
    content = border
  }

}
