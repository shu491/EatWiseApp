package eatwise.model

object BudgetTracker {
  var budget: Double = 0.0  // Total budget set by user
  var spent: Double = 0.0   // Total amount spent so far
  var expenses: List[String] = List()

  // Check if the current spending is within the allocated budget
  // @return true if spent is less than or equal to budget, false otherwise
  def isWithinBudget: Boolean = spent <= budget
  
  // Resets the budget, spent amount, and expenses list
  def reset(): Unit = {
    budget = 0.0
    spent = 0.0
    expenses = List()
  }
}
