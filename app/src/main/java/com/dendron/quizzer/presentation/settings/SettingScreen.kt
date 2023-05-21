package com.dendron.quizzer.presentation.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.dendron.quizzer.R
import com.dendron.quizzer.common.Constants
import com.dendron.quizzer.presentation.components.MainLayout
import com.dendron.quizzer.presentation.navigation.Screen
import kotlinx.coroutines.launch

@Composable
fun SettingScreen(navController: NavHostController, viewModel: SettingViewModel = hiltViewModel()) {
    val coroutineScope = rememberCoroutineScope()
    var questionCount by remember { mutableStateOf(Constants.QUESTION_COUNT) }

    fun navigateToHomeScreen() {
        coroutineScope.launch {
            navController.navigate(Screen.HOME.route)
        }
    }

    MainLayout(showBackground = false, bottomBar = {
        SettingActionSection(onBack = {
            navigateToHomeScreen()
        }, onSave = {
            viewModel.onSaveSettings(questionCount = questionCount, categories = emptyList())
            navigateToHomeScreen()
        })
    }) {
        Column(
            verticalArrangement = Arrangement.Top, modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            QuestionCountSection(questionCount = questionCount.toFloat()) { newValue ->
                questionCount = newValue.toInt()
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
fun SettingActionSection(onBack: () -> Unit, onSave: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.SpaceAround, modifier = Modifier.fillMaxWidth()
    ) {
        Button(onClick = {
            onBack()
        }) {
            Text(stringResource(R.string.close))
        }
        Button(onClick = {
            onSave()
        }) {
            Text(stringResource(R.string.save))
        }
    }
}
