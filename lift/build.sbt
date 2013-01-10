import AssemblyKeys._

assemblySettings

organization := "org.rogach"

version := "0.0.1"

name := "default web project"

scalaVersion := "2.9.2"

scalacOptions ++= Seq("-deprecation", "-unchecked", "-Xlint")

libraryDependencies ++= Seq(
  "org.eclipse.jetty" % "jetty-server" % "7.6.0.RC5",
  "org.eclipse.jetty" % "jetty-webapp" % "7.6.0.RC5",
  "javax.servlet" % "servlet-api" % "2.5" % "provided",
  "net.liftweb" % "lift-webkit_2.9.1" % "2.4"
)

seq(Revolver.settings: _*)

