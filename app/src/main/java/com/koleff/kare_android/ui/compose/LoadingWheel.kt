package com.koleff.kare_android.ui.compose

import androidx.compose.foundation.background
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
fun LoadingWheel(innerPadding: PaddingValues = PaddingValues(0.dp), hideScreen: Boolean = false) {
    val background = if (hideScreen) Color.White else Color.Transparent

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = 8.dp,
                start = 8.dp + innerPadding.calculateStartPadding(LayoutDirection.Rtl),
                end = 8.dp + innerPadding.calculateEndPadding(LayoutDirection.Rtl),
                bottom = 8.dp + innerPadding.calculateBottomPadding()
            )
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