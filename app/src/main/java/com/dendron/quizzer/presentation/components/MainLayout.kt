package com.dendron.quizzer.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.dendron.quizzer.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainLayout(
    showBackground: Boolean = false,
    bottomBar: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit = {}
) {
    Scaffold(
        bottomBar = {
            Box(
                modifier = Modifier
                    .padding(bottom = 30.dp)
            ) {
                bottomBar()
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box() {
            if (showBackground) {
                Image(
                    colorFilter = ColorFilter.tint(
                        MaterialTheme.colorScheme.primary,
                        blendMode = BlendMode.Hue
                    ),
                    contentScale = ContentScale.FillHeight,
                    alpha = 0.5f,
                    painter = painterResource(id = R.drawable.backgroud_star),
                    contentDescription = null,
                    modifier = Modifier.fillMaxHeight()
                )
            }
            content(innerPadding)
        }
    }
}