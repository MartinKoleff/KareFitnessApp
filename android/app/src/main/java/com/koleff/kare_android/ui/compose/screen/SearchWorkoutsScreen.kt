package com.koleff.kare_android.ui.compose.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.koleff.kare_android.ui.compose.scaffolds.SearchWorkoutsScaffold

@Composable
fun SearchWorkoutsScreen(
    navController: NavHostController,
    isNavigationInProgress: MutableState<Boolean>
) {
    SearchWorkoutsScaffold("Select workout", navController, isNavigationInProgress) { innerPadding ->
        val modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()

        SearchWorkoutsContent(modifier = modifier)
    }
}

@Composable
fun SearchWorkoutsContent(modifier: Modifier) {

}
