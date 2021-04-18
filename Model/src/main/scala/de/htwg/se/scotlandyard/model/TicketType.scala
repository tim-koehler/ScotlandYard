package de.htwg.se.scotlandyard.model

object TicketType extends Enumeration {
  type TicketType = Value
  val Taxi, Bus, Underground, Black, Invalid = Value

  def of(ticket: String): TicketType = {
    ticket match {
      case "t" => TicketType.Taxi
      case "b" => TicketType.Bus
      case "u" => TicketType.Underground
      case "x" => TicketType.Black
      case _ => TicketType.Invalid
    }
  }

  def parse(ticket: String): TicketType = {
    println(ticket)
    ticket match {
      case "Taxi" => TicketType.Taxi
      case "Bus" => TicketType.Bus
      case "Underground" => TicketType.Underground
      case "Black" => TicketType.Black
      case _ => TicketType.Invalid
    }
  }

  def of(ticket: Char): TicketType = {
    ticket match {
      case 't' => TicketType.Taxi
      case 'b' => TicketType.Bus
      case 'u' => TicketType.Underground
      case 'x' => TicketType.Black
      case _ => TicketType.Invalid
    }
  }
}
