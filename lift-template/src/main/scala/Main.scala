package #{rootPackage}

import org.eclipse.jetty.server.Server
#if https
import org.eclipse.jetty.server.ssl.SslSocketConnector
import org.eclipse.jetty.util.ssl.SslContextFactory
#fi
import org.eclipse.jetty.webapp.WebAppContext
#if includeConfigrity 
import org.streum.configrity._
import util.control.Exception._
#fi

object Main extends App {
  #if includeScallop
  val opts = new Options(args)
  #fi
  #if includeConfigrity
  val defaults = new Configuration(Map[String, String](
    #if https
    "https.use" -> "true",
    "https.only" -> "false",
    "https.port" -> "#{httpsPort}",
    "https.keystore.file" -> "keystore",
    "https.keystore.pass" -> "qwerty",
    #fi
    "http.port" -> "#{httpPort}"
  ))
  #fi
  #if includeScallop && includeConfigrity
  val config = defaults ++ allCatch.opt(Configuration.load("server.conf")).getOrElse(Configuration()) ++ Configuration(opts.properties)
  #elif includeConfigrity
  val config = defaults ++ allCatch.opt(Configuration.load("server.conf")).getOrElse(Configuration())
  #fi

  #if includeConfigrity
  val server = new Server(config[Int]("http.port"))
  #else
  val server = new Server(#{httpPort})
  #fi
  
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
  
  #if https
  #if includeConfigrity
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
  #else
  // HTTPS
  // for the following to work, 'keystore' needs to be in the directory you start the app from
  // the keystore can be generated using the following commands:
  //   keytool -keystore keystore -alias jetty -genkey -keyalg RSA
  //   password: gwerty
  //   then accept all the defaults
  val sslContextFactory = new SslContextFactory("keystore");
  sslContextFactory.setKeyStorePassword("qwerty");
  val connector = new SslSocketConnector(sslContextFactory);
  connector.setPort(#{httpsPort});
  server.addConnector(connector)
  #fi
  #fi
  
  server.start()
}
