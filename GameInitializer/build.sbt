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

lazy val model = ProjectRef(uri("https://github.com/tim-koehler/ScotlandYard.git"), "modelProject")
lazy val gameInitializer = (project in file(".")).dependsOn(model).aggregate(model).settings(
  name          := "scotland-yard-gameinitializer",
  organization  := "de.htwg.se",
  version       := "0.1.0",
  scalaVersion  := "2.13.0",
  libraryDependencies ++= commonDependencies,
  assemblyMergeStrategy in assembly := {
    case PathList("reference.conf") => MergeStrategy.concat
    case PathList("META-INF", xs @ _*) => MergeStrategy.discard
    case x => MergeStrategy.first
  },
  assemblyJarName in assembly := "ScotlandYard-GameInitializer.jar",
  mainClass in assembly := Some("de.htwg.se.scotlandyard.gameinitializer.Rest")
)