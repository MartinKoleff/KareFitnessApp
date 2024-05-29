package com.koleff.kare_android.ui.compose.components.navigation_components.toolbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.koleff.kare_android.R
import com.koleff.kare_android.ui.compose.components.navigation_components.NavigationItem

//TODO: timer from workout start...
//TODO: add toolbar with x and progress lines of how much exercises are there (and mark the passed ones and current one bold)...
// show percentage completed workout...
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoWorkoutToolbar(
    title: String = "",
    onExitWorkoutAction: () -> Unit,
    onNextExerciseAction: () -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    val tintColor = MaterialTheme.colorScheme.onSurface
    val textColor = MaterialTheme.colorScheme.onSurface

    val textStyle = MaterialTheme.typography.titleLarge.copy(
        color = textColor
    )

    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = textColor,
        ),
        title = {
            Text(
                text = title,
                style = textStyle,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
        },
        navigationIcon = {
            NavigationItem(
                icon = painterResource(id = R.drawable.ic_vector_close),
                label = "Exit workout",
                onNavigateAction = onExitWorkoutAction,
                tint = tintColor
            )
        },
        actions = {
            NavigationItem(
                icon = painterResource(id = R.drawable.ic_forward),
                label = "Go to next exercise",
                onNavigateAction = onNextExerciseAction,
                tint = tintColor
            )
        },
        scrollBehavior = scrollBehavior
    )
}

@Preview
@Composable
fun DoWorkoutToolbarPreview() {
    DoWorkoutToolbar(
        onExitWorkoutAction = {},
        onNextExerciseAction = {}
    )
}