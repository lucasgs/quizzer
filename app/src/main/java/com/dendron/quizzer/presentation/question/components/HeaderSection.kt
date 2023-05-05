package com.dendron.quizzer.presentation.question.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dendron.quizzer.presentation.common.VerticalSpace

@Composable
fun HeaderSection(progress: String, text: String) {
    Column(
        verticalArrangement = Arrangement.SpaceAround,
        modifier = Modifier.padding(8.dp)
    ) {
        Text(progress)
        VerticalSpace()
        Divider()
        VerticalSpace()
        Text(text)
    }
}
