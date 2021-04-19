package de.htwg.se.scotlandyard.fileio.fileIOJsonImpl

import java.awt.Color
import java.io._
import com.google.inject.Inject
import de.htwg.se.scotlandyard.fileio.FileIOInterface
import de.htwg.se.scotlandyard.model.TicketType.TicketType
import de.htwg.se.scotlandyard.model.players.{MrX, Player}
import de.htwg.se.scotlandyard.model.{GameModel, TicketType, Tickets}
import play.api.libs.json._
import de.htwg.se.scotlandyard.model.JsonProtocol._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import de.htwg.se.scotlandyard.gameinitializer.GameInitializerInterface
import spray.json.enrichAny
import spray.json._
import scala.io.Source
import scala.util.{Failure, Success, Try}

class FileIO @Inject()(override var gameInitializer: GameInitializerInterface) extends FileIOInterface {

  var pathname = "ScotlandYard.json"

  override def load(stationsFileContent: String): GameModel = {
    val gameModel = gameInitializer.initialize(3, stationsFileContent)

    val source: String = Source.fromFile(pathname).getLines.mkString
    val loadedGameModel = source.parseJson.convertTo[GameModel]
    gameModel.copy(
      players = loadedGameModel.players,
      round = loadedGameModel.round,
      totalRound = loadedGameModel.totalRound,
      win = loadedGameModel.win,
      gameRunning = loadedGameModel.gameRunning,
      winningPlayer = loadedGameModel.winningPlayer,
      stuckPlayers = loadedGameModel.stuckPlayers,
      allPlayerStuck = loadedGameModel.allPlayerStuck,
      WINNING_ROUND = loadedGameModel.WINNING_ROUND,
      MRX_VISIBLE_ROUNDS = loadedGameModel.MRX_VISIBLE_ROUNDS
    )
  }

  override def save(gameModel: GameModel): Boolean = {
    Try {
      val pw = new PrintWriter(new File(pathname))
      pw.write(gameModel.toJson.prettyPrint)
      pw.close()
    } match
    {
      case Success(v) => true
      case Failure(e) => false
    }
  }
}
