package eatwise.view

import javafx.fxml.FXML
import javafx.scene.control.{Label, PasswordField}
import javafx.scene.shape.Circle
import scalafx.scene.image.Image
import javafx.scene.paint.ImagePattern
import eatwise.EatWiseApp
import eatwise.repository.{User, UserDAO}

class UserProfileController {

  @FXML private var usernameLabel: Label = _
  @FXML private var passwordLabel: Label = _
  @FXML private var newPasswordField: PasswordField = _
  @FXML private var statusLabel: Label = _
  @FXML private var profileCircle: Circle = _

  private var currentUser: User = _

  // Sets the current user in this controller
  // Update the UI labels with user data
  def setUser(user: User): Unit = {
    currentUser = user
    usernameLabel.setText(user.username)
    passwordLabel.setText(user.password)
    setProfilePicture() // fix profile picture; Cannot be changed
  }

  // Handles password update
  // Validates new password and updates both DB and UI
  @FXML def handleUpdatePassword(): Unit = {
    val newPass = newPasswordField.getText.trim
    // Validation checks
    if (newPass.isEmpty) {
      statusLabel.setText("New password cannot be empty.")
    }
    else if (newPass == currentUser.password) {
      statusLabel.setText("New password cannot be the same as the old password.")
    }
    else {
      // Update password in memory and database
      currentUser = currentUser.copy(password = newPass)
      UserDAO.updatePassword(currentUser.username, newPass)

      // Update UI feedback
      passwordLabel.setText(newPass)
      statusLabel.setText("Password updated successfully!")
      newPasswordField.clear()
    }
  }

  // Loads the profile picture avatar.png for visualisation
  def setProfilePicture(): Unit = {
    val image = new Image(getClass.getResource("/images/avatar.png").toExternalForm)
    profileCircle.setFill(new ImagePattern(image))
  }


  // Navigate back to main page
  @FXML def handleBack(): Unit = {
    EatWiseApp.showMenuPage()
  }
}
