package eatwise.repository

import eatwise.model.{FoodCategory, Ingredient, NutritionInfo}
import eatwise.repository.IngredientRepository

import scala.io.Source
import scala.util.Try

object CSVImporter {

  // Safely convert string to Double; Strip non-numeric characters
  def safeToDouble(s: String): Double =
    Try(s.replaceAll("[^\\d.]", "").toDouble).getOrElse(0.0)

  def importCSV(path: String): Unit = {
    println("Checking ingredient table...")

    // Ensure the ingredient table exists in the database
    if (!DBIngredient.hasIngredientTable) {
      println("Table not found. Creating...")
      IngredientRepository.createTable()
    } else {
      println("Table exists.")
    }

    // Read file lines, skip the header row
    val lines = Source.fromFile(path).getLines().drop(1) // skip header

    // Process each line in the CSV file
    for (line <- lines) {
      val cols = line.split(",").map(_.trim)
      // Ensure at least 10 columns are present
      if (cols.length >= 10) {
        try {
          val name       = cols(0)
          val measure    = cols(1)
          val grams      = safeToDouble(cols(2))
          val calories   = safeToDouble(cols(3))
          val protein    = safeToDouble(cols(4))
          val fat        = safeToDouble(cols(5))
          val satFat     = safeToDouble(cols(6))
          val fiber      = safeToDouble(cols(7))
          val carbs      = safeToDouble(cols(8))

          // Last column is always the price
          val pricePer100g = safeToDouble(cols.last)

          // Middle columns are combined into category
          val rawCategory = cols.slice(9, cols.length - 1).mkString(",").stripPrefix("\"").stripSuffix("\"").trim
          val category    = FoodCategory.fromCSV(rawCategory)

          // Build Ingredient object
          val ingredient = Ingredient(
            name = name,
            measure = measure,
            grams = grams,
            pricePer100g = pricePer100g,
            nutrition = NutritionInfo(calories, protein, fat, satFat, fiber, carbs),
            category = category
          )

          // Insert ingredient into repository 
          IngredientRepository.insert(ingredient)
          println(s"Inserted: ${ingredient.name}")

        } catch {
          case e: Exception =>
            println(s"Skipped bad row: $line | Reason: ${e.getMessage}")
        }
      } else {
        // Skip rows with not enough columns
        println(s"Skipped incomplete row: $line")
      }
    }

    println("All valid ingredients imported.")
  }
}
