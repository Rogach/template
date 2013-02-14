#if pluginRevolver
addSbtPlugin("io.spray" % "sbt-revolver" % "0.6.2")
#fi

#if pluginAssembly
addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.8.6")
#fi

#if pluginBuildInfo
addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.2.2")
#fi