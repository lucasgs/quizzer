package com.dendron.quizzer.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dendron.quizzer.domain.model.Settings
import com.dendron.quizzer.domain.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
) : ViewModel() {

    fun onSaveSettings(questionCount: Int, categories: List<Int>) {
        viewModelScope.launch {
            val newSettings = Settings(
                questionCount = questionCount,
                categories = categories
            )
            settingsRepository.setSettings(newSettings)
        }
    }
}