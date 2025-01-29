package com.koleff.kare_android.ui.compose.components.navigation_components.scaffolds

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.koleff.kare_android.R
import com.koleff.kare_android.ui.compose.components.navigation_components.toolbar.AuthenticationToolbar

@Composable
fun AuthenticationScaffold(
    screenTitle: String,
    onNavigateBackAction: () -> Unit,
    modifierPadding: @Composable (paddingValues: PaddingValues) -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AuthenticationToolbar(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenHeight / 2.5f),
                onNavigateBackAction = onNavigateBackAction
            )
        },
    ) { innerPadding ->
        //Texture background
        Image(
            painter = painterResource(id = R.drawable.background_login_7),
            contentDescription = "Background",
            contentScale = ContentScale.Crop
        )

        modifierPadding(innerPadding)
    }
}