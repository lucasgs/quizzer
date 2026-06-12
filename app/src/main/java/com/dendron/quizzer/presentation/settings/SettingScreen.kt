@file:OptIn(ExperimentalMaterial3Api::class)

package com.dendron.quizzer.presentation.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
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
    val uiState = viewModel.state.collectAsStateWithLifecycle().value

    fun navigateToHomeScreen() {
        coroutineScope.launch {
            navController.navigate(Screen.HOME.route)
        }
    }

    if (!uiState.isLoading) {
        SettingContent(
            uiState = uiState,
            onQuestionCountChanged = { viewModel.onQuestionCountChanged(it) },
            onDifficultyChanged = { viewModel.onDifficultyChanged(it) },
            onCategoryChanged = { viewModel.onCategoryChanged(it) },
            onBack = { navigateToHomeScreen() },
            onSave = {
                viewModel.onSaveSettings()
                navigateToHomeScreen()
            },
        )
    }
}

@Composable
fun SettingContent(
    uiState: SettingState,
    onQuestionCountChanged: (Int) -> Unit,
    onDifficultyChanged: (Difficulty) -> Unit,
    onCategoryChanged: (Category) -> Unit,
    onBack: () -> Unit,
    onSave: () -> Unit,
) {
    MainLayout(showBackground = false, bottomBar = {
        SettingActionSection(
            canSave = uiState.hasChanges,
            onBack = onBack,
            onSave = onSave,
        )
    }) {
        Column(
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            QuestionCountSection(
                questionCount = uiState.questionCount,
                onValueChange = onQuestionCountChanged,
            )
            DifficultySection(
                difficulty = uiState.difficulty,
                onValueChange = onDifficultyChanged,
            )
            CategorySection(
                category = uiState.category,
                onValueChange = onCategoryChanged,
            )
            QuizSetupSummarySection(
                questionCount = uiState.questionCount,
                difficulty = uiState.difficulty,
                category = uiState.category,
            )
        }
    }
}

@Composable
fun QuestionCountSection(questionCount: Int, onValueChange: (Int) -> Unit) {
    val valueDescription = stringResource(R.string.question_count_value_description, questionCount)
    val sliderDescription = stringResource(R.string.question_count_slider_description)

    Card(modifier = Modifier.padding(10.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.how_many_questions))
                Text(
                    text = questionCount.toString(),
                    modifier = Modifier.semantics {
                        contentDescription = valueDescription
                    }
                )
            }
            Slider(
                value = questionCount.toFloat(),
                onValueChange = { onValueChange(it.toInt()) },
                valueRange = 1f..20f,
                steps = 18,
                modifier = Modifier.semantics {
                    contentDescription = sliderDescription
                }
            )
            Text(
                text = stringResource(R.string.question_count_helper),
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
fun DifficultySection(difficulty: Difficulty, onValueChange: (Difficulty) -> Unit) {
    DropdownSettingField(
        label = stringResource(R.string.which_difficulty),
        value = difficulty.displayName(),
        options = Difficulty.entries.map { option -> option to option.displayName() },
        onValueChange = onValueChange,
    )
}

@Composable
fun CategorySection(category: Category, onValueChange: (Category) -> Unit) {
    DropdownSettingField(
        label = stringResource(R.string.which_category),
        value = category.displayName(),
        options = Category.entries.map { option -> option to option.displayName() },
        onValueChange = onValueChange,
    )
}

@Composable
private fun <T> DropdownSettingField(
    label: String,
    value: String,
    options: List<Pair<T, String>>,
    onValueChange: (T) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Card(modifier = Modifier.padding(10.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = it },
            ) {
                OutlinedTextField(
                    value = value,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(label) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable, enabled = true),
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    options.forEach { (option, text) ->
                        DropdownMenuItem(
                            text = { Text(text) },
                            onClick = {
                                onValueChange(option)
                                expanded = false
                            },
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun QuizSetupSummarySection(
    questionCount: Int,
    difficulty: Difficulty,
    category: Category,
) {
    Card(modifier = Modifier.padding(10.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(stringResource(R.string.quiz_setup_summary_title))
            Text(
                text = stringResource(
                    R.string.quiz_setup_summary_value,
                    questionCount,
                    difficulty.displayName(),
                    category.displayName(),
                ),
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(
                text = stringResource(R.string.quiz_setup_summary_helper),
                modifier = Modifier.padding(top = 4.dp)
            )
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

private fun Difficulty.displayName(): String = splitAndCapitalizeWords(name)

private fun Category.displayName(): String = splitAndCapitalizeWords(name)

@Composable
fun SettingActionSection(canSave: Boolean, onBack: () -> Unit, onSave: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(onClick = onBack) {
            Text(stringResource(R.string.close))
        }
        Button(
            onClick = onSave,
            enabled = canSave,
        ) {
            Text(stringResource(R.string.save))
        }
    }
}
