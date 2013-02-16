package #{rootPackage}

import org.rogach.scallop._

class Options(args: Seq[String]) extends ScallopConf(args) {
  #if pluginBuildInfo
  version("#{projectName}, %s b%s (%3$td.%3$tm.%3$tY %3$tH:%3$tM). Built with Scala %4$s" format (
    BuildInfo.version,
    BuildInfo.buildinfoBuildnumber,
    new java.util.Date(BuildInfo.buildTime),
    BuildInfo.scalaVersion))
  #else
  version("#{projectName}, v0.0.1")
  #fi

  val properties = props[String]('C')

}
