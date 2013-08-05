import sbt._
import Keys._
import sbtassembly.Plugin._
import sbtassembly.Plugin.AssemblyKeys._
import spray.revolver.RevolverPlugin._
import sbtbuildinfo.Plugin._

object build extends Build {

  lazy val root = Project("default", file("."))
    .settings(assemblySettings:_*)
    .settings(Revolver.settings:_*)
    .settings(buildInfoSettings:_*)
    .settings (
      scalaVersion := "2.10.2",
      scalacOptions ++= Seq(
        "-deprecation",
        "-unchecked",
        "-feature",
        "-language:postfixOps",
        "-language:reflectiveCalls",
        "-language:implicitConversions",
        "-Xlint"
      ),

      libraryDependencies := Seq(
        "org.rogach" %% "scallop" % "0.9.3",
        "org.scalatest" %% "scalatest" % "1.9.1" % "test"
      ),

      Revolver.reColors := Seq("blue", "green", "magenta", "cyan"),
      mainClass in Revolver.reStart := Some("rosefinch.Main"),
      mainClass in assembly := Some("org.default.Main"),
      sourceGenerators in Compile <+= buildInfo,
      buildInfoPackage := "org.default",
      buildInfoKeys ++= Seq[BuildInfoKey](
        name,
        version,
        scalaVersion,
        sbtVersion,
        buildInfoBuildNumber,
        BuildInfoKey.action("buildTime") { System.currentTimeMillis }
      ),

      name := "default",
      version := "0.0.1",
      organization := "org.default"
    )
}
