package de.htwg.se.scotlandyard.persistence.postgres

import de.htwg.se.scotlandyard.model.GameModel
import de.htwg.se.scotlandyard.persistence.PersistenceInterface
import slick.jdbc.JdbcBackend._

class Postgres extends PersistenceInterface{

  val db = Database.forConfig("postgres")

  override def load(): GameModel = ???

  override def save(gameModel: GameModel): Boolean = ???
}
