package com.dendron.quizzer.presentation.settings

import com.dendron.quizzer.domain.model.Category
import com.dendron.quizzer.domain.model.Difficulty
import com.dendron.quizzer.domain.model.Settings

data class SettingState(
    val isLoading: Boolean = true,
    val settings: Settings = Settings(),
    val questionCount: Int = settings.questionCount,
    val difficulty: Difficulty = settings.difficulty,
    val category: Category = settings.category,
) {
    val hasChanges: Boolean
        get() = questionCount != settings.questionCount ||
            difficulty != settings.difficulty ||
            category != settings.category
}
