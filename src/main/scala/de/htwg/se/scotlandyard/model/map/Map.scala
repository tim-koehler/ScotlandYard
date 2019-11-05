package de.htwg.se.scotlandyard.model.map

object Map {
  val stations = Set[Station]()
  var playerPositions = List()

  override def toString: String = {
    var map: String = ""
    // just a place holder for the real Map
    for(i <- 0 to 8) {
      map = map + "MAP MAP MAP MAP MAP MAP MAP MAP MAP MAP MAP MAP MAP MAP MAP MAP MAP MAP MAP MAP MAP MAP MAP MAP MAP MAP MAP MAP MAP\n"
    }
    map
  }
}
