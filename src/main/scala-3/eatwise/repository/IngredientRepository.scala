package eatwise.repository

import scalikejdbc._
import eatwise.model.{Ingredient, NutritionInfo, FoodCategory}

// Repository for managing Ingredient records in the database
object IngredientRepository {

  // Creates the 'ingredient' table in the database
  // Ensures the schema contains fields for nutrition data, price, and category
  def createTable(): Unit = {
    NamedDB(Symbol("ingredient")).autoCommit { implicit s =>
      sql"""
        CREATE TABLE ingredient (
          id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
          name VARCHAR(255) NOT NULL,
          measure VARCHAR(50) NOT NULL,
          grams DOUBLE NOT NULL,
          calories DOUBLE NOT NULL,
          protein DOUBLE NOT NULL,
          fat DOUBLE NOT NULL,
          sat_fat DOUBLE NOT NULL,
          fiber DOUBLE NOT NULL,
          carbs DOUBLE NOT NULL,
          price_per_100g DOUBLE NOT NULL,
          category VARCHAR(100) NOT NULL
        )
      """.execute.apply()
    }
  }

  // Finds an ingredient by its name; case-insensitive
  def findByName(name: String): Option[Ingredient] =
    loadAll().find(_.name.equalsIgnoreCase(name))

  // Loads all ingredients from the database; @return A list of Ingredient objects
  def loadAll(): List[Ingredient] = {
    NamedDB(Symbol("ingredient")).readOnly { implicit s =>
      sql"SELECT * FROM ingredient".map { rs =>
        Ingredient(
          name = rs.string("name"),
          measure = rs.string("measure"),
          grams = rs.double("grams"),
          pricePer100g = rs.double("price_per_100g"),
          nutrition = NutritionInfo(
            calories = rs.double("calories"),
            protein = rs.double("protein"),
            fat = rs.double("fat"),
            satFat = rs.double("sat_fat"),
            fiber = rs.double("fiber"),
            carbs = rs.double("carbs")
          ),
          category = FoodCategory.valueOf(rs.string("category"))
        )
      }.list.apply()
    }
  }

  // Inserts a new ingredient into the database
  def insert(ingredient: Ingredient): Unit = {
    NamedDB(Symbol("ingredient")).autoCommit { implicit s =>
      sql"""
        INSERT INTO ingredient (name, measure, grams, calories, protein, fat, sat_fat, fiber, carbs, price_per_100g, category)
        VALUES (${ingredient.name}, ${ingredient.measure}, ${ingredient.grams}, ${ingredient.nutrition.calories},
                ${ingredient.nutrition.protein}, ${ingredient.nutrition.fat}, ${ingredient.nutrition.satFat},
                ${ingredient.nutrition.fiber}, ${ingredient.nutrition.carbs}, ${ingredient.pricePer100g},
                ${ingredient.category.toString})
      """.update.apply()
    }
  }
}
