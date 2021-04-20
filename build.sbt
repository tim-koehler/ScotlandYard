import sbt.Keys.libraryDependencies

name          := "scotland-yard"
organization  := "de.htwg.se"
version       := "0.1.0"
scalaVersion  := "2.13.0"

val AkkaVersion = "2.6.8"
val AkkaHttpVersion = "10.2.4"
val commonDependencies = Seq(
  "org.scalatest" %% "scalatest" % "3.0.8",
  "org.scalatest" %% "scalatest" % "3.0.8" % "test",
  "org.scala-lang.modules" %% "scala-swing" % "2.1.1",
  "net.codingwell" %% "scala-guice" % "4.2.6",
  "org.scala-lang.modules" %% "scala-xml" % "1.2.0",
  "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
  "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
  "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-spray-json" % AkkaHttpVersion
)

parallelExecution in Test := false
coverageExcludedPackages := "<empty>;.*aview.*;.*ScotlandYard;.*controllerMockImpl.*;.*fileIOMockImpl.*;.*gameInitializerMockImpl.*"
coverageEnabled.in(Test, test) := true

ThisBuild / trackInternalDependencies := TrackLevel.TrackIfMissing

lazy val model = (project in file("Model"))
lazy val gameInitializer = (project in file("GameInitializer"))
lazy val fileIO = (project in file("FileIO"))
lazy val scotlandYardBase = (project in file(".")).dependsOn(model, gameInitializer, fileIO).aggregate(model, gameInitializer, fileIO).settings(
  name := "ScotlandYard",
  libraryDependencies ++= commonDependencies,
  assemblyMergeStrategy in assembly := {
    case PathList("reference.conf") => MergeStrategy.concat
    case PathList("META-INF", xs @ _*) => MergeStrategy.discard
    case x => MergeStrategy.first
  },
  assemblyJarName in assembly := "ScotlandYard.jar",
  mainClass in assembly := Some("de.htwg.se.scotlandyard.ScotlandYard")
)