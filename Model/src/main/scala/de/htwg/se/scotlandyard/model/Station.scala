package de.htwg.se.scotlandyard.model

import de.htwg.se.scotlandyard.model.StationType.StationType

import scala.swing.Point

case class Station(number: Integer = -1,
                   stationType: StationType = StationType.Taxi,
                   blackStation: Boolean = false,
                   neighbourTaxis: Set[Int] = Set(),
                   neighbourBuses: Set[Int] = Set(),
                   neighbourUndergrounds: Set[Int] = Set(),
                   tuiCoordinates: Point = new Point(1, 1),
                   guiCoordinates: Point = new Point(1, 1))