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
  "-language:implicitConversions",
  "-Xlint")

#if pluginRevolver
seq(Revolver.settings: _*)
#fi

#if pluginRevolver && createMainClass
mainClass in Revolver.reStart := Some("#{rootPackage}.Main")
#fi

#if pluginAssembly
assemblySettings
#fi

libraryDependencies ++= Seq(
  #sep,
  #if includeScallop
  "org.rogach" %% "scallop" % "0.8.0"
  #fi
  #if includeScalatest
  "org.scalatest" %% "scalatest" % "1.9.1" % "test"
  #fi
  #endsep
)


#if pluginAssembly && createMainClass
mainClass in assembly := Some("#{rootPackage}.Main")
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
