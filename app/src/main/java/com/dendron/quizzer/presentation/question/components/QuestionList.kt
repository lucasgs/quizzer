package com.dendron.quizzer.presentation.question.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AnswersList(
    answers: List<String>,
    answerSelected: String,
    modifier: Modifier = Modifier,
    onSelected: (String) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceAround,
        modifier = modifier
            .padding(8.dp)
    ) {
        answers.forEach { text ->
            AnswerItem(text = text, text == answerSelected) {
                onSelected(it)
            }
        }
    }
}
