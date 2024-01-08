package com.koleff.kare_android.ui.compose.navigation

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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.koleff.kare_android.ui.MainScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Toolbar(
    navController: NavHostController,
    title: String = "",
    hasTitle: Boolean = true,
    isNavigationInProgress: MutableState<Boolean>
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            if (hasTitle) {
                Text(
                    text = title,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )
            }
        },
        navigationIcon = {
            NavigationItem(
                navController = navController,
                screen = null, //Backstack pop
                icon = Icons.Filled.ArrowBack,
                label = "Go back",
                isBlocked = isNavigationInProgress
            )
        },
        actions = {
            NavigationItem(
                navController = navController,
                screen = MainScreen.Settings,
                icon = Icons.Filled.Settings,
                label = "Settings",
                isBlocked = isNavigationInProgress
            )
        },
        scrollBehavior = scrollBehavior
    )
}

@Preview
@Composable
fun ToolbarPreview() {
    val navController = rememberNavController()

    Toolbar(
        navController = navController,
        isNavigationInProgress = mutableStateOf(false)
    )
}