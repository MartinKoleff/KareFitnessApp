package com.koleff.kare_android.ui.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

@Composable
fun LoadingWheel(
    modifier: Modifier = Modifier.fillMaxSize(),
    innerPadding: PaddingValues = PaddingValues(8.dp),
    hideScreen: Boolean = false,
    customBackground: Color = Color.Transparent
) {
    val background = if (hideScreen) {
        MaterialTheme.colorScheme.background //Dark theme applied too
    } else customBackground

    Box(
        modifier = modifier
            .padding(innerPadding)
            .background(background),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(50.dp),
            color = MaterialTheme.colorScheme.primary
        )
    }
}