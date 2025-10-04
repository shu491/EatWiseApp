package eatwise.model

// Represents a meal plan consisting of multiple meals
case class MealPlanner(meals: List[Meal], day: String) {
  
  // Calculates the total cost of all meals in the plan
  def totalCost: Double = meals.map(_.totalCost).sum

  // Summarized the total daily nutrition across all meals
  def NutritionSummary: Map[String, Double] ={
    val total = meals.map(_.totalNutrition).reduce { (a,b) =>
      a.copy (
        calories = a.calories + b.calories,
        protein = a.protein + b.protein,
        fat = a.fat + b.fat,
        satFat = a.satFat + b.satFat,
        fiber = a.fiber + b.fiber,
        carbs = a.carbs + b.carbs
      )
    }

    Map (
      "Calories" -> total.calories,
      "Protein" -> total.protein,
      "Fat" -> total.fat,
      "SatFat" -> total.satFat,
      "Fiber" -> total.fiber,
      "Carbs" -> total.carbs,
    )
  }
}