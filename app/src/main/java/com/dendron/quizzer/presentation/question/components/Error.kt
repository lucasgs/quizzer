package com.dendron.quizzer.presentation.question.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ErrorMessage(message: String, modifier: Modifier = Modifier, color: Color = Color.Red) {
    Box(
        modifier = modifier
            .padding(2.dp)
    ) {
        Text(
            text = message,
            color = color,
            fontSize = 12.sp,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.Bold,
        )
    }
}