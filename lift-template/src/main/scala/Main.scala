package #{rootPackage}

import org.eclipse.jetty.server.Server
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
    #if useDatabase
    #if db == "mysql"
    "db.host" -> "#{mysqlLocation}",
    "db.name" -> "#{mysqlDbName}",
    "db.user" -> "#{mysqlUserName}",
    "db.pass" -> "#{mysqlUserPass}",
    #fi
    #fi
    "http.port" -> "#{httpPort}"
  ))
  #fi
  #if includeScallop && includeConfigrity
  val config = defaults ++ allCatch.opt(Configuration.load("server.conf")).getOrElse(Configuration()) ++ Configuration(opts.properties)
  #elif includeConfigrity
  val config = defaults ++ allCatch.opt(Configuration.load("server.conf")).getOrElse(Configuration())
  #fi

  #if useDatabase
  // initialize the db
  DB
  #fi

  #if includeConfigrity
  val server = new Server(config[Int]("http.port"))
  #else
  val server = new Server(#{httpPort})
  #fi

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
