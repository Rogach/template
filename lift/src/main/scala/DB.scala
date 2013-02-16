package org.default

import org.apache.commons.dbcp.BasicDataSource
import javax.sql.DataSource
import Main.config
import scala.slick.driver.MySQLDriver.simple._
import Database.threadLocalSession
import com.googlecode.flyway.core.Flyway

object DB {
  val dataSource: DataSource = {
    val ds = new BasicDataSource
    ds.setDriverClassName("com.mysql.jdbc.Driver")
    ds.setUsername(config[String]("db.user"))
    ds.setPassword(config[String]("db.pass"))
    ds.setMaxActive(20);
    ds.setMaxIdle(10);
    ds.setInitialSize(10);
    ds.setValidationQuery("SELECT 1")
    ds.setUrl("jdbc:mysql://%s/%s?useUnicode=true&characterEncoding=UTF-8" 
      format (config[String]("db.host"), config[String]("db.name")))
    ds
  }
  
  // test the data source validity
  dataSource.getConnection().close()

  // perform the migrations
  val flyway = new Flyway
  flyway.setLocations("db_migrations")
  flyway.setDataSource(dataSource)
  flyway.migrate

  val database = Database.forDataSource(dataSource)

  val lastInsertIdFunction = SimpleFunction.nullary[Long]("LAST_INSERT_ID")
  def lastInsertId = Query(lastInsertIdFunction).firstOption

  /** runs the db code inside the transaction */
  def exec[A](fn: => A) =
    database withTransaction {
      fn
    }
  
}
