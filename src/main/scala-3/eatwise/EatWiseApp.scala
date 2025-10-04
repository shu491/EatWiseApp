package eatwise

import eatwise.repository.{DBIngredient, Database, User}
import javafx.fxml.FXMLLoader
import scalafx.application.JFXApp3
import scalafx.scene.Scene
import scalafx.Includes.*
import scalafx.scene as sfxs
import javafx.scene as jfxs
import scalafx.stage.{Modality, Stage}

object EatWiseApp extends JFXApp3:

  // Holds current log in user
  var currentUser: Option[User] = None
  // The main root layout used as container for switching views
  var rootLayout: Option[sfxs.layout.BorderPane] = None

  // Application entry point
  override def start(): Unit = {
    DBIngredient.init() // the nutrition DB
    Database.setupDB() //  user DB

    // Run importer once, then comment out because this is to just save csv file into database
    // MUST RUN ONCE AT FIRST!!!
    //eatwise.repository.CSVImporter.importCSV("src/main/resources/data/nutrients_csvfile.csv")
    showWelcomePage()
  }

  // Show the Welcome Page
  def showWelcomePage(): Unit =
    val resource = getClass.getResource("view/WelcomePage.fxml")
    val loader = new FXMLLoader(resource)
    val welcomeRoot = loader.load[jfxs.Parent]

    // Create and configure the primary stage
    stage = new JFXApp3.PrimaryStage {
      title = "Welcome to EatWise"
      scene = new Scene():
        root = welcomeRoot
      resizable = false
      width = 600
      height = 500
    }

    // Pass reference of this app to the controller
    val controller = loader.getController[eatwise.view.WelcomePageController]
    controller.setApp(this)

  // Show login page
  def showLogin(): Unit =
    val loginResource = getClass.getResource("view/Login.fxml")
    val loginLoader = new FXMLLoader(loginResource)
    val loginRoot = loginLoader.load[jfxs.Parent]

  // Update the current stage’s scene
    stage.scene = new Scene():
      root = loginRoot
    stage.title = "EatWise Login"
    stage.resizable = true

    // Give controller a reference to this app and stage
    val controller = loginLoader.getController[eatwise.view.LoginController]
    controller.setLoginStage(stage)
    controller.setApp(this)

  // Show register page
  def showRegister(): Unit =
    val registerResource = getClass.getResource("view/Register.fxml")
    val registerLoader = new FXMLLoader(registerResource)
    val registerRoot = registerLoader.load[jfxs.Parent]

    // Reuse the same primary stage
    stage.scene = new Scene():
      root = registerRoot

    stage.title = "EatWise Register"
    stage.resizable = true

    val controller = registerLoader.getController[eatwise.view.RegisterController]
    controller.setRegisterStage(stage)
    controller.setApp(this)
  
  // Show root layour; the main app window with menu bar
  def showRootLayout(): Unit =
    val rootResource = getClass.getResource("view/RootLayout.fxml")
    val loader = new FXMLLoader(rootResource)
    loader.load()

    rootLayout = Option(loader.getRoot[jfxs.layout.BorderPane])

    // Update the primary stage with the main app window
    stage = new JFXApp3.PrimaryStage {
      scene = new Scene():
        root = rootLayout.get
      resizable = true
      width = 600
      height = 600
      stage.minWidth = 600
      stage.minHeight = 600
    }

    showMenuPage()  // load menu as default center

  // Show menu page in center of root layout
  def showMenuPage(): Unit =
    val resource = getClass.getResource("view/MenuPageView.fxml")
    val loader = new FXMLLoader(resource)
    loader.load()
    val overview = loader.getRoot[jfxs.Parent]
    val controller = loader.getController[eatwise.view.MenuPageController]
    controller.setApp(this)
    controller.setApp(this)
    rootLayout.get.center = overview

    stage.title = "EatWise Menu Page"

  // Show budget based planner view
  def showBudgetBasedPlanner(): Unit =
    val resource = getClass.getResource("view/BudgetBasedPlannerView.fxml")
    val loader = new FXMLLoader(resource)
    loader.load()
    val content = loader.getRoot[jfxs.Parent]
    rootLayout.get.center = content
    stage.title = "Budget-Based Planner"

  // Show budget status tracker view
  def showBudgetStatusTracker(): Unit =
    val resource = getClass.getResource("view/BudgetStatusTrackerView.fxml")
    val loader = new FXMLLoader(resource)
    loader.load()
    val content = loader.getRoot[jfxs.Parent]
    rootLayout.get.center = content
    stage.title = "Budget Status Tracker"

  // Show nutrition checker view
  def showNutritionChecker(): Unit =
    val resource = getClass.getResource("view/NutritionCheckerView.fxml")
    val loader = new FXMLLoader(resource)
    loader.load()
    val content = loader.getRoot[jfxs.Parent]
    rootLayout.get.center = content
    stage.title = "Nutrition Checker"
  
  // Show user profile view
  def showUserProfile(): Unit =
    val resource = getClass.getResource("view/UserProfile.fxml")
    val loader = new FXMLLoader(resource)
    loader.load()
    val page = loader.getRoot[jfxs.Parent]

    val controller = loader.getController[eatwise.view.UserProfileController]
    controller.setUser(currentUser.getOrElse(
      throw new IllegalStateException("No user is logged in!")
    ))
    // Show the profile in the main center
    rootLayout.get.center = page
    stage.title = "User Profile"

    // Resize the window so it fit the new content
    stage.sizeToScene()


  //Show About Dialog
  def showAboutDialog(): Unit =
    val aboutResource = getClass.getResource("view/AboutApp.fxml")
    val loader = new FXMLLoader(aboutResource)
    val page = loader.load[jfxs.Parent]

    val dialogStage = new Stage() {
      title = "About EatWise"
      initModality(Modality.ApplicationModal)
      initOwner(stage)
      scene = new Scene(page)
      resizable = false
    }
    
    // Pass the dialog stage to the controller
    val controller = loader.getController[eatwise.view.AboutAppController]
    controller.setDialogStage(dialogStage)

    dialogStage.showAndWait()

end EatWiseApp

