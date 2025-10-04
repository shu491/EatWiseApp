package nutritrack.controller

import nutritrack.NutriTrackApp

class RootLayoutController:
  def handleShowMealPlannerHub(): Unit = NutriTrackApp.showMealPlannerHub()
  def handleShowBudgetMealPlanner(): Unit = NutriTrackApp.showBudgetMealPlanner()
  def handleShowBudgetStatus(): Unit = NutriTrackApp.showBudgetStatus()
  def handleShowIngredientChecker(): Unit = NutriTrackApp.showIngredientChecker()
