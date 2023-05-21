package com.dendron.quizzer.local

import com.dendron.quizzer.common.Constants
import com.dendron.quizzer.domain.model.Category
import com.dendron.quizzer.domain.model.Difficulty
import com.dendron.quizzer.domain.model.Settings
import com.dendron.quizzer.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LocalSettingsRepository : SettingsRepository {

    private var settings = Settings(
        questionCount = Constants.QUESTION_COUNT,
        difficulty = Difficulty.Any,
        category = Category.Any,
    )

    override suspend fun getSettings(): Flow<Settings> =
        flow {
            emit(settings)
        }

    override suspend fun setSettings(newSettings: Settings) {
        settings = newSettings
    }
}