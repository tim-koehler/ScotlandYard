package de.htwg.se.scotlandyard.persistence.mongodb

import com.mongodb.BasicDBObject
import de.htwg.se.scotlandyard.model.JsonProtocol.GameModelJsonFormat
import de.htwg.se.scotlandyard.model.GameModel
import de.htwg.se.scotlandyard.persistence.PersistenceInterface
import spray.json.enrichAny
import spray.json._
import de.htwg.se.scotlandyard.persistence.mongodb.Helpers.GenericObservable
import org.mongodb.scala._
import org.mongodb.scala.model.Projections.excludeId

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

  override def load(): GameModel = {
    val doc = collection.find().projection(excludeId()).results().head
    doc.toJson().parseJson.convertTo[GameModel]
  }

  override def save(gameModel: GameModel): Boolean = {
    collection.deleteMany(new BasicDBObject()).results()
    collection.insertOne(Document(json = gameModel.toJson.toString())).results().nonEmpty
  }

  override def update(gameModel: GameModel): Boolean = {
    save(gameModel)
  }

  override def delete(): Boolean = {
    collection.deleteMany(new BasicDBObject()).results().isEmpty
  }
}