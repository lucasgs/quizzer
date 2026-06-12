package com.dendron.quizzer.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.dendron.quizzer.common.Constants
import com.dendron.quizzer.domain.model.Category
import com.dendron.quizzer.domain.model.Difficulty
import com.dendron.quizzer.domain.model.Settings
import com.dendron.quizzer.domain.repository.SettingsRepository
import java.io.IOException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalSettingsRepository @Inject constructor(private val dataStore: DataStore<Preferences>) :
    SettingsRepository {

    private val countKey = intPreferencesKey("question_count")
    private val difficultyKey = stringPreferencesKey("difficulty")
    private val categoryKey = stringPreferencesKey("category")

    override fun getSettings(): Flow<Settings> =
        dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { settings ->
                Settings(
                    questionCount = settings[countKey] ?: Constants.QUESTION_COUNT,
                    difficulty = settings[difficultyKey].toDifficulty(),
                    category = settings[categoryKey].toCategory(),
                )
            }

    override suspend fun setSettings(newSettings: Settings) {
        dataStore.edit { settings ->
            settings[countKey] = newSettings.questionCount
            settings[difficultyKey] = newSettings.difficulty.name
            settings[categoryKey] = newSettings.category.name
        }
    }

    private fun String?.toDifficulty(): Difficulty =
        runCatching { Difficulty.valueOf(this ?: Difficulty.Any.name) }
            .getOrDefault(Difficulty.Any)

    private fun String?.toCategory(): Category =
        runCatching { Category.valueOf(this ?: Category.Any.name) }
            .getOrDefault(Category.Any)
}