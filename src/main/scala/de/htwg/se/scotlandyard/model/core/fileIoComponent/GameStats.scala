package de.htwg.se.scotlandyard.model.core.fileIoComponent

case class DetectiveSmall(name: String, stationNumber: Int, taxiTickets: Int, busTickets: Int, undergroundTickets: Int)

case class MrXSmall(name: String, stationNumber: Int, isVisible: Boolean, lastSeen: String, blackTickets: Int, doubleTurns: Int, taxiTickets: Int, busTickets: Int, undergroundTickets: Int)

case class GameStats(round: Int, totalRound: Int, nPlayer: Int, mrX: MrXSmall, detectives: List[DetectiveSmall])
