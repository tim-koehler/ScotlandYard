case class Cell(x:Int, y:Int)

val cell1 = Cell(4,5)
cell1.x
cell1.y

case class Field(cells: Array[Cell])

val field1 = Field(Array.ofDim[Cell](1))
field1.cells(0)=cell1
field1.cells(0).x
field1.cells(0).y

val menuEntries: List[String] = List("Start Game", "Settings", "End Game")

val firstString = "uff"
val secondString = "lol"

var myList = List("Uff")

val otherList = "moin" :: myList

val anotherString2 = String.valueOf(firstString)

val thirdString = firstString + secondString
thirdString

