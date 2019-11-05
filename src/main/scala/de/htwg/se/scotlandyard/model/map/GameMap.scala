package de.htwg.se.scotlandyard.model.map

object GameMap {
  val stations = List()
  var playerPositions = List()

  override def toString: String = {
    var map: String = ""
    for(i <- 0 to 5) {
      map = map + "MAP MAP MAP MAP MAP MAP MAP MAP MAP MAP MAP MAP MAP MAP MAP MAP MAP MAP MAP\n"
    }
    map
  }
}
