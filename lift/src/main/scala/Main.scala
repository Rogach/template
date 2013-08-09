package org.default

import org.eclipse.jetty.server.Server
import org.eclipse.jetty.webapp.WebAppContext
import org.streum.configrity._
import util.control.Exception._

object Main extends App {
  val opts = new Options(args)
  val defaults = new Configuration(Map[String, String](
    "http.port" -> "8088"
  ))
  val config = defaults ++ allCatch.opt(Configuration.load("server.conf")).getOrElse(Configuration()) ++ Configuration(opts.properties)

  // initialize the db
  DB

  val server = new Server(config[Int]("http.port"))

  val src = new java.io.File("src")
  if (src.exists && src.isDirectory) {
    // dev mode
    server.setHandler(new WebAppContext("src/main/resources/webapp/", "/"))
  } else {
    // runnable jar
    server.setHandler(new WebAppContext(getClass.getClassLoader.getResource("WEB-INF/web.xml").toExternalForm.stripSuffix("WEB-INF/web.xml"), "/"))
    // setting lift's production mode
    System.setProperty("run.mode", "production")
  }

  server.start()
}
