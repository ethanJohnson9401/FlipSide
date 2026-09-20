package com.ethanjohnson.flipside

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.tooling.preview.Preview
import com.ethanjohnson.flipside.screen.home.HomeScreen
import com.ethanjohnson.flipside.ui.theme.FlipSideTheme

@Composable
@Preview
fun App() {
    FlipSideTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            HomeScreen()
        }
    }
}