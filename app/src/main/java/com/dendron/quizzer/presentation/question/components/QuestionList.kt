package com.dendron.quizzer.presentation.question.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.text.parseAsHtml
import com.dendron.quizzer.presentation.question.QuestionResult

@Composable
fun AnswersList(
    answers: List<QuestionResult>,
    answerSelected: String,
    showCorrect: Boolean,
    modifier: Modifier = Modifier,
    onSelected: (String) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceAround,
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        answers.forEach { question ->
            AnswerItem(
                text = question.text.parseAsHtml().toString(),
                isSelected = question.text == answerSelected,
                isCorrect = question.isCorrect,
                showCorrect = showCorrect
            ) {
                onSelected(it)
            }
        }
    }
}
