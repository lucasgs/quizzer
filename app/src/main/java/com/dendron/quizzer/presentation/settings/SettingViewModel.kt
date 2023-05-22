package com.dendron.quizzer.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dendron.quizzer.domain.model.Category
import com.dendron.quizzer.domain.model.Difficulty
import com.dendron.quizzer.domain.model.Settings
import com.dendron.quizzer.domain.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(SettingState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            settingsRepository.getSettings().collect { lastSettings ->
                _state.update {
                    SettingState(
                        settings = lastSettings
                    )
                }
            }
        }
    }

    fun onSaveSettings(questionCount: Int, difficulty: Difficulty, category: Category) {
        viewModelScope.launch {
            val newSettings = Settings(
                questionCount = questionCount,
                difficulty = difficulty,
                category = category
            )
            settingsRepository.setSettings(newSettings)
        }
    }
}