#if pluginAssembly
import AssemblyKeys._
#fi

organization := "#{projectOrganization}"

name := "#{projectName}"

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
  #if includeConfigrity
  "org.streum" %% "configrity-core" % "1.0.0",
  #fi
  #if includeScallop
  "org.rogach" %% "scallop" % "0.9.3",
  #fi
  #if useDatabase
  "commons-dbcp" % "commons-dbcp" % "1.4",
  #if db == "mysql"
  "mysql" % "mysql-connector-java" % "5.1.26",
  #elif db == "hsql"
  "org.hsqldb" % "hsqldb" % "2.3.0",
  #fi
  #if useSlick
  "com.typesafe.slick" %% "slick" % "1.0.1",
  #fi
  #if useFlyway
  "com.googlecode.flyway" % "flyway-core" % "2.2",
  #fi
  #fi
  #if useLogback
  "ch.qos.logback" % "logback-classic" % "1.0.13",
  "org.eintr.loglady" %% "loglady" % "1.1.0",
  #fi
  "org.eclipse.jetty" % "jetty-server" % "9.0.4.v20130625",
  "org.eclipse.jetty" % "jetty-webapp" % "9.0.4.v20130625",
  "net.liftweb" %% "lift-webkit" % "2.5"
)

#if pluginAssembly
assemblySettings

mainClass in assembly := Some("#{rootPackage}.Main")

mergeStrategy in assembly <<= (mergeStrategy in assembly) { old =>
  {
    case "index.html" => MergeStrategy.first
    case "about.html" => MergeStrategy.discard
    case x => old(x)
  }
}
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
