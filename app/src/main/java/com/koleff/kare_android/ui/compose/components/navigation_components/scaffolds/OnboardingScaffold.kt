//package com.koleff.kare_android.ui.compose.components.navigation_components.scaffolds
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.PaddingValues
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.height
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Scaffold
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.Dp
//import androidx.compose.ui.unit.dp
//import com.koleff.kare_android.ui.compose.components.navigation_components.bottom_navigation_bar.BottomNavigationBar
//import com.koleff.kare_android.ui.compose.components.navigation_components.toolbar.Toolbar
//import com.koleff.kare_android.ui.state.AnimatedToolbarState
//
//@Composable
//fun OnboardingScaffold(
//    screenTitle: String,
//    onNavigateBackAction: () -> Unit,
//    modifierPadding: @Composable (paddingValues: PaddingValues) -> Unit
//) {
//    Scaffold(
//        modifier =
//        Modifier.fillMaxSize(),
//        topBar = {
//            OnboardingToolbar(
//                title = screenTitle,
//                onNavigateBackAction = onNavigateBackAction
//            )
//        },
//        bottomBar = {}
//    ) { innerPadding ->
//        modifierPadding(innerPadding)
//    }
//}