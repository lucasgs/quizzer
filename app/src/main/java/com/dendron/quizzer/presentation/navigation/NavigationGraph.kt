package com.dendron.quizzer.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dendron.quizzer.presentation.home.HomeScreen
import com.dendron.quizzer.presentation.question.QuestionScreen
import com.dendron.quizzer.presentation.question.QuestionViewModel
import com.dendron.quizzer.presentation.score.ScoreScreen
import com.dendron.quizzer.presentation.settings.SettingScreen

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.HOME.route,
    ) {

        composable(route = Screen.HOME.route) {
            HomeScreen(navController = navController)
        }

        composable(route = Screen.QUESTION.route) {
            val viewModel: QuestionViewModel = hiltViewModel()
            val state = viewModel.state.collectAsStateWithLifecycle()
            if (state.value.gameEnded) {
                LaunchedEffect(Unit) {
                    navController.navigate(Screen.SCORE.route + "/${state.value.score}") {
                        popUpTo(Screen.QUESTION.route) { inclusive = true }
                    }
                }
            } else {
                QuestionScreen(state = state.value, viewModel::onEvent)
            }
        }

        composable(route = Screen.SCORE.route + "/{score}") { navBackStackEntry ->
            ScoreScreen(
                navController = navController,
                score = navBackStackEntry.arguments?.getString("score").toString()
            )
        }

        composable(route = Screen.SETTINGS.route) {
            SettingScreen(navController = navController)
        }
    }
}