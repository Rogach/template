import sbt._
import Keys._

object build extends Build {

  lazy val root = Project("#{projectName}", file(".")) settings (Deploy.deploySettings:_*)
}
