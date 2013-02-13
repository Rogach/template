val projectName = string("New project name:", "default")
val projectOrganization = string("Organization:", "org.default")
val rootPackage = string("Root package for the project:", "org.default")

val configurePlugins = bool("Do you want to configure sbt plugins?", false)
val pluginBuildInfo = bool("Do you need sbt-buildinfo plugin included?", true).when(configurePlugins, or = true)
val pluginAssembly = bool("Do you need sbt-assembly plugin included?", true).when(configurePlugins, or = true)
val pluginRevolver = bool("Do you need sbt-revolver plugin included?", true).when(configurePlugins, or = true)

val includeScalatest = bool("Do you want ScalaTest included?", true)
val createMainClass = bool("Do you want to create stub Main file?", true)

override def routes = Seq(
  "".pp append Seq(
    iff(createMainClass)("src/main/scala/Main.scala".pp)
  )
)
