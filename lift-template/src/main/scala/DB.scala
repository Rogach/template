package #{rootPackage}

import org.apache.commons.dbcp.BasicDataSource
import javax.sql.DataSource
#if includeConfigrity
import Main.config
#fi
#if useSlick
#if db == "mysql"
import scala.slick.driver.MySQLDriver.simple._
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
    #fi
    #if includeConfigrity
    ds.setUsername(config[String]("db.user"))
    ds.setPassword(config[String]("db.pass"))
    #elif db == "mysql"
    ds.setUsername(#{mysqlUserName})
    ds.setPassword(#{mysqlUserPass})
    #fi
    ds.setMaxActive(20);
    ds.setMaxIdle(10);
    ds.setInitialSize(10);
    ds.setValidationQuery("SELECT 1")
    #if db == "mysql"
    ds.setUrl("jdbc:mysql://%s/%s?useUnicode=true&characterEncoding=UTF-8" 
    #if includeConfigrity
      format (config[String]("db.host"), config[String]("db.name")))
    #else
      format (#{mysqlLocation}, #{mysqlDbName}))
    #fi
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
  #fi
  def lastInsertId = Query(lastInsertIdFunction).firstOption

  /** runs the db code inside the transaction */
  def exec[A](fn: => A) =
    database withTransaction {
      fn
    }
  #fi
  
}
