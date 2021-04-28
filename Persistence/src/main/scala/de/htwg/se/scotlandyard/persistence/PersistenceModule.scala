package de.htwg.se.scotlandyard.persistence

import com.google.inject.AbstractModule
import net.codingwell.scalaguice.ScalaModule

class PersistenceModule extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    bind[PersistenceInterface].to[fileio.fileIOJsonImpl.FileIO]
  }
}
