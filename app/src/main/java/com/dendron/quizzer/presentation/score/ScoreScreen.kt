package com.dendron.quizzer.presentation.score

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.dendron.quizzer.R
import com.dendron.quizzer.presentation.components.VerticalSpace
import com.dendron.quizzer.presentation.navigation.Screen
import com.dendron.quizzer.presentation.ui.theme.Purple40
import kotlinx.coroutines.launch

@Composable
fun ScoreSection(score: String, modifier: Modifier = Modifier) {
    Card(
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = stringResource(R.string.score),
                fontSize = 30.sp,
                fontStyle = FontStyle.Italic,
                color = Color.Gray,
            )
            VerticalSpace()
            Text(
                text = score,
                fontSize = 30.sp,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold,
                color = Purple40,
            )
        }

    }
}

@Composable
fun ScoreScreen(navController: NavHostController?, score: String) {
    val coroutineScope = rememberCoroutineScope()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxSize(),
    ) {
        ScoreSection(score = score, modifier = Modifier.size(200.dp, 200.dp))
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
}

@Preview
@Composable
fun ScoreScreenPreview() {
    ScoreScreen(navController = null, score = "5")
}
