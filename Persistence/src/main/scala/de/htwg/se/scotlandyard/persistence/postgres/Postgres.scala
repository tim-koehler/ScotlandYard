package de.htwg.se.scotlandyard.persistence.postgres

import de.htwg.se.scotlandyard.model.TicketType.TicketType
import de.htwg.se.scotlandyard.model.players.{Detective, MrX, Player, PlayerTypes}
import de.htwg.se.scotlandyard.model.{Coordinate, GameModel, PersistenceGameModel, Station, StationType, TicketType, Tickets}
import de.htwg.se.scotlandyard.persistence.PersistenceInterface
import slick.dbio.DBIO
import slick.jdbc.JdbcBackend.{Database, _}
import slick.jdbc.PostgresProfile.api._

import java.awt.Color
import scala.:+
import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success, Try}


class Postgres extends PersistenceInterface {

  val db = Database.forURL("jdbc:postgresql://postgres/scotlandyard", user = "postgres", password = "scotty4life", driver = "org.postgresql.Driver")

  val setup = DBIO.seq(
    Schemas.general.schema.createIfNotExists,
    Schemas.players.schema.createIfNotExists,
  )
  db.run(setup)

  override def load(): PersistenceGameModel = {
    val playersSeq = Await.result(db.run(Schemas.players.result), Duration.Inf)
    val generalSeq = Await.result(db.run(Schemas.general.result), Duration.Inf)

    var players: Vector[Player] = Vector()
    for (p <- playersSeq) {
      if (p._12 == (PlayerTypes.MRX.toString)) {
        var history: List[TicketType] = List()
        if (!p._11.isEmpty) {
          history = p._11.split(",").map(TicketType.parse(_)).toList
        }
        players = players :+ MrX(p._2, Tickets(p._3, p._4, p._5, p._6), p._7, Color.decode(p._8), p._9, p._10, history)
      } else {
        players = players :+ Detective(p._2, p._7, Color.decode(p._8), p._13, Tickets(p._3, p._4, p._5, p._6))
      }
    }

    var winningPlayer: Player = Detective()
    if (generalSeq.head._6 != -1) {
      winningPlayer = players(generalSeq.head._6)
    }

    Future.successful(PersistenceGameModel(
      players = players,
      round = generalSeq.head._2,
      totalRound = generalSeq.head._3,
      win = generalSeq.head._4,
      gameRunning = generalSeq.head._5,
      winningPlayer = winningPlayer,
      allPlayerStuck = generalSeq.head._7,
      WINNING_ROUND = generalSeq.head._8,
      MRX_VISIBLE_ROUNDS = generalSeq.head._9.split(",").map(x => x.toInt).toVector
    ))
  }

  override def save(persistenceGameModel: PersistenceGameModel): Future[Boolean] = {
    var playersSeq: Seq[(Int, Int, Int, Int, Int, Int, String, String, Boolean, String, String, String, Boolean)] = Seq()

    Await.result(db.run(Schemas.players.delete), Duration.Inf)
    Await.result(db.run(Schemas.general.delete), Duration.Inf)

    // Insert Players
    for ((p, index) <- persistenceGameModel.players.zipWithIndex) {
      if (index == 0) {
        val mrx = p.asInstanceOf[MrX]
        playersSeq = playersSeq :+ (index, mrx.station, mrx.tickets.taxiTickets, mrx.tickets.busTickets, mrx.tickets.undergroundTickets, mrx.tickets.blackTickets, mrx.name, String.format("#%02x%02x%02x", mrx.color.getRed, mrx.color.getGreen, mrx.color.getBlue), mrx.isVisible, mrx.lastSeen, mrx.history.mkString(","), p.playerType.get.toString, false)
      } else {
        playersSeq = playersSeq :+ (index, p.station, p.tickets.taxiTickets, p.tickets.busTickets, p.tickets.undergroundTickets, p.tickets.blackTickets, p.name, String.format("#%02x%02x%02x", p.color.getRed, p.color.getGreen, p.color.getBlue), false, "", "", p.playerType.get.toString, false)
      }
    }

    val insert = DBIO.seq(
      Schemas.general += (0, persistenceGameModel.round, persistenceGameModel.totalRound, persistenceGameModel.win, persistenceGameModel.gameRunning, getPlayerIndex(persistenceGameModel.players, persistenceGameModel.winningPlayer), persistenceGameModel.allPlayerStuck, persistenceGameModel.WINNING_ROUND, persistenceGameModel.MRX_VISIBLE_ROUNDS.mkString(",")),
    )

    val insertPlayers = DBIO.sequence( playersSeq.map(current => {
      Schemas.players += current
    })
    )

    db.run(insert)
    db.run(insertPlayers)
    Future.successful(true)
  }

  override def update(persistenceGameModel: PersistenceGameModel): Future[Boolean] = {
    save(persistenceGameModel)
  }

  override def delete(): Future[Boolean] = {
    val delete = DBIO.seq(
      Schemas.general.delete,
      Schemas.players.delete,
    )
    db.run(delete)
    Future.successful(true)
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
