package de.htwg.se.scotlandyard.aview.gui.main

import java.awt.{BasicStroke, Color}
import de.htwg.se.scotlandyard.controller.ControllerInterface
import de.htwg.se.scotlandyard.model.players.MrX

import scala.swing.{Graphics2D, Image}

class GuiMainMapDrawer(controller: ControllerInterface) {
  def drawOnMap(g: Graphics2D , image: Image): Unit = {
    g.drawImage(image, 0, 0, null)
    drawPlayerCirclesOnMap(g, 50)
  }

  private def drawPlayerCirclesOnMap(g: Graphics2D, r: Int): Unit = {
    g.setStroke(new BasicStroke(10.0f))
    g.setColor(Color.BLUE)

    val mrx = controller.getMrX
    if (mrx.isVisible) {
      g.setColor(mrx.color)
      g.drawOval(controller.getStationOfPlayer(mrx).guiCoordinates.x - (r / 2), controller.getStationOfPlayer(mrx).guiCoordinates.y - (r / 2), r, r)
    } else if(!mrx.lastSeen.equals("never")) {
      g.setColor(mrx.lastSeenColor)
      g.drawOval(controller.getStations()(mrx.lastSeen.toInt).guiCoordinates.x - (r / 2),
        controller.getStations()(mrx.lastSeen.toInt).guiCoordinates.y - (r / 2), r, r)
    }

    for (p <- controller.getDetectives) {
      g.setColor(p.color)
      g.drawOval(controller.getStationOfPlayer(mrx).guiCoordinates.x - (r / 2), controller.getStationOfPlayer(mrx).guiCoordinates.y - (r / 2), r, r)
    }
  }
}
