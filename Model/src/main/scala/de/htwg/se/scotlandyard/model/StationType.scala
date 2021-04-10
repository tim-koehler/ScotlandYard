package de.htwg.se.scotlandyard.model

object StationType extends Enumeration {
  type StationType = Value
  val Taxi, Bus, Underground = Value

  def fromString(s: String): StationType = values.find(_.toString == s).get
}
