package com.koleff.kare_android.ui.compose.components.navigation_components.toolbar

import android.os.Build
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.koleff.kare_android.R
import com.koleff.kare_android.ui.compose.components.navigation_components.NavigationItem
import com.koleff.kare_android.ui.compose.shapes.RoundedToolbarShape
import com.koleff.kare_android.ui.theme.LocalExtendedColorScheme


@Composable
fun AuthenticationToolbar(
    modifier: Modifier,
    imageId: Int = R.drawable.background_muscle_default,
    onNavigateBackAction: () -> Unit
) {
    val configuration = LocalConfiguration.current

    val backgroundColor = LocalExtendedColorScheme.current.detailsToolbarColor
    val tintColor = MaterialTheme.colorScheme.onSurface
    val circleColor = MaterialTheme.colorScheme.surface
    val outlineColor = MaterialTheme.colorScheme.outlineVariant

    val hasTopOutline = true

    Box(modifier = modifier) {

        //Exercise Muscle Group Image
        Image(
            painter = painterResource(id = imageId),
            contentDescription = "Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .clip(
                    RoundedToolbarShape(hasTopOutline)
                )
                .border(
                    border = BorderStroke(
                        width = 1.dp,
                        color = outlineColor
                    ),
                    shape = RoundedToolbarShape(hasTopOutline)
                )
        )

        //Navigation
        Row(
            Modifier
                .fillMaxWidth()
                .height(50.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            NavigationItem(
                modifier = Modifier.drawBehind {
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//                        if (configuration.isNightModeActive) {
                            drawCircle(
                                radius = this.size.maxDimension / 4,
                                color = circleColor
                            )

                            if(hasTopOutline){
                                drawCircle(
                                    radius = this.size.maxDimension / 4,
                                    color = outlineColor,
                                    style = Stroke(width = 1.dp.toPx())
                                )
                            }
//                        }
//                    }
                },
                icon = Icons.Filled.ArrowBack,
                label = "Go back",
                tint = tintColor,
                onNavigateAction = onNavigateBackAction
            )
        }
    }
}

@Preview
@PreviewLightDark
@Composable
fun PreviewAuthenticationToolbar() {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    val backgroundColor = MaterialTheme.colorScheme.primary
    AuthenticationToolbar(
        modifier = Modifier
            .fillMaxWidth()
            .height(screenHeight / 2.5f)
            .background(backgroundColor),
        imageId = R.drawable.background_muscle_default,
        onNavigateBackAction = {}
    )
}




