package com.dendron.quizzer.presentation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun QuestionScreen(viewModel: QuestionViewModel = hiltViewModel()) {
    Text("Question sample")
}