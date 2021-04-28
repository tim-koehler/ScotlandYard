val AkkaVersion = "2.6.8"
val AkkaHttpVersion = "10.2.4"

val reslver = Seq(
  ("Typesafe Releases" at "http://repo.typesafe.com/typesafe/maven-releases/").withAllowInsecureProtocol(true)
)

val commonDependencies = Seq(
  "org.scalatest" %% "scalatest" % "3.0.8",
  "org.scalatest" %% "scalatest" % "3.0.8" % "test",
  "org.scala-lang.modules" %% "scala-swing" % "2.1.1",
  "net.codingwell" %% "scala-guice" % "4.2.6",
  "org.scala-lang.modules" %% "scala-xml" % "1.2.0",
  "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
  "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
  "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-spray-json" % AkkaHttpVersion,
  "com.typesafe.slick" %% "slick" % "3.3.3",
  "org.postgresql" % "postgresql" % "42.2.14"
)

lazy val model = ProjectRef(uri("https://github.com/tim-koehler/ScotlandYard.git"), "modelProject")
lazy val fileIO = (project in file(".")).dependsOn(model).aggregate(model).settings(
  name          := "scotland-yard-persistence",
  organization  := "de.htwg.se",
  version       := "0.1.0",
  scalaVersion  := "2.13.0",
  resolvers     ++= reslver,
  libraryDependencies ++= commonDependencies,
)