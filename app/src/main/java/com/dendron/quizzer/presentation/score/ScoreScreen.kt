package com.dendron.quizzer.presentation.score

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.dendron.quizzer.R
import com.dendron.quizzer.presentation.components.MainLayout
import com.dendron.quizzer.presentation.components.VerticalSpace
import com.dendron.quizzer.presentation.navigation.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ScoreSection(score: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Card(
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
            modifier = modifier
                .size(200.dp, 200.dp)
                .align(Alignment.Center)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = stringResource(R.string.score),
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.secondary,
                )
                VerticalSpace()
                Text(
                    text = score,
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold,
                )
            }

        }
    }
}

@Composable
fun ActionSection(navController: NavHostController?, coroutineScope: CoroutineScope) {
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Button(onClick = {
            coroutineScope.launch {
                navController?.navigate(Screen.HOME.route) {
                    popUpTo(Screen.HOME.route)
                }
            }
        }) {
            Text(stringResource(R.string.close))
        }
        Button(onClick = {
            coroutineScope.launch {
                navController?.navigate(Screen.QUESTION.route) {
                    popUpTo(Screen.HOME.route)
                }
            }
        }) {
            Text(stringResource(R.string.play_again))
        }
    }

}

@Composable
fun ScoreScreen(navController: NavHostController?, score: String) {
    val coroutineScope = rememberCoroutineScope()
    MainLayout(bottomBar = {
        ActionSection(navController = navController, coroutineScope = coroutineScope)
    }) {
        ScoreSection(
            score = score,
        )
    }
}

@Preview
@Composable
fun ScoreScreenPreview() {
    ScoreScreen(navController = null, score = "5")
}
