package nutritrack.model.budget

/**package nutritrack.model.budget

import nutritrack.model.BudgetTracker

case class BudgetReport(totalSpent: Double, remaining: Double, overBudget: Boolean)

class BudgetAnalysis {
  def analyze(tracker: BudgetTracker): BudgetReport = {
    BudgetReport(
      totalSpent = tracker.totalExpenses,
      remaining = tracker.remainingBudget,
      overBudget = !tracker.isWithinBudget
    )
  }

  def printReport(report: BudgetReport): Unit = {
    println(s"Total Spent: RM${report.totalSpent}")
    println(s"Remaining Budget: RM${report.remaining}")
    if (report.overBudget) {
      println("!!!WARNING: You are over budget!")
    } else {
      println("You are within budget.")
    }
  }
} **/