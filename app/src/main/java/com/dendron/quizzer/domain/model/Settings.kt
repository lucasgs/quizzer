package com.dendron.quizzer.domain.model

import com.dendron.quizzer.common.Constants

data class Settings(
    val questionCount: Int = Constants.QUESTION_COUNT,
    val difficulty: Difficulty = Difficulty.Any,
    val category: Category = Category.Any,
)
