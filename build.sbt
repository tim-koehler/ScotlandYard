name          := "scotland-yard"
organization  := "de.htwg.se"
version       := "0.1.0"
scalaVersion  := "2.13.0"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.8"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.8" % "test"
libraryDependencies += "org.scala-lang.modules" %% "scala-swing" % "2.1.1"
libraryDependencies += "com.typesafe.play" %% "play-guice" % "2.8.7"
libraryDependencies += "com.typesafe.play" %% "play-json" % "2.8.1"
libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "1.2.0"
parallelExecution in Test := false
coverageExcludedPackages := "<empty>;.*Gui.*;.*ScotlandYard"
coverageEnabled.in(Test, test) := true
