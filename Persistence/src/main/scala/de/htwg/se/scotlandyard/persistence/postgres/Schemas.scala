package de.htwg.se.scotlandyard.persistence.postgres

import slick.ast.ScalaBaseType.{booleanType, intType, stringType}
import slick.jdbc.PostgresProfile.Table

import slick.lifted.{TableQuery, Tag}

object Schemas {
  class Players(tag: Tag) extends Table[(Int, Int, Int, Int, Int, Int, String, String, Boolean, String, String, String, Boolean)](tag, "Players") {
    def id = column[Int]("id", O.PrimaryKey)

    def station = column[Int]("station")

    def taxiTickets = column[Int]("taxi_tickets")

    def busTickets = column[Int]("bus_tickets")

    def undergorundTickets = column[Int]("underground_tickets")

    def blackTickets = column[Int]("black_tickets")

    def name = column[String]("name")

    def color = column[String]("color")

    def isVisible = column[Boolean]("is_visible")

    def lastSeen = column[String]("last_seen")

    def history = column[String]("history")

    def playerType = column[String]("player_type")

    def isStuck = column[Boolean]("is_stuck")

    def * = (id, station, taxiTickets, busTickets, undergorundTickets, blackTickets, name, color, isVisible, lastSeen, history, playerType, isStuck )
  }
  val players = TableQuery[Players]

  class General(tag: Tag) extends Table[(Int, Int, Int, Boolean, Boolean, Int, Boolean, Int, String)](tag, "General") {
    def id = column[Int]("id", O.PrimaryKey)

    def round = column[Int]("round")

    def totalRound = column[Int]("total_round")

    def win = column[Boolean]("win")

    def gameRunning = column[Boolean]("gamerunning")

    def winningPlayer = column[Int]("winning_:player")

    def allPlayerStuck = column[Boolean]("all_player_stuck")

    def winningRound = column[Int]("winning_round")

    def mrxVisibleRounds = column[String]("mrx_visible_rounds")

    def * = (id, round, totalRound, win, gameRunning, winningPlayer, allPlayerStuck, winningRound, mrxVisibleRounds)
  }
  val general = TableQuery[General]
}
