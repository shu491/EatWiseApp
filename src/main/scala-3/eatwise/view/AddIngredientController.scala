package eatwise.view

import javafx.fxml.FXML
import javafx.scene.control.{ComboBox, Label, TextField}
import javafx.stage.Stage
import eatwise.model.{Ingredient, NutritionInfo, FoodCategory}
import eatwise.repository.IngredientRepository
import javafx.stage.FileChooser
import java.io.File
import java.nio.file.{Files, Paths, StandardCopyOption}

class AddIngredientController {

  // UI fields from FXML
  @FXML private var nameField: TextField = _
  @FXML private var measureField: TextField = _
  @FXML private var gramsField: TextField = _
  @FXML private var caloriesField: TextField = _
  @FXML private var proteinField: TextField = _
  @FXML private var fatField: TextField = _
  @FXML private var satFatField: TextField = _
  @FXML private var fiberField: TextField = _
  @FXML private var carbsField: TextField = _
  @FXML private var pricePer100gField: TextField = _
  @FXML private var categoryBox: ComboBox[FoodCategory] = _
  @FXML private var statusLabel: Label = _
  @FXML private var photoLabel: Label = _

  private var dialogStage: Stage = _
  private var selectedPhoto: File = null

  // Set dialog stage
  def setDialogStage(stage: Stage): Unit =
    this.dialogStage = stage

  // Called automatically after FXML is loaded
  @FXML def initialize(): Unit = {
    // Populate category dropdown with enum values
    categoryBox.getItems.addAll(FoodCategory.values*)
    categoryBox.getSelectionModel.selectFirst()
  }

  // Open file chooser to select an image
  @FXML def handleBrowse(): Unit = {
    val chooser = new FileChooser()
    chooser.setTitle("Select Ingredient Photo")
    chooser.getExtensionFilters.addAll(
      new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
    )
    val file = chooser.showOpenDialog(dialogStage)
    if (file != null) {
      selectedPhoto = file
      photoLabel.setText("Selected: " + file.getName)
    } else {
      photoLabel.setText("No file selected")
    }
  }


  // Save ingredient to database; Photo is copied
  @FXML def handleSave(): Unit = {
    val name = nameField.getText.trim

    // Check if any field is empty
    if (name.isEmpty || measureField.getText.trim.isEmpty||gramsField.getText.trim.isEmpty || caloriesField.getText.trim.isEmpty ||
      proteinField.getText.trim.isEmpty || fatField.getText.trim.isEmpty ||
      satFatField.getText.trim.isEmpty || fiberField.getText.trim.isEmpty ||
      carbsField.getText.trim.isEmpty || pricePer100gField.getText.trim.isEmpty || categoryBox.getValue == null) {
      statusLabel.setText("Please enter all fields.")
      return
    }

    // Must force user to enter photo to save
    if (selectedPhoto == null) {
      statusLabel.setText("Please select a photo in order to save.")
      return
    }

    // Prevent duplicate ingredient or meal name
    if (IngredientRepository.findByName(name).isDefined) {
      statusLabel.setText("Name already exists!")
      return
    }

    try {
      // Parse user inputs
      val measure = measureField.getText.trim
      val grams = gramsField.getText.trim.toDouble
      val calories = caloriesField.getText.trim.toDouble
      val protein = proteinField.getText.trim.toDouble
      val fat = fatField.getText.trim.toDouble
      val satFat = satFatField.getText.trim.toDouble
      val fiber = fiberField.getText.trim.toDouble
      val carbs = carbsField.getText.trim.toDouble
      val pricePer100g = pricePer100gField.getText.trim.toDouble
      val category = categoryBox.getValue

      // Create nutrition info
      val nutrition = NutritionInfo(calories, protein, fat, satFat, fiber, carbs)

      // Create new ingredient object
      val newIng = Ingredient(name, measure, grams, pricePer100g, nutrition, category)

      // Insert into repository
      IngredientRepository.insert(newIng)

      // Copy photo to local folder
      if (selectedPhoto != null) {
        val destDir = Paths.get("user-photos")
        Files.createDirectories(destDir)

        val safeName = name.toLowerCase.replace(" ", "_") + ".png"
        val destPath = destDir.resolve(safeName)

        Files.copy(selectedPhoto.toPath, destPath, StandardCopyOption.REPLACE_EXISTING)

        println(s"Copied user photo to: ${destPath.toAbsolutePath}")
      }

      statusLabel.setText("Saved successfully.")

    } catch {
      case _: NumberFormatException =>
        statusLabel.setText("Only numbers allowed for nutrition fields.")
      case ex: Exception =>
        ex.printStackTrace()
        statusLabel.setText("Failed to save.")
    }
  }

  // Close the window
  @FXML def handleCancel(): Unit = {
    dialogStage.close()
  }


}
