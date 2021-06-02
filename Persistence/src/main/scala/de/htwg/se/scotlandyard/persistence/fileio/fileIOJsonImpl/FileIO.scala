package de.htwg.se.scotlandyard.persistence.fileio.fileIOJsonImpl

import java.awt.Color
import java.io._
import com.google.inject.Inject
import de.htwg.se.scotlandyard.model.TicketType.TicketType
import de.htwg.se.scotlandyard.model.players.{MrX, Player}
import de.htwg.se.scotlandyard.model.{GameModel, PersistenceGameModel, TicketType, Tickets}
import de.htwg.se.scotlandyard.model.JsonProtocol._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import de.htwg.se.scotlandyard.model.JsonProtocol.GameModelJsonFormat.PersistenceGameModelJsonFormat
import de.htwg.se.scotlandyard.persistence.PersistenceInterface
import spray.json.enrichAny
import spray.json._

import scala.concurrent.Future
import scala.io.Source
import scala.util.{Failure, Success, Try}

class FileIO() extends PersistenceInterface {
  var pathname = "ScotlandYard.json"

  override def load(): Future[PersistenceGameModel] = {
    val source: String = Source.fromFile(pathname).getLines.mkString
    Future.successful(source.parseJson.convertTo[PersistenceGameModel])
  }

  override def save(persistenceGameModel: PersistenceGameModel): Future[Boolean] = {
    Try {
      val pw = new PrintWriter(new File(pathname))
      pw.write(persistenceGameModel.toJson.prettyPrint)
      pw.close()
    } match
    {
      case Success(v) => Future.successful(true)
      case Failure(e) => Future.successful(false)
    }
  }

  override def update(persistenceGameModel: PersistenceGameModel): Future[Boolean] = {
    save(persistenceGameModel)
  }

  override def delete(): Future[Boolean] = {
      val fileTemp = new File(pathname)
      if (fileTemp.exists) {
        fileTemp.delete()
      }
    Future.successful(true)
  }
}
