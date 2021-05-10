package de.htwg.se.scotlandyard.persistence

import de.htwg.se.scotlandyard.model.GameModel

trait PersistenceInterface {
  def load(): GameModel

  def save(gameModel: GameModel): Boolean

  def update(gameModel: GameModel): Boolean

  def delete(): Boolean
}
