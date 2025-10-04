package eatwise.repository

import scalikejdbc.*

// Trait that sets up a separate database connection for Ingredients.
trait DBIngredient {

  val driver = "org.apache.derby.jdbc.EmbeddedDriver"
  val url = "jdbc:derby:IngredientDB;create=true"
  val user = "me"
  val password = "mine"
  val settings = ConnectionPoolSettings()

  Class.forName(driver)

  
  ConnectionPool.add(Symbol("ingredient"), url, user, password, settings)

  implicit val session: DBSession = NamedAutoSession(Symbol("ingredient"))
}

// Companion object extending DBIngredient to add initialization logic.
object DBIngredient extends DBIngredient {

  // Initializes the Ingredient table if not exist
  def init(): Unit = {
    if (!hasIngredientTable) {
      NamedDB(Symbol("ingredient")).autoCommit { implicit session =>
        sql"""
          CREATE TABLE ingredient (
            id INT GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
            measure VARCHAR(50),
            name VARCHAR(255),
            grams DOUBLE,
            calories DOUBLE,
            protein DOUBLE,
            fat DOUBLE,
            sat_fat DOUBLE,
            fiber DOUBLE,
            carbs DOUBLE,
            price_per_100g DOUBLE,
            category VARCHAR(255)
          )
        """.execute.apply()
      }
      println("Ingredient table created")
    } else {
      println("Ingredient table already exists")
    }
  }

  // Checks if the 'ingredient' table already exists in the DB
  def hasIngredientTable: Boolean =
    NamedDB(Symbol("ingredient")).getTable("ingredient").isDefined
}
