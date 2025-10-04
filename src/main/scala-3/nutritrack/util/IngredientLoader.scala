package nutritrack.util

/**package nutritrack.util

import nutritrack.model.{Ingredient, NutritionInfo, FoodCategory}
import scala.io.Source
import scala.util.Try

object IngredientLoader {

  private def parseDouble(s: String): Double =
    Try(s.replaceAll("[^\\d.]", "").toDouble).getOrElse(0.0)

  def loadCSV(path: String): List[Ingredient] = {
    val source = Source.fromResource(path)
    val lines = source.getLines().drop(1) // skip header

    val ingredients = lines.map { line =>
      val cols = line.split(",").map(_.trim)

      // Defensive check in case row is malformed
      if cols.length < 10 then return List.empty

      val name        = cols(0)
      val grams       = parseDouble(cols(2))      // Grams is column 2
      val calories    = parseDouble(cols(3))
      val protein     = parseDouble(cols(4))
      val fat         = parseDouble(cols(5))
      val satFat      = parseDouble(cols(6))
      val fiber       = parseDouble(cols(7))
      val carbs       = parseDouble(cols(8))
      val rawCategory = cols(9)

      val nutrition = NutritionInfo(calories, protein, fat, satFat, fiber, carbs)
      val category = FoodCategory.fromCSV(rawCategory)
      val pricePer100g = 1.00 // Default fallback price

      Ingredient(name, grams, pricePer100g, nutrition, category)
    }.toList

    source.close()
    ingredients
  }
}**/