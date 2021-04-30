package de.htwg.se.scotlandyard.persistence.postgres

import de.htwg.se.scotlandyard.model.players.{Detective, Player}
import de.htwg.se.scotlandyard.model.{GameModel, Station}
import de.htwg.se.scotlandyard.persistence.PersistenceInterface
import slick.dbio.DBIO
import slick.jdbc.JdbcBackend.{Database, _}
import slick.jdbc.PostgresProfile.api._
import scala.concurrent.ExecutionContext.Implicits.global

class Postgres extends PersistenceInterface {

  val db = Database.forURL("jdbc:postgresql://postgres/scotlandyard", user = "postgres", password = "scotty4life", driver = "org.postgresql.Driver")

  val setup = DBIO.seq(
    Schemas.generalData.schema.createIfNotExists,
    //Schemas.stations.schema.createIfNotExists,
    Schemas.tickets.schema.createIfNotExists,
    Schemas.players.schema.createIfNotExists,
  )
  db.run(setup)

  override def load(): GameModel = ???

  override def save(gameModel: GameModel): Boolean = {
    var playersSeq: Seq[(Int, Int, String, String, String, Boolean)] = Seq()
    var ticketsSeq: Seq[(Int, Int, Int, Int, Int)] = Seq()

    for ((p, index) <- gameModel.players.zipWithIndex) {
      playersSeq = playersSeq ++ Seq((index, p.station.number, p.name, String.format("#%02x%02x%02x", p.color.getRed, p.color.getGreen, p.color.getBlue), p.playerType.get.toString, false))
    }

    for ((p, index) <- gameModel.players.zipWithIndex) {
      ticketsSeq = ticketsSeq ++ Seq((index, p.tickets.taxiTickets, p.tickets.busTickets, p.tickets.undergroundTickets, p.tickets.blackTickets))
    }

    val insert = DBIO.seq(
      Schemas.generalData += (0, gameModel.round, gameModel.totalRound, gameModel.win, gameModel.gameRunning, getPlayerIndex(gameModel.players, gameModel.winningPlayer), gameModel.allPlayerStuck, gameModel.WINNING_ROUND),

      Schemas.tickets ++= ticketsSeq,
      Schemas.players ++= playersSeq,
    )

    db.run(insert)
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
