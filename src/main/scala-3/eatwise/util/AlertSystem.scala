package eatwise.util

import eatwise.model.BudgetTracker


object AlertSystem {

  // Checks the current budget status by querying BudgetTracker
  def checkBudgetStatus(): String = {
    if (BudgetTracker.isWithinBudget) {
      "You are within your budget. Added Successfully"
    } else {
      "You are over your budget!"
    }
  }

}
