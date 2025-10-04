package eatwise.view

import javafx.fxml.FXML
import javafx.scene.control.Button
import eatwise.EatWiseApp

class WelcomePageController {

  // Reference to main app
  private var app: EatWiseApp.type = _

  // Setter so the main app can inject itself
  def setApp(app: EatWiseApp.type): Unit = {
    this.app = app
  }

  // Buttons linked from FXML
  @FXML private var loginButton: Button = _
  @FXML private var registerButton: Button = _

  // Called automatically after FXML is loaded
  @FXML
  def initialize(): Unit = {
    loginButton.setOnAction(_ => app.showLogin())
    registerButton.setOnAction(_ => app.showRegister())
  }
}
