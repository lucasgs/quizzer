package com.dendron.quizzer.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun QuestionScreen(viewModel: QuestionViewModel = hiltViewModel()) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val question = state.value.currentQuestion
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)
    ) {
        question?.let {
            Column() {
                Text("Question")
                Text(it.text)
                Spacer(modifier = Modifier.height(12.dp))
                (it.incorrectAnswer + it.correctAnswer).shuffled().let {
                    it.forEach { text ->
                        Text(text)
                    }
                }
            }
        }
    }
}