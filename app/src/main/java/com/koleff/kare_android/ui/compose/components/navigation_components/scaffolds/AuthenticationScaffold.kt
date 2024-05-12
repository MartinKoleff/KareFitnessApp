package com.koleff.kare_android.ui.compose.components.navigation_components.scaffolds

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.koleff.kare_android.R
import com.koleff.kare_android.ui.compose.components.navigation_components.toolbar.AuthenticationToolbar
import com.koleff.kare_android.ui.compose.shapes.RoundedToolbarShape
import com.koleff.kare_android.ui.theme.LocalExtendedColorScheme

@Composable
fun AuthenticationScaffold(
    screenTitle: String,
    onNavigateBackAction: () -> Unit,
    modifierPadding: @Composable (paddingValues: PaddingValues) -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp
    val backgroundGradientColor = LocalExtendedColorScheme.current.authenticationScreenBackgroundGradient

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

        //Background gradient
        Box(
            modifier = Modifier
                .fillMaxSize()
                .drawWithContent {
                    drawContent()
                    drawRect(
                        brush = Brush.linearGradient(backgroundGradientColor),
                        blendMode = BlendMode.Overlay //ColorBurn
                    )
                }
        ) {

            //Texture background
            Image(
                painter = painterResource(id = R.drawable.background_login_7),
                contentDescription = "Background",
                contentScale = ContentScale.Crop
            )
        }

        modifierPadding(innerPadding)
    }
}