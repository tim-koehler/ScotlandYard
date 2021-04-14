val commonDependencies = Seq(
  "org.scalatest" %% "scalatest" % "3.0.8",
  "org.scalatest" %% "scalatest" % "3.0.8" % "test",
  "org.scala-lang.modules" %% "scala-swing" % "2.1.1",
  "net.codingwell" %% "scala-guice" % "4.2.6",
  "com.typesafe.play" %% "play-json" % "2.8.1",
  "org.scala-lang.modules" %% "scala-xml" % "1.2.0"
)

lazy val model = ProjectRef(uri("https://github.com/tim-koehler/ScotlandYard.git"), "model")
lazy val gameInitializer = ProjectRef(uri("https://github.com/tim-koehler/ScotlandYard.git"), "gameInitializer")
lazy val fileIO = (project in file(".")).dependsOn(model, gameInitializer).aggregate(model, gameInitializer).settings(
  name          := "scotland-yard-fileio",
  organization  := "de.htwg.se",
  version       := "0.1.0",
  scalaVersion  := "2.13.0",
  libraryDependencies ++= commonDependencies,
)