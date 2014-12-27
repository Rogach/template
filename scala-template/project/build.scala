import sbt._
import Keys._
#if pluginAssembly
import sbtassembly.AssemblyKeys._
#fi
#if pluginRevolver
import spray.revolver.RevolverPlugin._
#fi
#if pluginBuildInfo
import sbtbuildinfo.Plugin._
#fi

object build extends Build {

  lazy val root = Project("#{projectName}", file("."))
    #if pluginRevolver
    .settings(Revolver.settings:_*)
    #fi
    #if pluginBuildInfo
    .settings(buildInfoSettings:_*)
    #fi
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
        #sep,
        #if includeScallop
        "org.rogach" %% "scallop" % "0.9.5"
        #fi
        #if includeScalatest
        "org.scalatest" %% "scalatest" % "2.2.2" % "test"
        #fi
        #if includeScalactic
        "org.scalactic" %% "scalactic" % "2.2.2"
        #fi
        #endsep
      ),

      #if pluginRevolver
      Revolver.reColors := Seq("blue", "green", "magenta", "cyan"),
      #fi
      #if pluginRevolver && createMainClass
      mainClass in Revolver.reStart := Some("#{rootPackage}.Main"),
      #fi
      #if pluginAssembly && createMainClass
      mainClass in assembly := Some("#{rootPackage}.Main"),
      #fi
      #if pluginBuildInfo
      sourceGenerators in Compile <+= buildInfo,
      buildInfoPackage := "#{rootPackage}",
      buildInfoKeys ++= Seq[BuildInfoKey](
        name,
        version,
        scalaVersion,
        sbtVersion,
        buildInfoBuildNumber,
        BuildInfoKey.action("buildTime") { System.currentTimeMillis }
      ),
      #fi

      name := "#{projectName}",
      version := "0.0.1",
      organization := "#{projectOrganization}"
    )
}
