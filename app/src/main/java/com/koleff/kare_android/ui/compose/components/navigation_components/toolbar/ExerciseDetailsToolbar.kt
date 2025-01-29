package com.koleff.kare_android.ui.compose.components.navigation_components.toolbar

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.Fill
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


@Composable
fun ExerciseDetailsToolbar(
    modifier: Modifier,
    exerciseImageId: Int,
    onNavigateAction: () -> Unit,
    onNavigateBackAction: () -> Unit,
) {
    val tintColor = MaterialTheme.colorScheme.onSurface
    val circleColor = MaterialTheme.colorScheme.surface
    val outlineColor = MaterialTheme.colorScheme.outlineVariant

    val hasTopOutline = true
    Box(
        modifier = modifier
    ) {

        //Exercise Muscle Group Image
        Image(
            painter = painterResource(id = exerciseImageId),
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

                            //Fill circle
                            drawCircle(
                                center = Offset.Zero,
                                radius = this.size.maxDimension,
                                color = circleColor,
                                style = Fill
                            )

                            //Border circle
                            if (hasTopOutline) {
                                drawCircle(
                                    center = Offset.Zero,
                                    radius = this.size.maxDimension,
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

            NavigationItem(
                modifier = Modifier.drawBehind {
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//                        if (configuration.isNightModeActive) {

                            //Fill circle
                            drawCircle(
                                center = Offset(x = this.size.width, y = 0f),
                                radius = this.size.maxDimension,
                                color = circleColor,
                                style = Fill
                            )

                            //Border circle
                            if (hasTopOutline) {
                                drawCircle(
                                    center = Offset(x = this.size.width, y = 0f),
                                    radius = this.size.maxDimension,
                                    color = outlineColor,
                                    style = Stroke(width = 1.dp.toPx())
                                )
                            }
//                        }
//                    }
                },
                icon = Icons.Filled.Settings,
                label = "Settings",
                tint = tintColor,
                onNavigateAction = onNavigateAction
            )
        }
    }
}

@Preview
@PreviewLightDark
@Composable
fun PreviewExerciseDetailsToolbar() {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    ExerciseDetailsToolbar(
        modifier = Modifier
            .fillMaxWidth()
            .height(screenHeight / 2.5f),
        exerciseImageId = R.drawable.background_legs,
        onNavigateBackAction = {},
        onNavigateAction = {}
    )
}




