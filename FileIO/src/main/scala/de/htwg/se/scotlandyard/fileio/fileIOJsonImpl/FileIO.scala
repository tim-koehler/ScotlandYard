package de.htwg.se.scotlandyard.fileio.fileIOJsonImpl

import java.awt.Color
import java.io._
import com.google.inject.Inject
import de.htwg.se.scotlandyard.fileio.FileIOInterface
import de.htwg.se.scotlandyard.model.TicketType.TicketType
import de.htwg.se.scotlandyard.model.players.{MrX, Player}
import de.htwg.se.scotlandyard.model.{GameModel, TicketType, Tickets}
import de.htwg.se.scotlandyard.model.JsonProtocol._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import spray.json.enrichAny
import spray.json._
import scala.io.Source
import scala.util.{Failure, Success, Try}

class FileIO() extends FileIOInterface {
  var pathname = "ScotlandYard.json"

  override def load(stationsFileContent: String): GameModel = {
    val source: String = Source.fromFile(pathname).getLines.mkString
    source.parseJson.convertTo[GameModel]
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
