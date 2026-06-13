package com.dendron.quizzer.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dendron.quizzer.domain.model.Category
import com.dendron.quizzer.domain.model.Difficulty
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
class SettingViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(SettingState())
    val state: StateFlow<SettingState> = _state.asStateFlow()

    init {
        loadSettings()
    }

    fun onQuestionCountChanged(questionCount: Int) {
        _state.update { it.copy(questionCount = questionCount) }
    }

    fun onDifficultyChanged(difficulty: Difficulty) {
        _state.update { it.copy(difficulty = difficulty) }
    }

    fun onCategoryChanged(category: Category) {
        _state.update { it.copy(category = category) }
    }

    fun onSaveSettings() {
        viewModelScope.launch {
            val currentState = state.value
            val newSettings = currentState.settings.copy(
                questionCount = currentState.questionCount,
                difficulty = currentState.difficulty,
                category = currentState.category,
            )
            settingsRepository.setSettings(newSettings)
            _state.update {
                it.copy(settings = newSettings)
            }
        }
    }

    private fun loadSettings() {
        viewModelScope.launch {
            val settings = settingsRepository.getSettings().first()
            _state.value = SettingState(
                isLoading = false,
                settings = settings,
                questionCount = settings.questionCount,
                difficulty = settings.difficulty,
                category = settings.category,
            )
        }
    }
}