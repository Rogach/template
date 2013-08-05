import AssemblyKeys._

organization := "org.default"

name := "default"

version := "0.0.1"

scalaVersion := "2.10.2"

scalacOptions ++= Seq(
  "-deprecation",
  "-unchecked",
  "-feature",
  "-language:postfixOps",
  "-language:reflectiveCalls",
  "-language:implicitConversions",
  "-Xlint")

seq(Revolver.settings: _*)

mainClass in Revolver.reStart := Some("org.default.Main")

assemblySettings

libraryDependencies ++= Seq(
  "org.rogach" %% "scallop" % "0.8.0",
  "org.scalatest" %% "scalatest" % "1.9.1" % "test"
)


mainClass in assembly := Some("org.default.Main")

buildInfoSettings

sourceGenerators in Compile <+= buildInfo

buildInfoKeys ++= Seq[BuildInfoKey](
  name,
  version,
  scalaVersion,
  sbtVersion,
  buildInfoBuildNumber,
  BuildInfoKey.action("buildTime") { System.currentTimeMillis }
)

buildInfoPackage := "org.default"
