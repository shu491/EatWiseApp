package eatwise.view

import javafx.fxml.FXML
import javafx.scene.control.{Label, ListView, TextField}
import javafx.collections.FXCollections
import eatwise.EatWiseApp
import eatwise.model.BudgetTracker

class BudgetStatusTrackerController {

  @FXML private var BudgetField: TextField = _
  @FXML private var BudgetLabel: Label = _
  @FXML private var spentLabel: Label = _
  @FXML private var remainingLabel: Label = _
  @FXML private var expensesList: ListView[String] = _
  @FXML private var errorLabel: Label = _
  @FXML private val expenses = FXCollections.observableArrayList[String]()

  // Called automatically when view loads
  @FXML def initialize(): Unit = {
    expensesList.setItems(expenses)
    expenses.clear()
    expenses.addAll(BudgetTracker.expenses *)
    updateStatus()
  }

  // Handle setting a new budget
  @FXML def handleSetBudget(): Unit = {
    val input = BudgetField.getText.trim
    input.toDoubleOption match {
      case Some(value) if value > 0 =>  // Valid positive number
        BudgetTracker.budget = value
        errorLabel.setText("")  // Clear error
        updateStatus()
      case _ =>
        errorLabel.setText("Invalid budget. Please enter a number.")
    }
  }

  // Update labels with budget, spent, and remaining amount
  private def updateStatus(): Unit = {
    BudgetLabel.setText(f"Budget: RM ${BudgetTracker.budget}%.2f")
    spentLabel.setText(f"Spent So Far: RM ${BudgetTracker.spent}%.2f")
    remainingLabel.setText(f"Remaining: RM ${BudgetTracker.budget - BudgetTracker.spent}%.2f")
  }

  // Handle clearing all expenses and resetting budget
  @FXML def handleClearExpenses(): Unit = {
    BudgetTracker.spent = 0.0
    BudgetTracker.expenses = List()
    BudgetTracker.budget = 0.0
    BudgetField.clear()

    expenses.clear()
    updateStatus()
  }

  // Navigate back to main menu page
  @FXML def handleBack(): Unit = {
    EatWiseApp.showMenuPage()
  }
}
