package de.htwg.se.scotlandyard.persistence.mongodb

import de.htwg.se.scotlandyard.model.JsonProtocol.GameModelJsonFormat
import de.htwg.se.scotlandyard.model.GameModel
import de.htwg.se.scotlandyard.persistence.PersistenceInterface
import de.htwg.se.scotlandyard.persistence.mongodb.Helpers.GenericObservable
import spray.json.enrichAny
import spray.json._
import org.mongodb.scala._

class MongoDB extends PersistenceInterface{
  val client: MongoClient = MongoClient("mongodb://root:scotty4life@mongodb")
  val database: MongoDatabase = client.getDatabase("scotlandyard")
  val collection: MongoCollection[Document] = database.getCollection("savegame")

  override def load(): GameModel = {
    val doc: Document = collection.find().results().head
    doc.getString("savegame").parseJson.convertTo[GameModel]
  }

  override def save(gameModel: GameModel): Boolean = {
    collection.insertOne(Document("savegame"-> gameModel.toJson.prettyPrint)).results()
    true
  }

}
