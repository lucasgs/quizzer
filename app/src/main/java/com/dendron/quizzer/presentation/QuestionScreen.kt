package com.dendron.quizzer.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
            .padding(top = 8.dp)
    ) {
        answers.forEach { text ->
            AnswerItem(text = text, text == answerSelected) {
                onSelected(it)
            }
        }
    }
}

@Composable
fun QuestionBottom(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxWidth()
    ) {
        Button(
            onClick = onClick
        ) {
            Text(text = "Next")
        }
    }
}

@Composable
fun QuestionScreen(viewModel: QuestionViewModel = hiltViewModel()) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val answerState = viewModel.answer.collectAsStateWithLifecycle()
    val errorState = viewModel.error.collectAsStateWithLifecycle()
    val value = state.value
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            HeaderSection(progress = value.progress, text = value.question)
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(10.dp)
            ) {
                AnswersList(
                    answers = value.answers,
                    answerSelected = answerState.value
                ) { selected ->
                    viewModel.setAnswer(selected)
                }
                if (errorState.value.isNotEmpty()) {
                    Text(errorState.value, color = Color.Red)
                }
                QuestionBottom() {
                    viewModel.nextQuestion()
                }
            }
        }
    }
}