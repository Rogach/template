package fulmar

import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.{ServletContextHandler, FilterHolder, FilterMapping, DefaultServlet, ServletHolder}
import org.eclipse.jetty.util.resource.Resource
import org.eclipse.jetty.webapp.WebAppContext

object Main extends App with Logger {

  val server = new Server(17437)
  
  val src = new java.io.File("src")
  if (src.exists && src.isDirectory) {
    log("Running in dev mode")
    server.setHandler(new WebAppContext("src/main/resources", "/"))
  } else {
    // setting lift's production mode
    System.setProperty("run.mode", "production")
    server.setHandler(new WebAppContext(getClass.getClassLoader.getResource("index.html").toExternalForm.stripSuffix("index.html"), "/"))
  }
  
  server.start()
  
  log("server init finished")
}
