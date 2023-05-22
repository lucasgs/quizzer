package com.dendron.quizzer.presentation.settings

import com.dendron.quizzer.domain.model.Settings

sealed interface SettingState {
    object Loading: SettingState
    class Success(val data: Settings): SettingState
}
