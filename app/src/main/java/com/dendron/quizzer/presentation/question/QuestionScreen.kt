package com.dendron.quizzer.presentation.question

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.text.parseAsHtml
import com.dendron.quizzer.presentation.components.MainLayout
import com.dendron.quizzer.presentation.components.VerticalSpace
import com.dendron.quizzer.presentation.question.components.AnswersList
import com.dendron.quizzer.presentation.question.components.ErrorMessage
import com.dendron.quizzer.presentation.question.components.HeaderSection
import com.dendron.quizzer.presentation.question.components.QuestionActions

@Composable
fun QuestionScreen(
    state: QuestionState,
    onEvent: (QuestionListEvent) -> Unit,
) {
    val isLoading = state.isLoading

    MainLayout(
        bottomBar = {
            AnimatedVisibility(visible = !isLoading) {
                QuestionActions {
                    onEvent(QuestionListEvent.NextQuestion)
                }
            }
        }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            AnimatedVisibility(visible = !isLoading) {
                Column(
                    modifier = Modifier
                        .padding(30.dp)
                ) {
                    HeaderSection(
                        text = state.question.parseAsHtml().toString(),
                        questionCount = state.questionCount,
                        questionNumber = state.questionNumber
                    )
                    VerticalSpace()
                    AnswersList(
                        answers = state.answers,
                        answerSelected = state.answer,
                        showCorrect = (state.answerResult != AnswerResult.None)
                    ) { selected ->
                        onEvent(QuestionListEvent.SetAnswer(answer = selected))
                    }
                }
            }
            if (state.error.isNotEmpty()) {
                ErrorMessage(
                    state.error, color = MaterialTheme.colorScheme.secondary
                )
            }
            if (state.isLoading) {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}