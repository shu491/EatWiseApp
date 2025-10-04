package nutritrack.model

class Planner(
               availableMeals: List[Meal],
               user: UserProfile,
               dailyBudget: Double
             ) {

  /** Suggest meals that fit the user’s budget & restrictions */
  def suggestMeals: List[Meal] = {
    availableMeals
      .filter(_.totalCost <= dailyBudget)
      .filter(meal => !containsRestricted(meal))
  }

  /** Helper: checks if meal has restricted ingredients */
  private def containsRestricted(meal: Meal): Boolean = {
    if (user.isVegetarian) {
      meal.ingredients.exists { case (ingredient, _) =>
        ingredient.category == FoodCategory.Protein && ingredient.name.toLowerCase.contains("meat")
      }
    } else {
      false
    } ||
      meal.ingredients.exists { case (ingredient, _) =>
        user.allergies.exists(allergy =>
          ingredient.name.toLowerCase.contains(allergy.toLowerCase)
        )
      }
  }

  /** Build daily plan with the cheapest valid meals under budget */
  def buildDailyPlan: MealPlanner = {
    var budgetLeft = dailyBudget
    val selected = suggestMeals
      .sortBy(_.totalCost)
      .takeWhile { meal =>
        if (meal.totalCost <= budgetLeft) {
          budgetLeft -= meal.totalCost
          true
        } else {
          false
        }
      }
    MealPlanner(selected, "Monday") // Day name for example
  }
}
