package com.dendron.quizzer.presentation.question.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun InfoMessage(message: String, modifier: Modifier = Modifier, color: Color = Color.Red) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Surface(
            color = color.copy(alpha = 0.12f),
            shape = MaterialTheme.shapes.large,
            modifier = Modifier.align(Alignment.Center)
        ) {
            Text(
                text = message,
                color = color,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            )
        }
    }
}