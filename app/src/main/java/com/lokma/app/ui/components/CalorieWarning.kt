package com.lokma.app.ui.components

enum class CalorieWarningState {
    NORMAL,
    LOW_REMAINING,
    OVER_TARGET
}

fun resolveCalorieWarningState(
    totalCalories: Int,
    calorieTarget: Int,
    calorieWarningThreshold: Int
): CalorieWarningState {
    if (totalCalories > calorieTarget) {
        return CalorieWarningState.OVER_TARGET
    }

    val remainingCalories = calorieTarget - totalCalories
    if (remainingCalories <= calorieWarningThreshold) {
        return CalorieWarningState.LOW_REMAINING
    }

    return CalorieWarningState.NORMAL
}
