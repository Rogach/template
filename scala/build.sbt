import AssemblyKeys._

organization := "org.rogach"

name := "default project"

version := "0.0.1"

scalaVersion := "2.10.0"

scalacOptions ++= Seq("-deprecation", "-unchecked", "-Xlint")

seq(Revolver.settings: _*)

seq(assemblySettings: _*)

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "1.9.1" % "test"
)

//mainClass in assembly := Some("main.class")
