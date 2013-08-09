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
  "-language:implicitConversions"
)

libraryDependencies ++= Seq(
  "org.streum" %% "configrity-core" % "1.0.0",
  "org.rogach" %% "scallop" % "0.9.3",
  "commons-dbcp" % "commons-dbcp" % "1.4",
  "org.hsqldb" % "hsqldb" % "2.3.0",
  "com.typesafe.slick" %% "slick" % "1.0.1",
  "com.googlecode.flyway" % "flyway-core" % "2.2",
  "ch.qos.logback" % "logback-classic" % "1.0.13",
  "org.eintr.loglady" %% "loglady" % "1.1.0",
  "org.eclipse.jetty" % "jetty-server" % "9.0.4.v20130625",
  "org.eclipse.jetty" % "jetty-webapp" % "9.0.4.v20130625",
  "net.liftweb" %% "lift-webkit" % "2.5"
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
