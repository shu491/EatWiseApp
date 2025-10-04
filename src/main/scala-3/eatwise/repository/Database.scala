package eatwise.repository

import scalikejdbc.*

// Database trait that sets up a connection pool to Derby Db
trait Database {
  val driver = "org.apache.derby.jdbc.EmbeddedDriver"
  val url = "jdbc:derby:UserDB;create=true;"
  Class.forName(driver)
  ConnectionPool.singleton(url, "me", "mine")
  implicit val session: DBSession = AutoSession
}

// Database object extends Database so it inherits the DB setup
object Database extends Database {
  // Ensure user table exists; if not create it using UserDAO
  def setupDB(): Unit = {
    if (!hasUserTable) UserDAO.initializeTable()
  }

  // Check if 'users' table exists in DB
  def hasUserTable: Boolean = DB.getTable("users").isDefined
}
