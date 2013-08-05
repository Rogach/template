#if pluginRevolver
addSbtPlugin("io.spray" % "sbt-revolver" % "0.7.1")
#fi

#if pluginAssembly
addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.9.1")
#fi

#if pluginBuildInfo
addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.2.5")
#fi
