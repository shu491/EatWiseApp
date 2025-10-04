package eatwise.view

import javafx.fxml.FXML
import javafx.scene.control.{Button, Label, TextField}
import javafx.scene.layout.TilePane
import javafx.scene.control.Label
import eatwise.model.{BudgetTracker, Ingredient}
import eatwise.util.AlertSystem
import eatwise.repository.IngredientRepository

class BudgetBasedPlannerController {

  @FXML private var budgetField: TextField = _
  @FXML private var mealsPane: TilePane = _
  @FXML private var selectedMealNameLabel: Label = _
  @FXML private var manualPriceField: TextField = _

  @FXML private var caloriesLabel: Label = _
  @FXML private var proteinLabel: Label = _
  @FXML private var fatLabel: Label = _
  @FXML private var satFatLabel: Label = _
  @FXML private var fiberLabel: Label = _
  @FXML private var carbsLabel: Label = _

  @FXML private var alertLabel: Label = _

// Current budget filter
  private var filterBudget: Double = 0.0

// Loads all food from repository
  private val allFoods: List[Ingredient] =
    IngredientRepository.loadAll()

// Suggested meal/ ingredient list
  private var currentPlan: List[Ingredient] = List()
  private var selectedFood: Option[Ingredient] = None // Current selected ingredient/ meal

  // Handles budget-based planner suggestion generation
  @FXML def handleGenerate(): Unit = {
    alertLabel.setText("") // Clear any old warning

    // Try to parse budget input as Double
    val budgetOpt = budgetField.getText.trim.toDoubleOption
    budgetOpt match {
      case Some(filter) if filter > 0 =>
        filterBudget = filter 
        // Suggest ingredient/meal that fit within budget
        val suggested = allFoods.filter { food =>
          val cost = (food.grams / 100.0) * food.pricePer100g
          cost <= filterBudget
        }
        // Update current plan
        currentPlan = suggested
        mealsPane.getChildren.clear()

        // Add clickable meal labels to the UI
        for (food <- currentPlan) {
          val label = new Label(food.name)
          label.setStyle("-fx-border-color: black; -fx-padding: 10; -fx-background-color: #eee;") // set style for UI
          label.setOnMouseClicked(_ => showFoodDetails(food))
          mealsPane.getChildren.add(label)
        }

      case _ =>
    }
  }

  // Show food nutrition and cost details when user selects a food
  def showFoodDetails(food: Ingredient): Unit = {
    selectedFood = Some(food)

    val autoCost = (food.grams / 100.0) * food.pricePer100g
    selectedMealNameLabel.setText(food.name)
    manualPriceField.setText(f"$autoCost%.2f")

    // Display nutrition breakdown
    val n = food.nutrition
    caloriesLabel.setText(f"${n.calories} kcal")
    proteinLabel.setText(f"${n.protein} g")
    fatLabel.setText(f"${n.fat} g")
    satFatLabel.setText(f"${n.satFat} g")
    fiberLabel.setText(f"${n.fiber} g")
    carbsLabel.setText(f"${n.carbs} g")
  }

  // Confirm meal selection and update budget tracking
  @FXML def handleConfirm(): Unit = {
    selectedFood match {
      case Some(food) =>
        // Get manual price if valid, else fall back to auto cost
        val manualOpt = manualPriceField.getText.trim.toDoubleOption
        val autoCost = (food.grams / 100.0) * food.pricePer100g
        val finalCost = manualOpt.filter(_ > 0).getOrElse(autoCost)

        // Check if adding this meal will go over budget or not
        val newTotal = BudgetTracker.spent + finalCost
        if (newTotal > BudgetTracker.budget) {
          alertLabel.setText(s"Adding this meal would exceed budget! Please choose a cheaper meal.")
          return
        }

        // Update budget and expenses
        BudgetTracker.spent = newTotal
        val today = java.time.LocalDate.now()
        BudgetTracker.expenses :+= f"$today      ${food.name}      RM$finalCost%.2f"

        // Show budget status
        val budgetAlert = AlertSystem.checkBudgetStatus()
        alertLabel.setText(s"$budgetAlert")

        // Reset UI for next selection
        manualPriceField.clear()
        selectedFood = None

      case None =>
        // show warning if no meal selected
        alertLabel.setText("Please select a meal before confirming.")
    }
  }

  // Navigate back to the main menu page
  @FXML def handleBack(): Unit = {
    eatwise.EatWiseApp.showMenuPage()
  }
}
