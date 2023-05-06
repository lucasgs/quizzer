package com.dendron.quizzer.presentation.question.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ErrorMessage(message: String, modifier: Modifier = Modifier, color: Color = Color.Red) {
    Box(
        modifier = modifier
            .padding(8.dp)
    ) {
        Text(
            text = message,
            color = color,
            style = MaterialTheme.typography.bodyLarge,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.Bold,
        )
    }
}