package eatwise.view

import javafx.fxml.{FXML, FXMLLoader}
import javafx.scene.{Parent, Scene}
import javafx.scene.control.{Label, PasswordField, TextField}
import javafx.stage.Stage
import eatwise.EatWiseApp
import eatwise.repository.{User, UserDAO}


class LoginController {

  @FXML private var usernameField: TextField = _
  @FXML private var passwordField: PasswordField = _
  @FXML private var errorLabel: Label = _

  private var loginStage: Stage = _
  private var app: EatWiseApp.type = _

  // Setter to inject login stage from outside
  def setLoginStage(stage: Stage): Unit = {
    this.loginStage = stage
  }

  // Setter to inject reference to main app
  def setApp(appRef: EatWiseApp.type): Unit = {
    this.app = appRef
  }

  // Handles login logic
  // Checks if username/password fields are empty and show proper error message
  // Validates against UserDAO if both field are filled
  @FXML def handleLogin(): Unit = {
    val username = usernameField.getText.trim
    val password = passwordField.getText.trim

    if (username.isEmpty && password.isEmpty) {
      errorLabel.setText("Please enter username and password to login.")
    } else if (username.nonEmpty && password.isEmpty) {
      errorLabel.setText("Please enter password to login.")
    } else if (username.isEmpty && password.nonEmpty) {
      errorLabel.setText("Please enter username to login.")
    } else {
      if (UserDAO.isValidLogin(username, password)) {
        // Save the login user globally
        EatWiseApp.currentUser = Some(User(username, password))
        // Proceed to root layout
        app.showRootLayout()
      } else {
        // Differentiate between wrong password and completely invalid user
        val userExists = UserDAO.userExists(username)
        if (userExists) {
          errorLabel.setText("Incorrect password. Please try again.")
        } else {
          errorLabel.setText("Invalid credentials. Please register.")
        }
      }
    }
  }


  // Back to Welcome Page
  @FXML def handleBack(): Unit = {
    app.showWelcomePage()
  }
}
