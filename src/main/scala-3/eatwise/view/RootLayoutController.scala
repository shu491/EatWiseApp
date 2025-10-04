package eatwise.view

import javafx.fxml.FXML
import javafx.event.ActionEvent
import eatwise.EatWiseApp
import eatwise.model.BudgetTracker

// Controller for the application's root layout
// Handles global actions like logout, exit, and navigation
class RootLayoutController {

  // Close the entire application
  @FXML
  def handleClose(action: ActionEvent): Unit =
    System.exit(0)

  // Log out the user
  @FXML 
  def handleLogout(): Unit = {
    BudgetTracker.reset() // Clear the budget status when logout
    EatWiseApp.showWelcomePage()
  }

  // Open the "About App" dialog with app info
  @FXML
  def handleShowAbout(): Unit =
    EatWiseApp.showAboutDialog()


  // Navigation to corresponding page
  def handleShowMenuPage(): Unit = EatWiseApp.showMenuPage()
  def handleShowBudgetBasedPlanner(): Unit = EatWiseApp.showBudgetBasedPlanner()
  def handleShowBudgetStatusTracker(): Unit = EatWiseApp.showBudgetStatusTracker()
  def handleShowNutritionChecker(): Unit = EatWiseApp.showNutritionChecker()
}
