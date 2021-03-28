package de.htwg.se.scotlandyard.model

case class Tickets(var taxiTickets: Int = 0,
                   var busTickets: Int = 0,
                   var undergroundTickets: Int = 0,
                   var blackTickets: Int = 0)
