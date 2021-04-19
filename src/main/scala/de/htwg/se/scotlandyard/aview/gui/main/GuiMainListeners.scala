package de.htwg.se.scotlandyard.aview.gui.main

import java.awt.event.AdjustmentEvent
import de.htwg.se.scotlandyard.aview.Gui
import de.htwg.se.scotlandyard.controller.ControllerInterface
import de.htwg.se.scotlandyard.model.{Coordinate, Station, TicketType}
import de.htwg.se.scotlandyard.model.TicketType.TicketType

import java.awt.geom.Point2D
import scala.swing.event.MouseClicked
import scala.swing.{Point, Reactions, ScrollPane}

class GuiMainListeners(controller: ControllerInterface, gui: Gui) {
  def addMapPanelClickListener(reactions: Reactions, guiMainComponentFactory: GuiMainComponentFactory): Unit = {
    reactions += {
      case e: MouseClicked =>
        controller.move(getStationNextToClickedCoords(e.point.x, e.point.y).number, getCurrentTicketType(guiMainComponentFactory))
        gui.updateGame()
    }
  }

  private def getCurrentTicketType(guiMainComponentFactory: GuiMainComponentFactory): TicketType = {
    val btn = guiMainComponentFactory.btnGroup.selected.get
    if(btn.text.contains("Taxi")) {
      TicketType.Taxi
    } else if(btn.text.contains("Bus")) {
      TicketType.Bus
    } else if(btn.text.contains("Black Ticket")) {
      TicketType.Black
    } else {
      TicketType.Underground
    }
  }

  private def getStationNextToClickedCoords(xPos: Int, yPos: Int): Station = {
    var distance = 9999.0
    var guessedStation: Station = controller.getStations()(0)
    for(station <- controller.getStations()) {
      val clickedPoint = Coordinate(xPos, yPos)
      if(station.guiCoordinates.distance(clickedPoint) < distance) {
        distance = station.guiCoordinates.distance(clickedPoint)
        guessedStation = station
      }
    }
    guessedStation
  }


  def addVerticalScrollBarListener(pane: ScrollPane, guiMainBuilder: GuiMainBuilder): Unit = {
    pane.verticalScrollBar.peer.addAdjustmentListener(
      (_: AdjustmentEvent) => {
        guiMainBuilder.scrollBarOffsetX = pane.horizontalScrollBar.value
        guiMainBuilder.scrollBarOffsetY = pane.verticalScrollBar.value
      })
  }

  def addHorizontalScrollBarListener(pane: ScrollPane, guiMainBuilder: GuiMainBuilder): Unit = {
    pane.horizontalScrollBar.peer.addAdjustmentListener(
      (_: AdjustmentEvent) => {
        guiMainBuilder.scrollBarOffsetX = pane.horizontalScrollBar.value
        guiMainBuilder.scrollBarOffsetY = pane.verticalScrollBar.value
      })
  }
}
