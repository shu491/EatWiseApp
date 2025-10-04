package eatwise.model

// Represents an ingredient in the EatWise App
case class Ingredient(
                       name: String,
                       measure: String,
                       grams: Double,
                       pricePer100g: Double,
                       nutrition: NutritionInfo,
                       category: FoodCategory,
                     )
