package com.dendron.quizzer.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dendron.quizzer.common.Constants
import com.dendron.quizzer.domain.model.Category
import com.dendron.quizzer.domain.model.Difficulty
import com.dendron.quizzer.domain.model.Settings
import com.dendron.quizzer.domain.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
) : ViewModel() {

    private val _settings =
        MutableStateFlow(Settings(Constants.QUESTION_COUNT, Difficulty.Any, Category.Any))
    val settings: StateFlow<Settings> = _settings

    init {
        viewModelScope.launch {
            settingsRepository.getSettings().onEach {
                _settings.update {
                    it
                }
            }.launchIn(viewModelScope)
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