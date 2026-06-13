package com.dendron.quizzer.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dendron.quizzer.domain.model.Settings
import com.dendron.quizzer.domain.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    settingsRepository: SettingsRepository,
) : ViewModel() {
    val state: StateFlow<HomeUiState> = settingsRepository.getSettings()
        .map { settings -> HomeUiState.from(settings) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HomeUiState(),
        )
}

data class HomeUiState(
    val bestScore: String = "0",
    val bestAccuracy: String = "0%",
    val currentStreak: String = "0",
    val bestStreak: String = "0",
    val lastRound: String = "No games played yet",
    val hasHistory: Boolean = false,
) {
    companion object {
        fun from(settings: Settings): HomeUiState {
            val lastRound = if (settings.lastQuestionCount == 0) {
                "No games played yet"
            } else {
                val correctAnswers = settings.lastScore / 100
                val percentage = ((correctAnswers * 100f) / settings.lastQuestionCount).toInt()
                "$correctAnswers/${settings.lastQuestionCount} correct · $percentage%"
            }

            return HomeUiState(
                bestScore = settings.bestScore.toString(),
                bestAccuracy = "${settings.bestPercentage}%",
                currentStreak = settings.currentStreak.toString(),
                bestStreak = settings.bestStreak.toString(),
                lastRound = lastRound,
                hasHistory = settings.lastQuestionCount > 0,
            )
        }
    }
}
