#if pluginAssembly
import AssemblyKeys._
#fi

organization := "#{projectOrganization}"

name := "#{projectName}"

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
  #if includeConfigrity
  "org.streum" %% "configrity-core" % "1.0.0",
  #fi
  #if includeScallop
  "org.rogach" %% "scallop" % "0.8.0",
  #fi
  #if useDatabase
  "commons-dbcp" % "commons-dbcp" % "1.4",
  #if db == "mysql"
  "mysql" % "mysql-connector-java" % "5.1.21",
  #elif db == "hsql"
  "org.hsqldb" % "hsqldb" % "2.2.9",
  #fi
  #if useSlick
  "com.typesafe.slick" %% "slick" % "1.0.0",
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  #fi
  #if useFlyway
  "com.googlecode.flyway" % "flyway-core" % "2.0.3",
  #fi
  #fi
  "org.eclipse.jetty" % "jetty-server" % "8.1.9.v20130131",
  "org.eclipse.jetty" % "jetty-webapp" % "8.1.9.v20130131",
  "net.liftweb" %% "lift-webkit" % "2.5-M4"
)

#if pluginAssembly
assemblySettings

mainClass in assembly := Some("#{rootPackage}.Main")
#fi

#if pluginRevolver
seq(Revolver.settings: _*)

mainClass in Revolver.reStart := Some("#{rootPackage}.Main")
#fi

#if pluginBuildInfo
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

buildInfoPackage := "#{rootPackage}"
#fi
