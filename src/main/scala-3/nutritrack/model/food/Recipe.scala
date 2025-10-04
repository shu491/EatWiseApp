package nutritrack.model.food

import nutritrack.model.Ingredient
import nutritrack.util.ComposableFood

case class Recipe(title: String, 
                  ingredients: List[(Ingredient, Double)], // (ingredient, grams)
                 instructions: String
                 ) extends ComposableFood