package com.dendron.quizzer.presentation.question.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnswerItem(
    text: String, selected: Boolean, modifier: Modifier = Modifier, onClick: (String) -> Unit
) {
    Box(
        modifier = modifier
            .padding(top = 8.dp)
    ) {
        ElevatedFilterChip(onClick = { onClick(text) },
            selected = false,
            label = { Text(text) },
            colors = FilterChipDefaults.elevatedFilterChipColors(
                selectedLabelColor = Color.Green,
                selectedContainerColor = Color.Green,
                selectedTrailingIconColor = Color.Green
            ),
            trailingIcon = if (selected) {
                {
                    Icon(
                        imageVector = Icons.Filled.Done,
                        contentDescription = null,
                        modifier = Modifier.size(FilterChipDefaults.IconSize)
                    )
                }
            } else {
                null
            },
            modifier = Modifier
        )
    }
}
