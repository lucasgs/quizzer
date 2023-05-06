package com.dendron.quizzer.presentation.question.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.dendron.quizzer.R
import com.dendron.quizzer.presentation.components.VerticalSpace

@Composable
fun HeaderSection(text: String, questionNumber: String, questionCount: String) {
    Column(
        verticalArrangement = Arrangement.SpaceAround,
        modifier = Modifier.padding(8.dp)
    ) {
        Text(
            buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    )
                ) {
                    append(stringResource(R.string.question, questionNumber))
                }
                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    append(" / ")
                    append(questionCount)
                }
            }
        )
        VerticalSpace()
        Divider()
        VerticalSpace()
        Text(
            text,
            style = MaterialTheme.typography.titleLarge
        )
    }
}
