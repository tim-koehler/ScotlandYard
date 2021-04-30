package de.htwg.se.scotlandyard.persistence.postgres

import de.htwg.se.scotlandyard.model.players.{Detective, Player}
import de.htwg.se.scotlandyard.model.{GameModel, Station}
import de.htwg.se.scotlandyard.persistence.PersistenceInterface
import slick.dbio.DBIO
import slick.jdbc.JdbcBackend.{Database, _}
import de.htwg.se.scotlandyard.persistence.postgres.Schemas._

import scala.concurrent.ExecutionContext.Implicits.global

class Postgres extends PersistenceInterface {

  override def load(): GameModel = ???

  override def save(gameModel: GameModel): Boolean = {
    val db = Database.forURL("jdbc:postgresql://postgres/scotlandyard", user = "postgres", password = "scotty4life", driver = "org.postgresql.Driver")

    var playersSeq: Seq[(Int, Int, String, String, String, Boolean)] = Seq()
    var ticketsSeq: Seq[(Int, Int, Int, Int, Int)] = Seq()

    for ((p, index) <- gameModel.players.zipWithIndex) {
      playersSeq = playersSeq ++ Seq((index, p.station.number, p.name, String.format("#%02x%02x%02x", p.color.getRed, p.color.getGreen, p.color.getBlue), p.playerType.get.toString, false))
    }

    for ((p, index) <- gameModel.players.zipWithIndex) {
      ticketsSeq = ticketsSeq ++ Seq((index, p.tickets.taxiTickets, p.tickets.busTickets, p.tickets.undergroundTickets, p.tickets.blackTickets))
    }

    val setup = DBIO.seq(
      Schema.generalData.schema.createIfNotExists,
      //stations.schema.create,
      Schema.tickets.schema.createIfNotExists,
      Schema.players.schema.createIfNotExists,

      Schema.generalData += (0, gameModel.round, gameModel.totalRound, gameModel.win, gameModel.gameRunning, getPlayerIndex(gameModel.players, gameModel.winningPlayer), gameModel.allPlayerStuck, gameModel.WINNING_ROUND),

      Schema.tickets ++= ticketsSeq,
      Schema.players ++= playersSeq,
    )

    db.run(setup)
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
