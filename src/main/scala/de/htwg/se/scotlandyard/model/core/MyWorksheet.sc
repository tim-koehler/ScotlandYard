
var round = 4
var playersLen = 3

var division = round.toDouble / playersLen.toDouble
var totalRound = (round.toDouble / playersLen.toDouble).floor.toInt

println(division)
println(totalRound)