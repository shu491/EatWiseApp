package eatwise.view

import javafx.fxml.FXML
import eatwise.EatWiseApp
import javafx.scene.image.Image
import javafx.scene.paint.ImagePattern

class MenuPageController {

  private var app: EatWiseApp.type = _

  // Setter method to pass the main application reference
  def setApp(mainApp: EatWiseApp.type): Unit = {
    this.app = mainApp
  }

  // Method to set default profile picture (avatar.png) into the profile circle
  def setProfilePicture(): Unit = {
    // Load image from resource
    val image = new Image(getClass.getResource("/images/avatar.png").toExternalForm)
    // Fill the circle with image
    profileCircle.setFill(new ImagePattern(image))
  }

  // FXML-injected reference to profile picture Circle in the UI
  @FXML private var profileCircle: javafx.scene.shape.Circle = _

  // Called automatically after FXMl is loaded
  @FXML def initialize(): Unit = {
    setProfilePicture()
  }

  // Open the budget-based planner page
  @FXML def handleOpenBudgetBasedPlanner(): Unit = {
    app.showBudgetBasedPlanner()
  }

  // Open the Budget Status Tracker page
  @FXML def handleOpenBudgetStatusTracker(): Unit = {
    app.showBudgetStatusTracker()
  }

  // Open the Nutrition Checker page
  @FXML def handleOpenNutritionChecker(): Unit = {
    app.showNutritionChecker()
  }

  // Open the User Profile Page
  @FXML def handleProfileClicked(): Unit =
    EatWiseApp.showUserProfile()



}
