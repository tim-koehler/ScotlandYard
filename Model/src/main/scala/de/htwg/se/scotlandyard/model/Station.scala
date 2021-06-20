package de.htwg.se.scotlandyard.model

import de.htwg.se.scotlandyard.model.StationType.StationType

case class Station(number: Integer = -1,
                   stationType: StationType = StationType.Taxi,
                   blackStation: Boolean = false,
                   neighbourTaxis: Set[Int] = Set(),
                   neighbourBuses: Set[Int] = Set(),
                   neighbourUndergrounds: Set[Int] = Set(),
                   tuiCoordinates: Coordinate =  Coordinate(),
                   guiCoordinates: Coordinate = Coordinate())