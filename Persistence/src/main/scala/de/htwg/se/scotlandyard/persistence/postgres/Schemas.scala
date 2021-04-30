package de.htwg.se.scotlandyard.persistence.postgres


import slick.ast.ScalaBaseType.{booleanType, intType, stringType}
import slick.lifted.{TableQuery, Tag}
import slick.jdbc.PostgresProfile.Table

import scala.concurrent.ExecutionContext.Implicits.global
import slick.lifted.{TableQuery, Tag}

object Schemas {
  class Stations(tag: Tag) extends Table[(Int, Boolean, Int, Int, Int, Int)](tag, "Stations") {
    def number = column[Int]("station_number", O.PrimaryKey) // This is the primary key column

    def blackStation = column[Boolean]("black_station")

    def tuiX = column[Int]("tui_x")

    def tuiY = column[Int]("tui_y")

    def guiX = column[Int]("gui_x")

    def guiY = column[Int]("gui_y")

    // Every table needs a * projection with the same type as the table's type parameter
    def * = (number, blackStation, tuiX, tuiY, guiX, guiY)
  }

  val stations = TableQuery[Stations]

  class GeneralData(tag: Tag) extends Table[(Int, Int, Int, Boolean, Boolean, Int, Boolean, Int)](tag, "Genera") {
    def id = column[Int]("id", O.PrimaryKey)

    def round = column[Int]("round")

    def totalRound = column[Int]("total_round")

    def win = column[Boolean]("win")

    def gameRunning = column[Boolean]("gamerunning")

    def winningPlayer = column[Int]("winning_:player")

    def allPlayerStuck = column[Boolean]("all_player_stuck")

    def winningRound = column[Int]("winning_round")

    def * = (id, round, totalRound, win, gameRunning, winningPlayer, allPlayerStuck, winningRound)
  }

  val generalData = TableQuery[GeneralData]

  class Tickets(tag: Tag) extends Table[(Int, Int, Int, Int, Int)](tag, "Tickets") {
    def id = column[Int]("ID", O.PrimaryKey)

    def taxiTickets = column[Int]("taxi_tickets")

    def busTickets = column[Int]("bus_tickets")

    def undergroundTickets = column[Int]("underground_tickets")

    def blackTickets = column[Int]("black_tickets")

    def * = (id, taxiTickets, busTickets, undergroundTickets, blackTickets)
  }

  val tickets = TableQuery[Tickets]

  class Players(tag: Tag) extends Table[(Int, Int, String, String, String, Boolean)](tag, "Players") {
    def id = column[Int]("id")

    def station = column[Int]("station")

    def name = column[String]("name")

    def color = column[String]("color")

    def playerType = column[String]("player_type")

    def isStuck = column[Boolean]("is_stuck")

    def * = (id, station, name, color, playerType, isStuck)

    def playerTickets = foreignKey("tickets_fk", id, tickets)(_.id)
  }
  val players = TableQuery[Players]

}
