package eatwise.model

// Enumeration of all possible food categories in the system
enum FoodCategory:
  case Dairy
  case FatOil
  case Protein
  case Seafood
  case Vegetable
  case Fruit
  case Grain
  case Soup
  case Dessert
  case Jam
  case NutSeed
  case Beverage
  case Other

object FoodCategory:
  def fromCSV(raw: String): FoodCategory =
    val s = raw.trim.toLowerCase
    if s.contains("dairy") || s.contains("milk") then Dairy
    else if s.contains("fat") || s.contains("oil") || s.contains("shortenings") then FatOil
    else if s.contains("meat") || s.contains("poultry") || s.contains("protein") || s.contains("beef") || s.contains("chicken") then Protein
    else if s.contains("fish") || s.contains("seafood") || s.contains("shellfish") then Seafood
    else if s.contains("vegetable") || s.contains("greens") then Vegetable
    else if s.contains("fruit") then Fruit
    else if s.contains("grain") || s.contains("bread") || s.contains("cereal") then Grain
    else if s.contains("soup") then Soup
    else if s.contains("dessert") || s.contains("sweet") || s.contains("cake") || s.contains("pie") then Dessert
    else if s.contains("jam") || s.contains("jelly") then Jam
    else if s.contains("nut") || s.contains("seed") then NutSeed
    else if s.contains("drink") || s.contains("beverage") || s.contains("alcohol") || s.contains("beer") then Beverage
    else Other

