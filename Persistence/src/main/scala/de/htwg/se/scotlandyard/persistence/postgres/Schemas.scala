package de.htwg.se.scotlandyard.persistence.postgres


import slick.ast.ScalaBaseType.{booleanType, intType, stringType}
import slick.lifted.{TableQuery, Tag}
import slick.jdbc.PostgresProfile.Table

import scala.concurrent.ExecutionContext.Implicits.global
import slick.lifted.{TableQuery, Tag}
import slick.jdbc.PostgresProfile.api._

object Schemas {
  class Stations(tag: Tag) extends Table[(Int, Boolean, Int, Int, Int, Int)](tag, "Stations") {
    def number = column[Int]("STATION_NUMBER", O.PrimaryKey) // This is the primary key column

    def blackStation = column[Boolean]("BLACK_STATION")

    def tuiX = column[Int]("TUIX")

    def tuiY = column[Int]("TUIY")

    def guiX = column[Int]("GUIX")

    def guiY = column[Int]("GUIY")

    // Every table needs a * projection with the same type as the table's type parameter
    def * = (number, blackStation, tuiX, tuiY, guiX, guiY)
  }

  val stations = TableQuery[Stations]

  class GeneralData(tag: Tag) extends Table[(Int, Int, Int, Boolean, Boolean, Int, Boolean, Int)](tag, "GeneralData") {
    def id = column[Int]("ID")

    def round = column[Int]("ROUND")

    def totalRound = column[Int]("TOTALROUND")

    def win = column[Boolean]("WIN")

    def gameRunning = column[Boolean]("GAMERUNNING")

    def winningPlayer = column[Int]("WINNINGPLAYER")

    def allPlayerStuck = column[Boolean]("ALLPLAYERSTUCK")

    def winningRound = column[Int]("WINNINGROUND")

    def * = (id, round, totalRound, win, gameRunning, winningPlayer, allPlayerStuck, winningRound)
  }

  val generalData = TableQuery[GeneralData]

  class Tickets(tag: Tag) extends Table[(Int, Int, Int, Int, Int)](tag, "Tickets") {
    def id = column[Int]("ID", O.PrimaryKey)

    def taxiTickets = column[Int]("TAXITICKETS")

    def busTickets = column[Int]("BUSTICKETS")

    def undergroundTickets = column[Int]("UNDERGROUNDTICKETS")

    def blackTickets = column[Int]("BLACKTICKETS")

    def * = (id, taxiTickets, busTickets, undergroundTickets, blackTickets)
  }

  val tickets = TableQuery[Tickets]

  class Players(tag: Tag) extends Table[(Int, Int, String, String, String, Boolean)](tag, "Players") {
    def id = column[Int]("ID")

    def station = column[Int]("STATION")

    def name = column[String]("NAME")

    def color = column[String]("COLOR")

    def playerType = column[String]("PLAYERTYPE")

    def isStuck = column[Boolean]("ISSTUCK")

    def * = (id, station, name, color, playerType, isStuck)

    def playerTickets = foreignKey("TICKETS_FK", id, tickets)(_.id)
  }
  val players = TableQuery[Players]

}
