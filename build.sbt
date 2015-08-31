import scalariform.formatter.preferences._

name          := "minimal-scala-lib-seed"
organization  := "com.github.yeghishe"
version       := "0.0.1"
scalaVersion  := "2.11.7"
scalacOptions := Seq("-unchecked", "-feature", "-deprecation", "-encoding", "utf8")

libraryDependencies ++= {
  val scalazV          = "7.2.0-M2"
  val scalaTestV       = "3.0.0-M7"
  val scalaMockV       = "3.2.2"
  val scalazScalaTestV = "0.2.3"
  Seq(
    "org.scalaz"    %% "scalaz-core"                 % scalazV,
    "org.scalatest" %% "scalatest"                   % scalaTestV       % "it,test",
    "org.scalamock" %% "scalamock-scalatest-support" % scalaMockV       % "it,test",
    "org.scalaz"    %% "scalaz-scalacheck-binding"   % scalazV          % "it,test",
    "org.typelevel" %% "scalaz-scalatest"            % scalazScalaTestV % "it,test"
  )
}

lazy val root = project.in(file(".")).configs(IntegrationTest)
Defaults.itSettings
scalariformSettings

ScalariformKeys.preferences := ScalariformKeys.preferences.value
  .setPreference(AlignSingleLineCaseStatements, true)
  .setPreference(AlignSingleLineCaseStatements.MaxArrowIndent, 100)
  .setPreference(DoubleIndentClassDeclaration, true)

initialCommands := """|import scalaz._
                      |import Scalaz._
                      |import scala.concurrent._
                      |import scala.concurrent.duration._""".stripMargin
