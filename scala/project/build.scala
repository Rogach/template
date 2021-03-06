import sbt._
import Keys._
import sbtassembly.AssemblyKeys._
import spray.revolver.RevolverPlugin._
import sbtbuildinfo.Plugin._

object build extends Build {

  lazy val root = Project("default", file("."))
    .settings(Revolver.settings:_*)
    .settings(buildInfoSettings:_*)
    .settings (
      scalaVersion := "2.11.4",
      scalacOptions ++= Seq(
        "-deprecation",
        "-unchecked",
        "-feature",
        "-language:postfixOps",
        "-language:reflectiveCalls",
        "-language:implicitConversions",
        "-Xlint"
      ),

      libraryDependencies ++= Seq(
        "org.rogach" %% "scallop" % "0.9.5",
        "org.scalatest" %% "scalatest" % "2.2.2" % "test",
        "org.scalactic" %% "scalactic" % "2.2.2"
      ),

      Revolver.reColors := Seq("blue", "green", "magenta", "cyan"),
      mainClass in Revolver.reStart := Some("org.default.Main"),
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
