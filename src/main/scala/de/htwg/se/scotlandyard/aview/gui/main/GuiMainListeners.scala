package de.htwg.se.scotlandyard.aview.gui.main

import java.awt.event.AdjustmentEvent

import de.htwg.se.scotlandyard.aview.Gui
import de.htwg.se.scotlandyard.controllerComponent.controllerBaseImpl.Controller
import de.htwg.se.scotlandyard.model.map.station.Station
import de.htwg.se.scotlandyard.util.TicketType
import de.htwg.se.scotlandyard.util.TicketType.TicketType

import scala.swing.{ButtonGroup, Point, Reactions, ScrollPane}
import scala.swing.event.MouseClicked

class GuiMainListeners(controller: Controller, gui: Gui) {
  def addMapPanelClickListener(reactions: Reactions, guiMainComponentFactory: GuiMainComponentFactory): Unit = {
    reactions += {
      case e: MouseClicked =>
        if(controller.validateMove(getStationNextToClickedCoords(e.point.x, e.point.y).number, getCurrentTicketType(guiMainComponentFactory))) {
          controller.doMove(getStationNextToClickedCoords(e.point.x, e.point.y).number, getCurrentTicketType(guiMainComponentFactory))
          if(controller.getWin())
            controller.winGame()
          gui.updateGame()
        }
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
      val clickedPoint = new Point(xPos, yPos)
      if(station.guiCoords.distance(clickedPoint) < distance) {
        distance = station.guiCoords.distance(clickedPoint)
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
