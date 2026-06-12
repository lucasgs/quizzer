package com.dendron.quizzer.presentation.score

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test

class ScoreScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun score_screen_shows_summary_message_and_actions() {
        composeTestRule.setContent {
            MaterialTheme {
                ScoreScreen(
                    navController = null,
                    score = "700",
                    questionCount = "10",
                )
            }
        }

        composeTestRule.onNodeWithText("700").assertIsDisplayed()
        composeTestRule.onNodeWithText("7 of 10 correct · 70%").assertIsDisplayed()
        composeTestRule.onNodeWithText("Great job — you were on a roll.").assertIsDisplayed()
        composeTestRule.onNodeWithText("Home").assertIsDisplayed()
        composeTestRule.onNodeWithText("Play again").assertIsDisplayed()
    }
}
