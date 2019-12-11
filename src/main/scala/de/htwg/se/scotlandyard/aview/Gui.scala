package de.htwg.se.scotlandyard.aview

import de.htwg.se.scotlandyard.controller.Controller
import jdk.nashorn.internal.ir.Labels

import scala.swing._
import scala.swing.Swing.LineBorder
import scala.swing.event._
import scala.io.Source._

class Gui(controller: Controller) extends Frame {
  title = "Scotland Yard"

  val testLabel = new Label {
    text = "TestLabel"
  }

  def gamePanel = new BorderPanel {
    add(testLabel, BorderPanel.Position.Center)
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

  visible = true
  repaint()

}
