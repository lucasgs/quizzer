package com.dendron.quizzer.presentation.question

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.text.parseAsHtml
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.dendron.quizzer.presentation.components.MainLayout
import com.dendron.quizzer.presentation.components.VerticalSpace
import com.dendron.quizzer.presentation.navigation.Screen
import com.dendron.quizzer.presentation.question.components.AnswersList
import com.dendron.quizzer.presentation.question.components.ErrorMessage
import com.dendron.quizzer.presentation.question.components.HeaderSection
import com.dendron.quizzer.presentation.question.components.QuestionActions

@Composable
fun QuestionScreen(
    navController: NavHostController, viewModel: QuestionViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val answerState = viewModel.answer.collectAsStateWithLifecycle()
    val errorState = viewModel.error.collectAsStateWithLifecycle()
    val loadingState = viewModel.loading.collectAsStateWithLifecycle()
    val gameEnded = viewModel.gameEnded.collectAsStateWithLifecycle()
    val answerResultState = viewModel.answerResult.collectAsStateWithLifecycle()
    val value = state.value

    if (gameEnded.value) {
        LaunchedEffect(gameEnded) {
            navController.navigate(Screen.SCORE.route + "/${value.score}") {
                popUpTo(Screen.QUESTION.route) { inclusive = true }
            }
        }
    }

    MainLayout(
        bottomBar = {
            AnimatedVisibility(visible = !loadingState.value) {
                QuestionActions() {
                    viewModel.nextQuestion()
                }
            }
        }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            AnimatedVisibility(visible = !loadingState.value) {
                Column(
                    modifier = Modifier
                        .padding(30.dp)
                ) {
                    HeaderSection(
                        text = value.question.parseAsHtml().toString(),
                        questionCount = value.questionCount,
                        questionNumber = value.questionNumber
                    )
                    VerticalSpace()
                    AnswersList(
                        answers = value.answers,
                        answerSelected = answerState.value,
                        showCorrect = answerResultState.value != AnswerResult.None
                    ) { selected ->
                        viewModel.setAnswer(selected)
                    }
                }
            }
            if (errorState.value.isNotEmpty()) {
                ErrorMessage(
                    errorState.value, color = MaterialTheme.colorScheme.secondary
                )
            }
            if (loadingState.value) {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}