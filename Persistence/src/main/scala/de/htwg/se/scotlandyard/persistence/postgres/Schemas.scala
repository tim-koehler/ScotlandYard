package de.htwg.se.scotlandyard.persistence.postgres


import slick.ast.ScalaBaseType.{booleanType, intType, stringType}
import slick.lifted.{TableQuery, Tag}
import slick.jdbc.PostgresProfile.Table

import scala.concurrent.ExecutionContext.Implicits.global
import slick.lifted.{TableQuery, Tag}

object Schemas {
  class Stations(tag: Tag) extends Table[(Int, Boolean, String, String, String, String, Int, Int, Int, Int)](tag, "Stations") {
    def number = column[Int]("station_number", O.PrimaryKey) // This is the primary key column

    def blackStation = column[Boolean]("black_station")

    def taxiNeighbours = column[String]("taxi_neighbours")

    def busNeighbours = column[String]("bus_neighbours")

    def undergroundNeighbours = column[String]("underground_neighbours")

    def blackNeighbours = column[String]("black_neighbours")

    def tuiX = column[Int]("tui_x")

    def tuiY = column[Int]("tui_y")

    def guiX = column[Int]("gui_x")

    def guiY = column[Int]("gui_y")

    // Every table needs a * projection with the same type as the table's type parameter
    def * = (number, blackStation, taxiNeighbours, busNeighbours, undergroundNeighbours, blackNeighbours, tuiX, tuiY, guiX, guiY)
  }

  val stations = TableQuery[Stations]

  class Players(tag: Tag) extends Table[(Int, Int, Int, Int, Int, Int, Int, String, String, String, Boolean)](tag, "Players") {
    def id = column[Int]("id", O.PrimaryKey)

    def index = column[Int]("index")

    def station = column[Int]("station")

    def taxiTickets = column[Int]("taxi_tickets")

    def busTickets = column[Int]("bus_tickets")

    def undergorundTickets = column[Int]("underground_tickets")

    def blackTickets = column[Int]("black_tickets")

    def name = column[String]("name")

    def color = column[String]("color")

    def playerType = column[String]("player_type")

    def isStuck = column[Boolean]("is_stuck")

    def * = (id, index, station, taxiTickets, busTickets, undergorundTickets, blackTickets, name, color, playerType, isStuck )
  }
  val players = TableQuery[Players]

  class General(tag: Tag) extends Table[(Int, Int, Int, Boolean, Boolean, Int, Boolean, Int)](tag, "General") {
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

  val general = TableQuery[General]

  class GeneralPlayers(tag: Tag) extends Table[(Int, Int)](tag, "General_Players") {

    def general_id = column[Int]("general_id")

    def players_id = column[Int]("players_id")

    def * = (general_id, players_id)

    def pk = primaryKey("pk_general_players", (general_id, players_id))
  }

  val generalPlayers = TableQuery[GeneralPlayers]

}
