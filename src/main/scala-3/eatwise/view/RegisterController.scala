package eatwise.view

import javafx.fxml.{FXML, FXMLLoader}
import javafx.scene.control.{Label, PasswordField, TextField}
import javafx.scene.{Parent, Scene}
import javafx.stage.Stage
import eatwise.EatWiseApp
import eatwise.repository.{User, UserDAO}

import scala.util.{Success, Failure}

class RegisterController {

  @FXML private var usernameField: TextField = _
  @FXML private var passwordField: PasswordField = _
  @FXML private var errorLabel: Label = _

  private var registerStage: Stage = _
  private var app: EatWiseApp.type = _

  // Set the stager reference
  def setRegisterStage(stage: Stage): Unit =
    this.registerStage = stage
    
  // Set main app references so controller can navigate
  def setApp(mainApp: EatWiseApp.type): Unit =
    this.app = mainApp

  // Handles user registration when "Register" button is clicked
  @FXML def handleRegister(): Unit = {
    val username = usernameField.getText.trim
    val password = passwordField.getText.trim

    // Check if both fields is filled
    if (username.isEmpty || password.isEmpty) {
      errorLabel.setText("Please enter both fields.")
      return
    }

    // Check if username already exists in database
    if (UserDAO.userExists(username)) {
      errorLabel.setText("Username already exists.")
      return
    }

    // Try to insert new user into DB
    UserDAO.insertUser(User(username, password)) match {
      case Success(_) =>
        errorLabel.setText("Registered! You can now login.")
        usernameField.clear()
        passwordField.clear()
      case Failure(e) =>
        e.printStackTrace()
        errorLabel.setText("Failed to register.")
    }
  }

  // Navigate back to Welcome Page
  @FXML def handleBack(): Unit = {
    try {
      val loader = new FXMLLoader(getClass.getResource("/eatwise/view/WelcomePage.fxml"))
      val root: Parent = loader.load()
      val controller = loader.getController[eatwise.view.WelcomePageController]
      controller.setApp(app)

      // Switch scene back to WelcomePage
      val currentStage = usernameField.getScene.getWindow.asInstanceOf[Stage]
      currentStage.setScene(new Scene(root))
      currentStage.setTitle("EatWise")
      currentStage.show()
    } catch {
      case e: Exception =>
        e.printStackTrace()
        errorLabel.setText("Failed to return to welcome page.")
    }
  }

}
