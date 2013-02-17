import AssemblyKeys._

organization := "org.default"

name := "default"

version := "0.0.1"

scalaVersion := "2.10.0"

scalacOptions ++= Seq(
  "-deprecation",
  "-unchecked", 
  "-feature", 
  "-language:postfixOps",
  "-language:reflectiveCalls",
  "-language:implicitConversions",
  "-Xlint")

libraryDependencies ++= Seq(
  "org.streum" %% "configrity-core" % "1.0.0",
  "org.rogach" %% "scallop" % "0.8.0",
  "commons-dbcp" % "commons-dbcp" % "1.4",
  "org.hsqldb" % "hsqldb" % "2.2.9",
  "com.typesafe.slick" %% "slick" % "1.0.0",
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "com.googlecode.flyway" % "flyway-core" % "2.0.3",
  "org.eclipse.jetty" % "jetty-server" % "8.1.9.v20130131",
  "org.eclipse.jetty" % "jetty-webapp" % "8.1.9.v20130131",
  "net.liftweb" %% "lift-webkit" % "2.5-M4"
)

assemblySettings

mainClass in assembly := Some("org.default.Main")

mergeStrategy in assembly <<= (mergeStrategy in assembly) { old =>
  {
    case "index.html" => MergeStrategy.first
    case "about.html" => MergeStrategy.discard
    case x => old(x)
  }
}

seq(Revolver.settings: _*)

mainClass in Revolver.reStart := Some("org.default.Main")

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
