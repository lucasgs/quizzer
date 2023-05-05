package com.dendron.quizzer.presentation.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun VerticalSpace(modifier: Modifier = Modifier) {
    Spacer(modifier = modifier.height(20.dp))
}