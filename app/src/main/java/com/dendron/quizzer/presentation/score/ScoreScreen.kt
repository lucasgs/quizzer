package com.dendron.quizzer.presentation.score

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
fun ScoreSection(score: String, questionCount: String, modifier: Modifier = Modifier) {
    val scoreValue = score.toIntOrNull() ?: 0
    val questionCountValue = questionCount.toIntOrNull() ?: 0
    val correctAnswers = if (questionCountValue == 0) 0 else scoreValue / 100
    val percentage = if (questionCountValue == 0) 0 else ((correctAnswers * 100f) / questionCountValue).toInt()
    val performanceMessage = when {
        percentage >= 90 -> stringResource(R.string.score_message_excellent)
        percentage >= 70 -> stringResource(R.string.score_message_great)
        percentage >= 40 -> stringResource(R.string.score_message_good)
        else -> stringResource(R.string.score_message_try_again)
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Card(
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
            modifier = modifier
                .padding(24.dp)
                .align(Alignment.Center)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(24.dp)
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
                VerticalSpace()
                Text(
                    text = stringResource(R.string.score_summary, correctAnswers, questionCountValue, percentage),
                    style = MaterialTheme.typography.titleMedium,
                )
                VerticalSpace()
                Text(
                    text = performanceMessage,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.secondary,
                )
                VerticalSpace()
                BottomDecoration()
            }
        }
    }
}

@Composable
fun BottomDecoration(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.inversePrimary,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier.fillMaxWidth()
        ) {
            Box(
                modifier = modifier
                    .background(color)
                    .height(3.dp)
                    .weight(2f)
            ) {}
            Icon(
                tint = color,
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                modifier = Modifier
                    .size(16.dp)
                    .weight(1f)
            )
            Box(
                modifier = modifier
                    .background(color)
                    .height(3.dp)
                    .weight(2f)
            ) {}
        }
        Spacer(modifier = modifier.height(5.dp))
        Box(
            modifier = modifier
                .align(Alignment.CenterHorizontally)
                .background(color)
                .height(3.dp)
                .width(140.dp)
        ) {}
    }
}

@Composable
fun ActionSection(navController: NavHostController?, coroutineScope: CoroutineScope) {
    Row(
        horizontalArrangement = Arrangement.SpaceAround, modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedButton(onClick = {
            coroutineScope.launch {
                navController?.navigate(Screen.HOME.route) {
                    popUpTo(Screen.HOME.route)
                }
            }
        }) {
            Text(stringResource(R.string.home))
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
fun ScoreScreen(navController: NavHostController?, score: String, questionCount: String) {
    val coroutineScope = rememberCoroutineScope()
    MainLayout(showBackground = true, bottomBar = {
        ActionSection(navController = navController, coroutineScope = coroutineScope)
    }) {
        ScoreSection(
            score = score,
            questionCount = questionCount,
        )
    }
}

@Preview
@Composable
fun ScoreScreenPreview() {
    ScoreScreen(navController = null, score = "500", questionCount = "10")
}
