import sbt.Keys.libraryDependencies

name          := "scotland-yard"
organization  := "de.htwg.se"
version       := "0.1.0"
scalaVersion  := "2.13.0"

val commonDependencies = Seq(
  "org.scalatest" %% "scalatest" % "3.0.8",
  "org.scalatest" %% "scalatest" % "3.0.8" % "test",
  "org.scala-lang.modules" %% "scala-swing" % "2.1.1",
  "net.codingwell" %% "scala-guice" % "4.2.6",
  "com.typesafe.play" %% "play-json" % "2.8.1",
  "org.scala-lang.modules" %% "scala-xml" % "1.2.0"
)

parallelExecution in Test := false
coverageExcludedPackages := "<empty>;.*ScotlandYard;.*controllerMockImpl.*;.*gameInitializerMockImpl.*"
coverageEnabled.in(Test, test) := true

ThisBuild / trackInternalDependencies := TrackLevel.TrackIfMissing

lazy val model = (project in file("Model"))
lazy val tui = (project in file("Tui")).dependsOn(scotlandYardBase).aggregate(scotlandYardBase)
lazy val scotlandYardBase = (project in file(".")).dependsOn(model).aggregate(model).settings(
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