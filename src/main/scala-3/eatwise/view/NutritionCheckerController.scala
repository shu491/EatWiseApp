package eatwise.view

import javafx.fxml.{FXML, FXMLLoader}
import javafx.scene.control.{Button, Label, TextField}
import javafx.scene.image.{Image, ImageView}
import javafx.scene.Scene
import eatwise.EatWiseApp
import eatwise.repository.{DBIngredient, IngredientRepository}
import eatwise.model.Ingredient
import scalafx.stage.{Modality, Stage}
import scalafx.Includes.*

class NutritionCheckerController {

  @FXML private var ingredientField: TextField = _
  @FXML private var ingredientImage: ImageView = _
  @FXML private var ingredientNameLabel: Label = _
  @FXML private var measureLabel: Label = _
  @FXML private var ingredientPriceLabel: Label = _
  @FXML private var caloriesLabel: Label = _
  @FXML private var proteinLabel: Label = _
  @FXML private var fatLabel: Label = _
  @FXML private var satFatLabel: Label = _
  @FXML private var fiberLabel: Label = _
  @FXML private var carbsLabel: Label = _

  // Holds all ingredients loaded from the repository
  private var ingredients: List[Ingredient] = List.empty

  // Called automatically when FXML is loaded
  @FXML
  def initialize(): Unit = {
    if (DBIngredient.hasIngredientTable) {
      ingredients = IngredientRepository.loadAll()
      println(s"Loaded ${ingredients.size} ingredients")
    } else {
      println("ERROR: Ingredient table not found in database.")
    }
  }

  // Update UI with nutrition details if found
  @FXML
  def handleCheckNutrition(): Unit = {
    // This reload fresh data include ingredient or meal new added
    ingredients = IngredientRepository.loadAll()
    val nameQuery = ingredientField.getText.trim.toLowerCase

    if (nameQuery.isEmpty) {
      ingredientNameLabel.setText("Enter an ingredient name")
      measureLabel.setText("")
      ingredientPriceLabel.setText("")
      caloriesLabel.setText("")
      proteinLabel.setText("")
      fatLabel.setText("")
      satFatLabel.setText("")
      fiberLabel.setText("")
      carbsLabel.setText("")
      ingredientImage.setImage(null)
      return
    }

    // Try to find ingredient or meal by partial name match
    val resultOpt: Option[Ingredient] =
      ingredients.find(_.name.toLowerCase.contains(nameQuery))

    resultOpt match {
      case Some(ing) =>
        ingredientNameLabel.setText(ing.name)
        ingredientPriceLabel.setText(s"${ing.grams} grams")
        measureLabel.setText(ing.measure)

        val n = ing.nutrition
        caloriesLabel.setText(f"${n.calories} kcal")
        proteinLabel.setText(f"${n.protein} g")
        fatLabel.setText(f"${n.fat} g")
        satFatLabel.setText(f"${n.satFat} g")
        fiberLabel.setText(f"${n.fiber} g")
        carbsLabel.setText(f"${n.carbs} g")

        // These code converts the ingredient/meal name into a valid file name
        // This build the file name where ing.name is the name of the matched food
        val photoFileName = s"${ing.name.toLowerCase.replace(" ", "_")}.png"

        // First, check if user photo exists; user photo is the add ingredient photo added by user
        val userPhoto = new java.io.File("user-photos/" + photoFileName)
        if (userPhoto.exists()) {
          ingredientImage.setImage(new Image(userPhoto.toURI.toString))
        } else {
          // fallback to built-in resource image
          val imageUrl = getClass.getResource(s"/images/$photoFileName")
          if (imageUrl != null)
            ingredientImage.setImage(new Image(imageUrl.toExternalForm))
          else
            ingredientImage.setImage(null)
        }

      case None =>
        ingredientNameLabel.setText("Not found")
        measureLabel.setText("")
        ingredientPriceLabel.setText("")
        caloriesLabel.setText("")
        proteinLabel.setText("")
        fatLabel.setText("")
        satFatLabel.setText("")
        fiberLabel.setText("")
        carbsLabel.setText("")
        ingredientImage.setImage(null)
    }
  }

  // Clears all UI fields and resets the nutrition checker form
  @FXML
  def handleClear(): Unit = {
    ingredientField.clear()
    ingredientNameLabel.setText("")
    measureLabel.setText("")
    ingredientPriceLabel.setText("")
    caloriesLabel.setText("")
    proteinLabel.setText("")
    fatLabel.setText("")
    satFatLabel.setText("")
    fiberLabel.setText("")
    carbsLabel.setText("")
    ingredientImage.setImage(null)
  }

  // Open the Add Ingredient dialog
  @FXML def handleAddNew(): Unit = {
    try {
      val loader = new FXMLLoader(getClass.getResource("/eatwise/view/AddIngredient.fxml"))
      val root = loader.load[javafx.scene.Parent]()

      // Use current window as owner if possible, else fallback to main stage
      val ownerWindow =
        if (ingredientField != null && ingredientField.getScene != null) {
          ingredientField.getScene.getWindow
        } else {
          EatWiseApp.stage
        }

      // Create new modal dialog window
      val addStage = new Stage() {
        title = "Add Ingredient or Meal"
        initModality(Modality.ApplicationModal)
        scene = new Scene(root)
        resizable = false
      }

      // Pass stage to controller so it can close itself
      val controller = loader.getController[eatwise.view.AddIngredientController]
      controller.setDialogStage(addStage)

      addStage.showAndWait()

    } catch {
      case ex: Exception =>
        ex.printStackTrace()
    }
  }
  
  // Navigates back to main menu page
  @FXML def handleBack(): Unit = {
    EatWiseApp.showMenuPage()
  }
}
