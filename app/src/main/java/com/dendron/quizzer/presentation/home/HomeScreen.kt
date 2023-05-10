package com.dendron.quizzer.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.dendron.quizzer.R
import com.dendron.quizzer.presentation.components.VerticalSpace
import com.dendron.quizzer.presentation.navigation.Screen
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(navController: NavHostController) {
    val coroutineScope = rememberCoroutineScope()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            stringResource(R.string.app_name),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.headlineLarge,
        )
        VerticalSpace()
        VerticalSpace()
        Button(onClick = {
            coroutineScope.launch {
                navController.navigate(Screen.QUESTION.route)
            }
        }) {
            Text(stringResource(R.string.start))
        }
    }
}