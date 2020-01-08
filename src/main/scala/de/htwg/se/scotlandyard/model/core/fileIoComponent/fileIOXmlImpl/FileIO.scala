package de.htwg.se.scotlandyard.model.core.fileIoComponent.fileIOXmlImpl

import de.htwg.se.scotlandyard.model.core.GameMaster
import de.htwg.se.scotlandyard.model.core.fileIoComponent.{DetectiveSmall, FileIOInterface, GameStats, MrXSmall}
import de.htwg.se.scotlandyard.model.playersComponent.playersBaseImpl.MrX

class FileIO extends FileIOInterface {
  override def load(): Unit = {

  }

  override def save(): Unit = {
    val players = GameMaster.players
    val mrx = GameMaster.players(0).asInstanceOf[MrX]

    var detectives = List[DetectiveSmall]()
    for(i <- 1 to players.length - 1) {
      detectives = detectives ::: List(DetectiveSmall(players(i).name, players(i).station.number, players(i).taxiTickets, players(i).busTickets, players(i).undergroundTickets))
    }

    val mrxSmall = MrXSmall(mrx.name, mrx.station.number, mrx.isVisible, mrx.lastSeen, mrx.blackTickets, mrx.doubleTurn, mrx.taxiTickets, mrx.busTickets, mrx.undergroundTickets)
    val gs = GameStats(GameMaster.round, GameMaster.totalRound, GameMaster.players.length, mrxSmall, detectives)

    def toXml = {
      <stock>
        <symbol>{symbol}</symbol>
        <businessName>{businessName}</businessName>
        <price>{price}</price>
      </stock>
    }

    scala.xml.XML.save("ScotlandYard.xml", toXml)
  }

}
