package eatwise.repository

import eatwise.repository.Database
import scalikejdbc.*

import scala.util.{Failure, Success, Try}

// Represent a user entity with username and password
case class User(username: String, password: String)

// Data Access Object (DAO) for User
// Handles all user operation
object UserDAO extends Database {

  // Initializes the 'users' table with username and password columns
  def initializeTable(): Unit = DB.autoCommit { implicit session =>
    sql"""
      CREATE TABLE users (
        id INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
        username VARCHAR(64) PRIMARY KEY,
        password VARCHAR(64)
      )
    """.execute.apply()
  }

  // Inserts a new user into the database
  def insertUser(user: User): Try[Int] = Try {
    sql"INSERT INTO users (username, password) VALUES (${user.username}, ${user.password})"
      .update.apply()
  }

  // Checks if a user already exists in the database by username
  def userExists(username: String): Boolean = DB.readOnly { implicit session =>
    sql"SELECT * FROM users WHERE username = $username"
      .map(rs => rs.string("username")).single.apply().isDefined
  }

  // Validates login by checking username and password
  def isValidLogin(username: String, password: String): Boolean = DB.readOnly { implicit session =>
    sql"SELECT * FROM users WHERE username = $username AND password = $password"
      .map(rs => rs.string("username")).single.apply().isDefined
  }

  // Updates a user's password in the database
  def updatePassword(username: String, newPassword: String): Int = DB.localTx { implicit session =>
    sql"UPDATE users SET password = $newPassword WHERE username = $username".update.apply()
  }

  // Finds a user by username
  def findByUsername(username: String): Option[User] = DB.readOnly { implicit session =>
    sql"SELECT username, password FROM users WHERE username = $username"
      .map(rs => User(rs.string("username"), rs.string("password")))
      .single.apply()
  }
}
