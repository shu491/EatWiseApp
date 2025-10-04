package nutritrack.repository

import scalikejdbc._
import nutritrack.model.User

object UserRepository {

  def updatePassword(username: String, newPassword: String): Unit = {
    DB localTx { implicit session =>
      sql"""
        UPDATE Users SET password = $newPassword WHERE username = $username
      """.update.apply()
    }
  }

  def findByUsername(username: String): Option[User] = {
    DB readOnly { implicit session =>
      sql"""
        SELECT username, password FROM Users WHERE username = $username
      """.map(rs => User(rs.string("username"), rs.string("password"))).single.apply()
    }
  }

}
