package de.htwg.se.scotlandyard.persistence.postgres

import de.htwg.se.scotlandyard.model.players.{Detective, Player}
import de.htwg.se.scotlandyard.model.{GameModel, Station}
import de.htwg.se.scotlandyard.persistence.PersistenceInterface
import slick.dbio.DBIO
import slick.jdbc.JdbcBackend.{Database, _}
import slick.lifted.{TableQuery, Tag}
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.ExecutionContext.Implicits.global

class Postgres extends PersistenceInterface{

    class StuckPlayers(tag: Tag) extends Table[(Int, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean)](tag, "Stations") {
      def id = column[Int]("ID")

      def mrx = column[Boolean]("MRX")
      def detective1 = column[Boolean]("DETECTIVE1")
      def detective2 = column[Boolean]("DETECTIVE2")
      def detective3 = column[Boolean]("DETECTIVE3")
      def detective4 = column[Boolean]("DETECTIVE4")
      def detective5 = column[Boolean]("DETECTIVE5")
      def detective6 = column[Boolean]("DETECTIVE6")

      def * = (id, mrx, detective1, detective2, detective3, detective4, detective5, detective6)
    }

    val stuckPlayers = TableQuery[StuckPlayers]

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

    class GeneralData(tag: Tag) extends Table[(Int, Int, Int, Boolean, Boolean, Int, Boolean, Int)](tag, "Stations") {
      def id = column[Int]("ID")

      def round = column[Int]("ROUND")

      def totalRound = column[Int]("TOTALROUND")

      def win = column[Boolean]("WIN")

      def gameRunning = column[Boolean]("GAMERUNNING")

      def winningPlayer = column[Int]("WINNINGPLAYER")

      def allPlayerStuck = column[Boolean]("ALLPLAYERSTUCK")

      def winningRound = column[Int]("WINNINGROUND")

      def * = (id, round, totalRound, win, gameRunning, winningPlayer, allPlayerStuck, winningRound)

      def stuckPlayer = foreignKey("STUCKPLAYERS_FK", id, stuckPlayers)(_.id)
    }

  val generalData = TableQuery[GeneralData]

    class Tickets(tag: Tag) extends Table[(Int, Int, Int, Int, Int)](tag, "Stations") {
      def id = column[Int]("ID", O.PrimaryKey)

      def taxiTickets = column[Int]("TAXITICKETS")

      def busTickets = column[Int]("BUSTICKETS")

      def undergroundTickets = column[Int]("UNDERGROUNDTICKETS")

      def blackTickets = column[Int]("BLACKTICKETS")

      def * = (id, taxiTickets, busTickets, undergroundTickets, blackTickets)
    }

    val tickets = TableQuery[Tickets]

    class Players(tag: Tag) extends Table[(Int, Int, String, String, String)](tag, "Stations") {
      def id = column[Int]("ID")

      def station = column[Int]("STATION")

      def name = column[String]("NAME")

      def color = column[String]("COLOR")

      def playerType = column[String]("PLAYERTYPE")

      def * = (id, station, name, color, playerType)

      def playerTickets = foreignKey("TICKETS_FK", id, tickets)(_.id)
    }

    val players = TableQuery[Players]




  val db = Database.forConfig("postgres")

  override def load(): GameModel = ???

  override def save(gameModel: GameModel): Boolean = {

    var playersSeq: Seq[(Int, Int, String, String, String)] = Seq()
    var ticketsSeq: Seq[(Int, Int, Int, Int, Int)] = Seq()

    for ((p, index) <- gameModel.players.zipWithIndex) {
      playersSeq = playersSeq ++ Seq((index, p.station.number, p.name, String.format("#%02x%02x%02x", p.color.getRed, p.color.getGreen, p.color.getBlue), p.playerType.get.toString))
    }

    for ((p, index) <- gameModel.players.zipWithIndex) {
      ticketsSeq = ticketsSeq ++ Seq((index, p.tickets.taxiTickets, p.tickets.busTickets, p.tickets.undergroundTickets, p.tickets.blackTickets))
    }

    for ((p, index) <- gameModel.players.zipWithIndex) {
      ticketsSeq = ticketsSeq ++ Seq((index, p.tickets.taxiTickets, p.tickets.busTickets, p.tickets.undergroundTickets, p.tickets.blackTickets))
    }

    val setup = DBIO.seq(
      generalData.schema.create,
      //stations.schema.create,
      players.schema.create,
      tickets.schema.create,
      stuckPlayers.schema.create,

      generalData += (0, gameModel.round, gameModel.totalRound, gameModel.win, gameModel.gameRunning, getPlayerIndex(gameModel.players, gameModel.winningPlayer), gameModel.allPlayerStuck, gameModel.WINNING_ROUND),

      players ++= playersSeq,
      tickets ++= ticketsSeq,
      stuckPlayers += (0, false, gameModel.stuckPlayers.contains(gameModel.players(1).asInstanceOf[Detective]), gameModel.stuckPlayers.contains(gameModel.players(2).asInstanceOf[Detective]), gameModel.stuckPlayers.contains(gameModel.players(3).asInstanceOf[Detective]), gameModel.stuckPlayers.contains(gameModel.players(4).asInstanceOf[Detective]), gameModel.stuckPlayers.contains(gameModel.players(5).asInstanceOf[Detective]), gameModel.stuckPlayers.contains(gameModel.players(6).asInstanceOf[Detective]))

      // Equivalent SQL code:
      // insert into SUPPLIERS(SUP_ID, SUP_NAME, STREET, CITY, STATE, ZIP) values (?,?,?,?,?,?)
    )

    val setupFuture = db.run(setup)
    true
  }

  private def getPlayerIndex(players: Vector[Player], player: Player): Int = {
    for ((p, index) <- players.zipWithIndex) {
      if (p.name == player.name) {
        return index
      }
    }
    -1
  }
}
