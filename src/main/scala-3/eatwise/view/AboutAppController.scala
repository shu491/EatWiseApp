package eatwise.view

import javafx.fxml.FXML
import javafx.stage.Stage
import javafx.scene.control.Button
import javafx.scene.image.Image
import javafx.scene.shape.Rectangle
import javafx.scene.paint.ImagePattern

class AboutAppController {
  
  private var dialogStage: Stage = _

  @FXML private var closeButton: Button = _
  @FXML private var photoRect: Rectangle = _

  // Called by loader to provide the stage reference for this dialog
  def setDialogStage(stage: Stage): Unit =
    this.dialogStage = stage

  // Automatically called by JavaFX after the FXML has been loaded
  @FXML
  def initialize(): Unit =
    loadAboutImage()

  // Load the image resource and fills the rectangle with image
  private def loadAboutImage(): Unit =
    val image = new Image(getClass.getResource("/images/AboutPageImage.png").toExternalForm)
    photoRect.setFill(new ImagePattern(image))

  // Close the dialog window
  @FXML
  def handleClose(): Unit =
    dialogStage.close()

}
