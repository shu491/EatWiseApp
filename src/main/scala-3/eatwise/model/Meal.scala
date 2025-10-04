package eatwise.model
import eatwise.util.ComposableFood

// Represents a meal composed of multiple ingredients
//List[(Ingredient, Double)] is (ingredient, grams)
case class Meal ( name: String, ingredients: List[(Ingredient, Double)]
                ) extends ComposableFood




