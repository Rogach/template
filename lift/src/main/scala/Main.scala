package org.default

import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.ssl.SslSocketConnector
import org.eclipse.jetty.util.ssl.SslContextFactory
import org.eclipse.jetty.webapp.WebAppContext
import org.streum.configrity._
import util.control.Exception._

object Main extends App {
  val opts = new Options(args)
  val defaults = new Configuration(Map[String, String](
    "https.use" -> "true",
    "https.only" -> "false",
    "https.port" -> "8089",
    "https.keystore.file" -> "keystore",
    "https.keystore.pass" -> "qwerty",
    "http.port" -> "8088"
  ))
  val config = defaults ++ allCatch.opt(Configuration.load("server.conf")).getOrElse(Configuration()) ++ Configuration(opts.properties)
  
  // initialize the db
  DB

  val server = new Server(config[Int]("http.port"))
  
  val src = new java.io.File("src")
  if (src.exists && src.isDirectory) {
    // dev mode
    server.setHandler(new WebAppContext("src/main/resources", "/"))
  } else {
    // runnable jar
    server.setHandler(new WebAppContext(getClass.getClassLoader.getResource("WEB-INF/web.xml").toExternalForm.stripSuffix("WEB-INF/web.xml"), "/"))
    // setting lift's production mode
    System.setProperty("run.mode", "production")
  }
  
  if (!config[Boolean]("https.use")) {
    // do not add https connector
  } else {
    // HTTPS
    // for the following to work, 'keystore' needs to be in the directory you start the app from
    // the keystore can be generated using the following commands:
    //   keytool -keystore keystore -alias jetty -genkey -keyalg RSA
    //   password: gwerty
    //   then accept all the defaults
    val sslContextFactory = new SslContextFactory(config[String]("https.keystore.file"));
    sslContextFactory.setKeyStorePassword(config[String]("https.keystore.pass"));
    val connector = new SslSocketConnector(sslContextFactory);
    connector.setPort(config[Int]("https.port"));
    if (config[Boolean]("https.only")) {
      server.setConnectors(Array(connector))
    } else {
      server.addConnector(connector)
    }
  }
  
  server.start()
}
