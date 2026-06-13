package com.dendron.quizzer.presentation.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.dendron.quizzer.R
import com.dendron.quizzer.presentation.components.VerticalSpace
import com.dendron.quizzer.presentation.navigation.Screen
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val coroutineScope = rememberCoroutineScope()
    val state by viewModel.state.collectAsStateWithLifecycle()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .weight(1f)
                .widthIn(max = 560.dp)
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(28.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(horizontal = 20.dp, vertical = 10.dp)
            ) {
                Text(
                    text = stringResource(R.string.home_tagline_badge),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                )
            }
            VerticalSpace()
            Text(
                text = stringResource(R.string.app_name),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.ExtraBold,
            )
            VerticalSpace()
            Text(
                text = stringResource(R.string.home_subtitle),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleMedium,
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
            VerticalSpace()
            AnimatedVisibility(
                visible = state.hasHistory,
                enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 }),
            ) {
                StatsSection(state = state)
            }
        }
        SettingsIconButton {
            navController.navigate(Screen.SETTINGS.route)
        }
    }
}

@Composable
private fun StatsSection(state: HomeUiState) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp)
    ) {
        Text(
            text = stringResource(R.string.home_stats_title),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
        )
        VerticalSpace()
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            StatCard(
                title = stringResource(R.string.home_best_score),
                value = state.bestScore,
                modifier = Modifier.weight(1f),
            )
            StatCard(
                title = stringResource(R.string.home_best_accuracy),
                value = state.bestAccuracy,
                modifier = Modifier.weight(1f),
            )
        }
        VerticalSpace()
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            StatCard(
                title = stringResource(R.string.home_current_streak),
                value = state.currentStreak,
                modifier = Modifier.weight(1f),
            )
            StatCard(
                title = stringResource(R.string.home_best_streak),
                value = state.bestStreak,
                modifier = Modifier.weight(1f),
            )
        }
        VerticalSpace()
        StatCard(
            title = stringResource(R.string.home_last_round),
            value = state.lastRound,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun StatCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = modifier,
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            VerticalSpace()
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SettingsIconButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier.padding(bottom = 20.dp)
    ) {
        FilledIconButton(
            onClick = { onClick() },
            modifier = Modifier
                .semantics { testTagsAsResourceId = true }
                .testTag("SettingsButton"),
        ) {
            Icon(Icons.Outlined.Settings, contentDescription = null)
        }
    }
}
