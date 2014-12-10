#if pluginRevolver
addSbtPlugin("io.spray" % "sbt-revolver" % "0.7.2")
#fi

#if pluginAssembly
addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.12.0")
#fi

#if pluginBuildInfo
addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.3.2")
#fi
