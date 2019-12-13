package de.htwg.se.scotlandyard.model.player

import java.awt.Color

import de.htwg.se.scotlandyard.model.map.station.Station

class Detective(var station: Station, var name: String = "Dt1", var color: Color = Color.BLUE) extends Player {
}
