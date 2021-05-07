package de.htwg.se.scotlandyard.persistence.mongodb

import de.htwg.se.scotlandyard.model.players.{Detective, MrX, PlayerTypes, Player}
import de.htwg.se.scotlandyard.model.{GameModel, Station, Coordinate, Tickets, TicketType, StationType}
import de.htwg.se.scotlandyard.persistence.PersistenceInterface
import org.bson.codecs.configuration.CodecRegistries.fromRegistries
import org.mongodb.scala.MongoClient.DEFAULT_CODEC_REGISTRY
import org.mongodb.scala.{Document, MongoClient, MongoCollection, MongoDatabase}
import org.bson.codecs.configuration.CodecRegistries
import org.bson.codecs.configuration.CodecRegistries._
import org.mongodb.scala.bson.codecs.Macros._


class MongoDB extends PersistenceInterface{

  private val customCodecs = fromProviders(classOf[GameModel], classOf[Tickets], classOf[Coordinate], classOf[Station], classOf[TicketType], classOf[StationType], classOf[PlayerTypes], classOf[Player], classOf[MrX], classOf[Detective])

  private val codecRegistry = fromRegistries(customCodecs,
    DEFAULT_CODEC_REGISTRY)

  val uri: String = "mongodb://root:scotty4life@mongodb/test?retryWrites=true&w=majority"

  val client: MongoClient = MongoClient(uri)
  val database: MongoDatabase = client.getDatabase("scotlandyard").withCodecRegistry(codecRegistry)

  val collection: MongoCollection[GameModel] = database.getCollection("scotty")

  override def load(): GameModel = ???

  override def save(gameModel: GameModel): Boolean = {
    collection.insertOne(gameModel)
    true
  }

}
