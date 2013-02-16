package #{rootPackage}

import org.apache.commons.dbcp.BasicDataSource
import javax.sql.DataSource
#if includeConfigrity
import Main.config
#fi
#if useSlick
#if db == "mysql"
import scala.slick.driver.MySQLDriver.simple._
#elif db == "hsql"
import scala.slick.driver.HsqldbDriver.simple._
import scala.slick.jdbc.StaticQuery
#fi
import Database.threadLocalSession
#fi
#if useFlyway
import com.googlecode.flyway.core.Flyway
#fi

object DB {
  val dataSource: DataSource = {
    val ds = new BasicDataSource
    #if db == "mysql"
    ds.setDriverClassName("com.mysql.jdbc.Driver")
    #elif db == "hsql"
    ds.setDriverClassName("org.hsqldb.jdbc.JDBCDriver")
    #fi
    #if db == "mysql"
    #if includeConfigrity
    ds.setUsername(#{mysqlUserName})
    ds.setPassword(#{mysqlUserPass})
    #else
    ds.setUsername(config[String]("db.user"))
    ds.setPassword(config[String]("db.pass"))
    #fi
    #elif db == "hsql"
    ds.setUsername("SA")
    ds.setPassword("")
    #fi
    ds.setMaxActive(20);
    ds.setMaxIdle(10);
    ds.setInitialSize(10);
    #if db == "mysql"
    ds.setValidationQuery("SELECT 1")
    #elif db == "hsql"
    ds.setValidationQuery("SELECT 1 FROM INFORMATION_SCHEMA.SYSTEM_USERS")
    #fi
    #if db == "mysql"
    ds.setUrl("jdbc:mysql://%s/%s?useUnicode=true&characterEncoding=UTF-8" 
    #if includeConfigrity
      format (config[String]("db.host"), config[String]("db.name")))
    #else
      format (#{mysqlLocation}, #{mysqlDbName}))
    #fi
    #elif db == "hsql"
    ds.setUrl("jdbc:hsqldb:file:target/db/db")
    #fi
    ds
  }
  
  // test the data source validity
  dataSource.getConnection().close()

  #if useFlyway
  // perform the migrations
  val flyway = new Flyway
  flyway.setLocations("db_migrations")
  flyway.setDataSource(dataSource)
  flyway.migrate
  #fi

  #if useSlick
  val database = Database.forDataSource(dataSource)

  #if db == "mysql"
  val lastInsertIdFunction = SimpleFunction.nullary[Long]("LAST_INSERT_ID")
  #elif db == "hsql"
  val lastInsertIdFunction = SimpleFunction.nullary[Long]("IDENTITY")
  #fi
  def lastInsertId = Query(lastInsertIdFunction).firstOption
  
  #if db == "hsql"
  def shutdown = StaticQuery.updateNA("SHUTDOWN")
  #fi

  /** runs the db code inside the transaction */
  def exec[A](fn: => A) =
    database withTransaction {
      fn
    }
  #fi
  
}
