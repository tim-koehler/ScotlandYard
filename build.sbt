name          := "scotland-yard"
organization  := "de.htwg.se"
version       := "0.0.1"
scalaVersion  := "2.13.0"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.8"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.8" % "test"
libraryDependencies += "org.scala-lang.modules" %% "scala-swing" % "2.1.1"
libraryDependencies += "com.typesafe.play" %% "play-json" % "2.8.1"
libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "1.2.0"
coverageEnabled.in(Test, test) := true
