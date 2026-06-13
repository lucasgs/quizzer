package com.dendron.quizzer.domain.model

import com.dendron.quizzer.common.Constants

data class Settings(
    val questionCount: Int = Constants.QUESTION_COUNT,
    val difficulty: Difficulty = Difficulty.Any,
    val category: Category = Category.Any,
    val bestScore: Int = 0,
    val bestPercentage: Int = 0,
    val currentStreak: Int = 0,
    val bestStreak: Int = 0,
    val lastScore: Int = 0,
    val lastQuestionCount: Int = 0,
)
