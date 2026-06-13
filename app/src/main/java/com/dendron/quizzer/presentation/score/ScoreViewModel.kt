package com.dendron.quizzer.presentation.score

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dendron.quizzer.domain.model.Settings
import com.dendron.quizzer.domain.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScoreViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(ScoreUiState())
    val state: StateFlow<ScoreUiState> = _state.asStateFlow()
    private var hasRecorded = false

    fun recordResult(score: Int, questionCount: Int) {
        if (hasRecorded) return
        hasRecorded = true

        viewModelScope.launch {
            val previous = settingsRepository.getSettings().first()
            settingsRepository.recordGameResult(score, questionCount)
            val current = settingsRepository.getSettings().first()

            _state.update {
                ScoreUiState(
                    isNewBestScore = score > previous.bestScore,
                    isNewBestStreak = current.currentStreak > previous.bestStreak,
                    currentStreak = current.currentStreak,
                    bestStreak = current.bestStreak,
                    bestScore = current.bestScore,
                )
            }
        }
    }
}

data class ScoreUiState(
    val isNewBestScore: Boolean = false,
    val isNewBestStreak: Boolean = false,
    val currentStreak: Int = 0,
    val bestStreak: Int = 0,
    val bestScore: Int = 0,
)
