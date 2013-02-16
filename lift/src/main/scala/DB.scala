package org.default

import org.apache.commons.dbcp.BasicDataSource
import javax.sql.DataSource
import Main.config
import scala.slick.driver.HsqldbDriver.simple._
import scala.slick.jdbc.StaticQuery
import Database.threadLocalSession
import com.googlecode.flyway.core.Flyway

object DB {
  val dataSource: DataSource = {
    val ds = new BasicDataSource
    ds.setDriverClassName("org.hsqldb.jdbc.JDBCDriver")
    ds.setUsername("SA")
    ds.setPassword("")
    ds.setMaxActive(20);
    ds.setMaxIdle(10);
    ds.setInitialSize(10);
    ds.setValidationQuery("SELECT 1 FROM INFORMATION_SCHEMA.SYSTEM_USERS")
    ds.setUrl("jdbc:hsqldb:file:target/db/db")
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

  val lastInsertIdFunction = SimpleFunction.nullary[Long]("IDENTITY")
  def lastInsertId = Query(lastInsertIdFunction).firstOption
  
  def shutdown = StaticQuery.updateNA("SHUTDOWN")

  /** runs the db code inside the transaction */
  def exec[A](fn: => A) =
    database withTransaction {
      fn
    }
  
}
