name          := "scotland-yard"
organization  := "de.htwg.se"
version       := "0.0.1"
scalaVersion  := "2.13.0"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.8"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.8" % "test"
libraryDependencies += "org.scalafx" %% "scalafx" % "12.0.2-R18"
coverageEnabled.in(Test, test) := true
