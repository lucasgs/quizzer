package com.dendron.quizzer.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.dendron.quizzer.common.Constants
import com.dendron.quizzer.domain.model.Category
import com.dendron.quizzer.domain.model.Difficulty
import com.dendron.quizzer.domain.model.Settings
import com.dendron.quizzer.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LocalSettingsRepository @Inject constructor(private val dataStore: DataStore<Preferences>) :
    SettingsRepository {

    private val countKey = intPreferencesKey("question_count")
    private val difficultyKey = stringPreferencesKey("difficulty")
    private val categoryKey = stringPreferencesKey("category")

    override fun getSettings(): Flow<Settings> = flow {
        dataStore.data.collect { settings ->
            emit(
                Settings(
                    questionCount = settings[countKey] ?: Constants.QUESTION_COUNT,
                    difficulty = Difficulty.valueOf(settings[difficultyKey] ?: "Any"),
                    category = Category.valueOf(settings[categoryKey] ?: "Any"),
                )
            )
        }
    }

    override suspend fun setSettings(newSettings: Settings) {
        dataStore.edit { settings ->
            settings[countKey] = newSettings.questionCount
            settings[difficultyKey] = newSettings.difficulty.name
            settings[categoryKey] = newSettings.category.name
        }
    }
}