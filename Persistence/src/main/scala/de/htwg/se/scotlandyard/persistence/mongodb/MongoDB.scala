package de.htwg.se.scotlandyard.persistence.mongodb
import com.mongodb.client.result.DeleteResult
import com.mongodb.BasicDBObject
import de.htwg.se.scotlandyard.model.JsonProtocol.GameModelJsonFormat
import de.htwg.se.scotlandyard.model.JsonProtocol.GameModelJsonFormat.PersistenceGameModelJsonFormat
import de.htwg.se.scotlandyard.model.{GameModel, PersistenceGameModel}
import de.htwg.se.scotlandyard.persistence.PersistenceInterface
import spray.json.enrichAny
import spray.json._
import de.htwg.se.scotlandyard.persistence.mongodb.Helpers.GenericObservable
import org.mongodb.scala._
import org.mongodb.scala.model.Projections.excludeId

import scala.concurrent.Future

class MongoDB extends PersistenceInterface{
  val client: MongoClient = MongoClient("mongodb://root:scotty4life@mongodb")
  val database: MongoDatabase = client.getDatabase("scotlandyard")
  try {
    database.createCollection("savegame").results()
    println("Collection 'savegame' created")
  } catch {
    case _: Throwable => println("Collection 'savegame' already exists")
  }
  val collection: MongoCollection[Document] = database.getCollection("savegame")

  override def load(): Future[PersistenceGameModel] = {
    val doc = collection.find().projection(excludeId()).results().head
    Future.successful(doc.toJson().parseJson.convertTo[PersistenceGameModel])
  }

  override def save(persistenceGameModel: PersistenceGameModel): Future[Boolean] = {
    collection.deleteMany(new BasicDBObject()).results()
    Future.successful(collection.insertOne(Document(json = persistenceGameModel.toJson.toString())).results().nonEmpty)
  }

  override def update(persistenceGameModel: PersistenceGameModel): Future[Boolean] = {
    save(persistenceGameModel)
  }

  override def delete(): Future[Boolean] = {
    collection.deleteMany(new BasicDBObject()).results()
    Future.successful(true)
  }
}