package de.htwg.se.scotlandyard.persistence.postgres

import de.htwg.se.scotlandyard.model.players.{Detective, MrX, Player}
import de.htwg.se.scotlandyard.model.{GameModel, Station}
import de.htwg.se.scotlandyard.persistence.PersistenceInterface
import slick.dbio.DBIO
import slick.jdbc.JdbcBackend.{Database, _}
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.ExecutionContext.Implicits.global

class Postgres extends PersistenceInterface {

  val db = Database.forURL("jdbc:postgresql://postgres/scotlandyard", user = "postgres", password = "scotty4life", driver = "org.postgresql.Driver")

  val setup = DBIO.seq(
    Schemas.general.schema.createIfNotExists,
    Schemas.stations.schema.createIfNotExists,
    Schemas.generalPlayers.schema.createIfNotExists,
    Schemas.players.schema.createIfNotExists,
  )
  db.run(setup)

  override def load(): GameModel = {
    GameModel(round = -420)
  }

  override def save(gameModel: GameModel): Boolean = {
    var playersSeq: Seq[(Int, Int, Int, Int, Int, Int, Int, String, String, String, Boolean)] = Seq()
    var generalPlayersSeq: Seq[(Int, Int)] = Seq()
    var stationsSeq: Seq[(Int, Boolean, String, String, String, String, Int, Int, Int, Int)] = Seq()

    // Insert Players
    for ((p, index) <- gameModel.players.zipWithIndex) {
      var blackTickets = 0
      if (index == 0) {
        blackTickets = p.asInstanceOf[MrX].tickets.blackTickets
      }
      playersSeq = playersSeq :+ (index, index, p.station.number, p.tickets.taxiTickets, p.tickets.busTickets, p.tickets.undergroundTickets, blackTickets, p.name, String.format("#%02x%02x%02x", p.color.getRed, p.color.getGreen, p.color.getBlue), p.playerType.get.toString, false)
    }

    // Insert general and players mapping
    for ((p, index) <- gameModel.players.zipWithIndex) {
      generalPlayersSeq = generalPlayersSeq :+ (0, index)
    }

    // Insert stations
    for (s <- gameModel.stations) {
      stationsSeq = stationsSeq :+ (s.number, s.blackStation, neighboursToString(s.neighbourTaxis), neighboursToString(s.neighbourBuses), neighboursToString(s.neighbourUndergrounds), neighboursToString(Set(0,1,2)), s.tuiCoordinates.x, s.tuiCoordinates.y, s.guiCoordinates.x, s.guiCoordinates.y)
    }

    val insert = DBIO.seq(
      // Insert general
      Schemas.general += (0, gameModel.round, gameModel.totalRound, gameModel.win, gameModel.gameRunning, getPlayerIndex(gameModel.players, gameModel.winningPlayer), gameModel.allPlayerStuck, gameModel.WINNING_ROUND),

      Schemas.generalPlayers ++= generalPlayersSeq,
      Schemas.players ++= playersSeq,
      Schemas.stations ++= stationsSeq,
    )

    val insertGeneralPlayers = DBIO.sequence( generalPlayersSeq.map(current => {
      Schemas.generalPlayers += current
      })
    )

    val insertPlayers = DBIO.sequence( playersSeq.map(current => {
      Schemas.players += current
    })
    )

    val insertStations = DBIO.sequence( stationsSeq.map(current => {
      Schemas.stations += current
    })
    )

    db.run(insert)
    db.run(insertGeneralPlayers)
    db.run(insertPlayers)
    db.run(insertStations)

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

  private def neighboursToString(neighbours: Set[Int]): String = {
    neighbours.mkString(",")
  }

  private def stringToNeighboursSet(neighboursString: String): Set[Int] = {
    val neighbours = neighboursString.split(",")
    var neighboursSet: Set[Int] = Set()

    for (n <- neighbours) {
      neighboursSet += n.toInt
    }
    neighboursSet
  }
}
