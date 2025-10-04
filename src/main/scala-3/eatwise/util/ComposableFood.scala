package eatwise.util

import eatwise.model.{Ingredient, NutritionInfo}

// Trait for any food item that is composed of multiple ingredients
trait ComposableFood {

  def ingredients: List[(Ingredient, Double)] // each: Ingredient + grams used in Meal

  // Calculate total cost based on grams & price per 100g
  def totalCost: Double = ingredients.map {
    case (ingredient, grams) =>
      (grams / 100.0) * ingredient.pricePer100g
  }.sum

  // Calculate total nutrition for this Meal
  def totalNutrition: NutritionInfo = {
    ingredients.map {
      case (ingredient, grams) =>
        val factor = grams / 100.0  // scale from per 100g
        val n = ingredient.nutrition
        NutritionInfo(
          calories = n.calories * factor,
          protein = n.protein * factor,
          fat = n.fat * factor,
          satFat = n.satFat * factor,
          fiber = n.fiber * factor,
          carbs = n.carbs * factor
        )
    }.reduce { (a, b) =>  // reduce the list of NutritionInfo into a single aggregated NutritionInfo
      NutritionInfo(
        calories = a.calories + b.calories,
        protein = a.protein + b.protein,
        fat = a.fat + b.fat,
        satFat = a.satFat + b.satFat,
        fiber = a.fiber + b.fiber,
        carbs = a.carbs + b.carbs
      )
    }
  }
}
