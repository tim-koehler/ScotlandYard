package de.htwg.se.scotlandyard.aview

import java.awt.{BasicStroke, Color}
import java.io.File

import de.htwg.se.scotlandyard.controller.Controller
import de.htwg.se.scotlandyard.model.core.GameMaster

import scala.swing._
import scala.swing.Swing._
import scala.swing.event._
import scala.swing.ListView.Renderer
import scala.swing.event.MouseClicked
import javax.imageio.ImageIO
import java.awt.image.BufferedImage

import de.htwg.se.scotlandyard.aview.gui.GuiMainBuilder

class Gui(controller: Controller) extends Frame {
  title = "Scotland Yard"
  preferredSize = new Dimension(500, 300)
  centerOnScreen()

  def gamePanel = new BorderPanel {
    val mainBuiler = new GuiMainBuilder()
    setMax()
    mainBuiler.getPanel()
  }

  def settingsPanel = new BorderPanel {

  }

  def update(): Unit = {
    contents = settingsPanel
    this.repaint()
  }

  def setMax() = {
    this.maximize()
  }

  contents += settingsPanel()


  visible = true
}
