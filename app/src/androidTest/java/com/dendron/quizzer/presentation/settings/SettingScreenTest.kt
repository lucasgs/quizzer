package com.dendron.quizzer.presentation.settings

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test
import com.dendron.quizzer.domain.model.Category
import com.dendron.quizzer.domain.model.Difficulty
import com.dendron.quizzer.domain.model.Settings

class SettingScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun save_is_disabled_when_nothing_changed() {
        composeTestRule.setContent {
            MaterialTheme {
                SettingContent(
                    uiState = SettingState(
                        isLoading = false,
                        settings = Settings(
                            questionCount = 10,
                            difficulty = Difficulty.Any,
                            category = Category.Any,
                        ),
                        questionCount = 10,
                        difficulty = Difficulty.Any,
                        category = Category.Any,
                    ),
                    onQuestionCountChanged = {},
                    onDifficultyChanged = {},
                    onCategoryChanged = {},
                    onBack = {},
                    onSave = {},
                )
            }
        }

        composeTestRule.onNodeWithText("Save").assertIsNotEnabled()
    }

    @Test
    fun selecting_difficulty_updates_summary_and_enables_save() {
        composeTestRule.setContent {
            MaterialTheme {
                var uiState by mutableStateOf(
                    SettingState(
                        isLoading = false,
                        settings = Settings(
                            questionCount = 10,
                            difficulty = Difficulty.Any,
                            category = Category.Any,
                        ),
                        questionCount = 10,
                        difficulty = Difficulty.Any,
                        category = Category.Any,
                    )
                )

                SettingContent(
                    uiState = uiState,
                    onQuestionCountChanged = {
                        uiState = uiState.copy(questionCount = it)
                    },
                    onDifficultyChanged = {
                        uiState = uiState.copy(difficulty = it)
                    },
                    onCategoryChanged = {
                        uiState = uiState.copy(category = it)
                    },
                    onBack = {},
                    onSave = {},
                )
            }
        }

        composeTestRule.onNodeWithText("Which difficulty?").performClick()
        composeTestRule.onNodeWithText("Hard").performClick()

        composeTestRule.onNodeWithText("10 questions · Hard difficulty · Any category").assertIsDisplayed()
        composeTestRule.onNodeWithText("Save").assertIsEnabled()
    }
}
