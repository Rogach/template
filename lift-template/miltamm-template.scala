val projectName = string("New project name:", "default")
val projectOrganization = string("Organization:", "org.default")
val rootPackage = string("Root package for the project:", "org.default")

val configurePlugins = bool("Do you want to configure sbt plugins?", false)
val pluginBuildInfo = bool("Do you need sbt-buildinfo plugin included?", true).when(configurePlugins, or = true)
val pluginAssembly = bool("Do you need sbt-assembly plugin included?", true).when(configurePlugins, or = true)
val pluginRevolver = bool("Do you need sbt-revolver plugin included?", true).when(configurePlugins, or = true)

val configureJsLibs = bool("Do you want to configure JavaScript libraries?", false)
val jsUnderscore = bool("Do you need Underscore.js lib included?", true).when(configureJsLibs, or = true)
val jsDojo = bool("Do you need Dojo framework included?", true).when(configureJsLibs, or = true)

val includeConfigrity = bool("Do you want to include configuration library (Configrity)?", true)
val includeScallop = bool("Are you going to use command-line options?", true)
val httpPort = string("Port for http listener:", "8088")
val https = bool("Will your project use https?", true)
val httpsPort = string("Port for https listener:", "8089").when(https, or = "")

override def routes = Seq(
  "".pp append Seq(
    iff(includeScallop)("src/main/scala/Options.scala".pp),
    iff(includeConfigrity)("server.conf".pp),
    iff(https)("keystore"),
    iff(jsUnderscore)("src/main/resources/jsl/underscore-1.4.3.min.js"),
    "src/main/resources/jsl/jquery-1.8.3.min.js",
    "src/main/resources/bootstrap" // these don't need preprocessing
  )
)
