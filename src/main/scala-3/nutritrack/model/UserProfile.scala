package nutritrack.model

case class UserProfile(
                        name: String,
                        age: Int,
                        householdSize: Int,
                        isVegetarian: Boolean = false,
                        allergies: List[String] = List.empty
                      )
