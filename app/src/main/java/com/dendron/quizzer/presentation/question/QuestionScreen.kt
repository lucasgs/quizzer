package com.dendron.quizzer.presentation.question

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.text.parseAsHtml
import com.dendron.quizzer.R
import com.dendron.quizzer.presentation.components.MainLayout
import com.dendron.quizzer.presentation.components.VerticalSpace
import com.dendron.quizzer.presentation.question.components.AnswersList
import com.dendron.quizzer.presentation.question.components.ErrorMessage
import com.dendron.quizzer.presentation.question.components.HeaderSection
import com.dendron.quizzer.presentation.question.components.InfoMessage
import com.dendron.quizzer.presentation.question.components.QuestionActions

@Composable
fun QuestionScreen(
    state: QuestionState,
    onEvent: (QuestionListEvent) -> Unit,
) {
    val isLoading = state.isLoading
    val scrollState = rememberScrollState()
    val errorMessage = when (val error = state.error) {
        QuestionUiError.None -> null
        QuestionUiError.NoAnswerSelected -> stringResource(R.string.please_select_an_answer)
        QuestionUiError.EmptyQuestions -> stringResource(R.string.no_questions_matching_filters)
        is QuestionUiError.LoadingFailed -> error.message
    }
    val answerFeedback = when (val result = state.answerResult) {
        is AnswerResult.Correct -> result.message
        is AnswerResult.Incorrect -> result.message
        AnswerResult.None -> null
    }
    val showRetry = state.error is QuestionUiError.EmptyQuestions || state.error is QuestionUiError.LoadingFailed
    val actionText = when {
        state.answerResult == AnswerResult.None -> stringResource(R.string.check_answer)
        state.questionNumber == state.questionCount -> stringResource(R.string.finish)
        else -> stringResource(R.string.next_question)
    }
    val actionEnabled = state.answer.isNotEmpty() || state.answerResult != AnswerResult.None

    LaunchedEffect(state.questionNumber) {
        scrollState.animateScrollTo(0)
    }

    MainLayout(
        bottomBar = {
            AnimatedVisibility(visible = !isLoading && state.question.isNotEmpty()) {
                QuestionActions(
                    text = actionText,
                    enabled = actionEnabled,
                ) {
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
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(30.dp)
                ) {
                    HeaderSection(
                        text = state.question.parseAsHtml().toString(),
                        questionCount = state.questionCount,
                        questionNumber = state.questionNumber,
                        category = state.category,
                        difficulty = state.difficulty,
                    )
                    VerticalSpace()
                    AnswersList(
                        answers = state.answers,
                        answerSelected = state.answer,
                        showCorrect = (state.answerResult != AnswerResult.None)
                    ) { selected ->
                        onEvent(QuestionListEvent.SetAnswer(answer = selected))
                    }
                    if (answerFeedback != null) {
                        VerticalSpace()
                        InfoMessage(
                            message = answerFeedback,
                            color = if (state.answerResult is AnswerResult.Correct) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.error
                            }
                        )
                    }
                }
            }
            if (errorMessage != null) {
                ErrorMessage(
                    errorMessage, color = MaterialTheme.colorScheme.secondary
                )
                if (showRetry) {
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Button(
                            onClick = { onEvent(QuestionListEvent.RetryLoad) },
                            modifier = Modifier.align(Alignment.TopCenter)
                        ) {
                            Text(stringResource(R.string.retry))
                        }
                    }
                }
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