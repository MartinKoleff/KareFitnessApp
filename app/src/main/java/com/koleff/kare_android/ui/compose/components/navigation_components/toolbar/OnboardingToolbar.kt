//package com.koleff.kare_android.ui.compose.components.navigation_components.toolbar
//
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.fillMaxHeight
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.height
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
//import androidx.compose.material.icons.filled.Settings
//import androidx.compose.material3.CenterAlignedTopAppBar
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.material3.TopAppBarDefaults
//import androidx.compose.material3.rememberTopAppBarState
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.alpha
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.text.style.TextOverflow
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.tooling.preview.PreviewLightDark
//import androidx.compose.ui.unit.dp
//import com.koleff.kare_android.ui.compose.components.navigation_components.NavigationItem
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun OnboardingToolbar(
//    modifier: Modifier = Modifier,
//    title: String = "",
//    onNavigateBackAction: () -> Unit
//) {
//    val textColor = MaterialTheme.colorScheme.onSurface
//    val textStyle = MaterialTheme.typography.titleLarge.copy(
//        color = textColor
//    )
//
//    val tintColor = MaterialTheme.colorScheme.onSurface
//
//    CenterAlignedTopAppBar(
//        modifier = modifier,
//        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
//            containerColor = MaterialTheme.colorScheme.primary,
//            titleContentColor = textColor,
//        ),
//        title = {
//            Box(
//                modifier = Modifier
//                    .fillMaxSize(),
//                contentAlignment = Alignment.Center
//            ) {
//                Text(
//                    text = title,
//                    style = textStyle,
//                    maxLines = 2,
//                    overflow = TextOverflow.Ellipsis,
//                    textAlign = TextAlign.Center
//                )
//            }
//        },
//        navigationIcon = {
//            NavigationItem(
//                modifier = Modifier.fillMaxHeight(),
//                icon = Icons.AutoMirrored.Filled.ArrowBackIos,
//                label = "Go back",
//                tint = tintColor,
//                onNavigateAction = onNavigateBackAction
//            )
//        },
//        actions = {}
//    )
//}
//
//@Preview
//@PreviewLightDark
//@Composable
//fun OnboardingToolbarPreview() {
//    OnboardingToolbar(
//        modifier = Modifier.height(100.dp),
//        title = "Onboarding",
//        onNavigateBackAction = {},
//    )
//}