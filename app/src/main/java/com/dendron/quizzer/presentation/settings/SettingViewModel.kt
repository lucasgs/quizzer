package com.dendron.quizzer.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dendron.quizzer.domain.model.Category
import com.dendron.quizzer.domain.model.Difficulty
import com.dendron.quizzer.domain.model.Settings
import com.dendron.quizzer.domain.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
) : ViewModel() {

    val state: StateFlow<SettingState> =
        settingsRepository.getSettings().map { result -> SettingState.Success(result) }.stateIn(
            scope = viewModelScope,
            initialValue = SettingState.Loading,
            started = SharingStarted.WhileSubscribed(5_000)
        )

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