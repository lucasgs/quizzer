package com.dendron.quizzer.presentation.question

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

class QuestionScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun check_answer_flow_updates_button_state_and_feedback() {
        composeTestRule.setContent {
            MaterialTheme {
                var state by mutableStateOf(
                    QuestionState(
                        question = "What is the right answer?",
                        questionNumber = "1",
                        questionCount = "1",
                        category = "Science",
                        difficulty = "Easy",
                        answers = listOf(
                            QuestionResult(text = "A", isCorrect = true),
                            QuestionResult(text = "B", isCorrect = false),
                        ),
                    )
                )

                QuestionScreen(state = state) { event ->
                    state = when (event) {
                        is QuestionListEvent.SetAnswer -> state.copy(answer = event.answer)
                        QuestionListEvent.NextQuestion -> {
                            if (state.answerResult == AnswerResult.None) {
                                if (state.answer == "A") {
                                    state.copy(answerResult = AnswerResult.Correct("Correct! Great job."))
                                } else {
                                    state.copy(answerResult = AnswerResult.Incorrect("Incorrect. The right answer is A"))
                                }
                            } else {
                                state.copy(gameEnded = true)
                            }
                        }
                        QuestionListEvent.RetryLoad -> state
                    }
                }
            }
        }

        composeTestRule.onNodeWithText("Check answer").assertIsNotEnabled()
        composeTestRule.onNodeWithText("A").performClick()
        composeTestRule.onNodeWithText("Check answer").assertIsEnabled().performClick()
        composeTestRule.onNodeWithText("Correct! Great job.").assertIsDisplayed()
        composeTestRule.onNodeWithText("Finish").assertIsDisplayed()
    }

    @Test
    fun retry_is_shown_for_loading_errors() {
        composeTestRule.setContent {
            MaterialTheme {
                QuestionScreen(
                    state = QuestionState(
                        error = QuestionUiError.LoadingFailed("Network down")
                    ),
                    onEvent = {},
                )
            }
        }

        composeTestRule.onNodeWithText("Network down").assertIsDisplayed()
        composeTestRule.onNodeWithText("Retry").assertIsDisplayed()
    }
}
