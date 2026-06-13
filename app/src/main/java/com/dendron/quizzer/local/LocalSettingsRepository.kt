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
    private val bestScoreKey = intPreferencesKey("best_score")
    private val bestPercentageKey = intPreferencesKey("best_percentage")
    private val currentStreakKey = intPreferencesKey("current_streak")
    private val bestStreakKey = intPreferencesKey("best_streak")
    private val lastScoreKey = intPreferencesKey("last_score")
    private val lastQuestionCountKey = intPreferencesKey("last_question_count")

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
                    bestScore = settings[bestScoreKey] ?: 0,
                    bestPercentage = settings[bestPercentageKey] ?: 0,
                    currentStreak = settings[currentStreakKey] ?: 0,
                    bestStreak = settings[bestStreakKey] ?: 0,
                    lastScore = settings[lastScoreKey] ?: 0,
                    lastQuestionCount = settings[lastQuestionCountKey] ?: 0,
                )
            }

    override suspend fun setSettings(newSettings: Settings) {
        dataStore.edit { settings ->
            settings[countKey] = newSettings.questionCount
            settings[difficultyKey] = newSettings.difficulty.name
            settings[categoryKey] = newSettings.category.name
            settings[bestScoreKey] = newSettings.bestScore
            settings[bestPercentageKey] = newSettings.bestPercentage
            settings[currentStreakKey] = newSettings.currentStreak
            settings[bestStreakKey] = newSettings.bestStreak
            settings[lastScoreKey] = newSettings.lastScore
            settings[lastQuestionCountKey] = newSettings.lastQuestionCount
        }
    }

    override suspend fun recordGameResult(score: Int, questionCount: Int) {
        val correctAnswers = if (questionCount == 0) 0 else score / 100
        val percentage = if (questionCount == 0) 0 else ((correctAnswers * 100f) / questionCount).toInt()

        dataStore.edit { settings ->
            val currentBestScore = settings[bestScoreKey] ?: 0
            val currentBestPercentage = settings[bestPercentageKey] ?: 0
            val currentStreak = settings[currentStreakKey] ?: 0
            val bestStreak = settings[bestStreakKey] ?: 0
            val nextStreak = if (percentage >= 70) currentStreak + 1 else 0

            settings[bestScoreKey] = maxOf(currentBestScore, score)
            settings[bestPercentageKey] = maxOf(currentBestPercentage, percentage)
            settings[currentStreakKey] = nextStreak
            settings[bestStreakKey] = maxOf(bestStreak, nextStreak)
            settings[lastScoreKey] = score
            settings[lastQuestionCountKey] = questionCount
        }
    }

    private fun String?.toDifficulty(): Difficulty =
        runCatching { Difficulty.valueOf(this ?: Difficulty.Any.name) }
            .getOrDefault(Difficulty.Any)

    private fun String?.toCategory(): Category =
        runCatching { Category.valueOf(this ?: Category.Any.name) }
            .getOrDefault(Category.Any)
}