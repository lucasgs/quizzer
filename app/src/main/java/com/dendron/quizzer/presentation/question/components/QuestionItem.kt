package com.dendron.quizzer.presentation.question.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun AnswerItem(
    text: String,
    isSelected: Boolean,
    isCorrect: Boolean,
    showCorrect: Boolean,
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit
) {
    val iconColor = MaterialTheme.colorScheme.primary
    val textColor = if (isSelected || isSystemInDarkTheme()) Color.White else Color.Black

    Box(
        modifier = modifier.padding(top = 8.dp)
    ) {

        ElevatedFilterChip(
            onClick = { onClick(text) },
            selected = isSelected,
            label = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = text,
                        textAlign = TextAlign.Center,
                        softWrap = true,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .semantics { testTagsAsResourceId = true }
                            .testTag("Option"),
                    )
                    if (showCorrect && (isCorrect || isSelected)) {
                        Icon(
                            imageVector = if (isCorrect) Icons.Filled.Done else Icons.Filled.Close,
                            contentDescription = null,
                            modifier = Modifier
                                .size(FilterChipDefaults.IconSize)
                                .align(Alignment.CenterEnd)
                                .padding(2.dp)
                        )

                    }
                }
            },
            colors = FilterChipDefaults.elevatedFilterChipColors(
                labelColor = textColor,
                iconColor = iconColor,
            ),

            modifier = Modifier
                .fillMaxWidth()
        )
    }
}
