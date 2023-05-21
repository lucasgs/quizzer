package com.dendron.quizzer.domain.repository

import com.dendron.quizzer.domain.model.Settings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    suspend fun getSettings(): Flow<Settings>
    suspend fun setSettings(newSettings: Settings)
}