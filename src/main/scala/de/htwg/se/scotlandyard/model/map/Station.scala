package de.htwg.se.scotlandyard.model.map

import de.htwg.se.scotlandyard.model.map.StationType.StationType
import de.htwg.se.scotlandyard.model.player.Player

case class Station(var number: Integer, sType: StationType, neighbourTaxis: Set[Station], neighbourBuses: Set[Station], neighbourUndergrounds: Set[Station]) {

}