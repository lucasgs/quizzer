package com.dendron.quizzer.presentation.question

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.dendron.quizzer.presentation.navigation.Screen
import com.dendron.quizzer.presentation.question.components.AnswersList
import com.dendron.quizzer.presentation.question.components.HeaderSection
import com.dendron.quizzer.presentation.question.components.QuestionBottom

@Composable
fun QuestionScreen(
    navController: NavHostController,
    viewModel: QuestionViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val answerState = viewModel.answer.collectAsStateWithLifecycle()
    val errorState = viewModel.error.collectAsStateWithLifecycle()
    val gameEnded = viewModel.gameEnded.collectAsStateWithLifecycle()
    val value = state.value

    if (gameEnded.value) {
        LaunchedEffect(gameEnded) {
            navController.navigate(Screen.SCORE.route + "/${value.score}") {
                popUpTo(Screen.QUESTION.route) { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            HeaderSection(progress = value.progress, text = value.question)
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(10.dp)
            ) {
                AnswersList(
                    answers = value.answers,
                    answerSelected = answerState.value
                ) { selected ->
                    viewModel.setAnswer(selected)
                }
                if (errorState.value.isNotEmpty()) {
                    Text(errorState.value, color = Color.Red)
                }
                QuestionBottom() {
                    viewModel.nextQuestion()
                }
            }
        }
    }
}