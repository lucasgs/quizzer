@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)

package com.dendron.quizzer.presentation.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.dendron.quizzer.R
import com.dendron.quizzer.domain.model.Category
import com.dendron.quizzer.domain.model.Difficulty
import com.dendron.quizzer.presentation.components.MainLayout
import com.dendron.quizzer.presentation.navigation.Screen
import kotlinx.coroutines.launch

@Composable
fun SettingScreen(navController: NavHostController, viewModel: SettingViewModel = hiltViewModel()) {
    val coroutineScope = rememberCoroutineScope()
    val state = viewModel.state.collectAsStateWithLifecycle()

    fun navigateToHomeScreen() {
        coroutineScope.launch {
            navController.navigate(Screen.HOME.route)
        }
    }

    val uiState = state.value

    if (!uiState.isLoading) {
        MainLayout(showBackground = false, bottomBar = {
            SettingActionSection(
                canSave = uiState.hasChanges,
                onBack = { navigateToHomeScreen() },
                onSave = {
                    viewModel.onSaveSettings()
                    navigateToHomeScreen()
                }
            )
        }) {
            Column(
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
            ) {
                QuestionCountSection(questionCount = uiState.questionCount.toFloat()) { newValue ->
                    viewModel.onQuestionCountChanged(newValue.toInt())
                }
                DifficultySection(difficulty = uiState.difficulty) { newValue ->
                    viewModel.onDifficultyChanged(newValue)
                }
                CategorySection(category = uiState.category) { newValue ->
                    viewModel.onCategoryChanged(newValue)
                }
            }
        }
    }
}

@Composable
fun QuestionCountSection(questionCount: Float, onValueChange: (Float) -> Unit) {
    Card(
        modifier = Modifier.padding(10.dp)
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.how_many_questions))
                Text(questionCount.toInt().toString())
            }
            Slider(
                value = questionCount,
                onValueChange = { onValueChange(it) },
                valueRange = 1f..20f,
                steps = 20,
            )
        }
    }
}

@Composable
fun DifficultySection(difficulty: Difficulty, onValueChange: (Difficulty) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier.padding(10.dp)
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()
            ) {
                ClickableText(text = AnnotatedString(stringResource(R.string.which_difficulty)),
                    onClick = { expanded = true })
                ClickableText(
                    text = AnnotatedString(difficulty.toString()),
                    onClick = { expanded = true })
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                Difficulty.values().forEach {
                    DropdownMenuItem(text = { Text(it.name) }, onClick = {
                        onValueChange(it)
                        expanded = false
                    })
                }
            }
        }
    }
}

fun splitAndCapitalizeWords(origin: String): String {
    val words = mutableListOf<String>()
    val currentWord = StringBuilder()

    for (i in origin.indices) {
        val currentChar = origin[i]

        if (currentChar.isUpperCase() && currentWord.isNotEmpty()) {
            words.add(currentWord.toString())
            currentWord.clear()
        }

        currentWord.append(currentChar)
    }

    if (currentWord.isNotEmpty()) {
        words.add(currentWord.toString())
    }

    return words.joinToString(" ") { it.replaceFirstChar { letter -> letter.uppercase() } }
}

@Composable
fun CategorySection(category: Category, onValueChange: (Category) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val selectedText = splitAndCapitalizeWords(category.name)
    Card(
        modifier = Modifier.padding(10.dp)
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            FlowRow(
                horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()
            ) {
                ClickableText(text = AnnotatedString(stringResource(R.string.which_category)),
                    onClick = { expanded = true })
                ClickableText(
                    text = AnnotatedString(selectedText),
                    onClick = { expanded = true },
                )
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                Category.values().forEach {
                    val name = splitAndCapitalizeWords(it.name)
                    DropdownMenuItem(text = { Text(name) }, onClick = {
                        onValueChange(it)
                        expanded = false
                    })
                }
            }
        }
    }
}

@Composable
fun SettingActionSection(canSave: Boolean, onBack: () -> Unit, onSave: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.SpaceAround, modifier = Modifier.fillMaxWidth()
    ) {
        Button(onClick = {
            onBack()
        }) {
            Text(stringResource(R.string.close))
        }
        Button(
            onClick = { onSave() },
            enabled = canSave,
        ) {
            Text(stringResource(R.string.save))
        }
    }
}
