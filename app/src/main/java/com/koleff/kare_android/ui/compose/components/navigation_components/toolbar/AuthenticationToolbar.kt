package com.koleff.kare_android.ui.compose.components.navigation_components.toolbar

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.koleff.kare_android.R
import com.koleff.kare_android.ui.compose.components.navigation_components.NavigationItem
import com.koleff.kare_android.ui.compose.shapes.RoundedToolbarShape


@Composable
fun AuthenticationToolbar(
    modifier: Modifier,
    imageId: Int = R.drawable.ic_default,
    onNavigateBackAction: () -> Unit
) {
    Box(
        modifier = modifier
    ) {

        //Exercise Muscle Group Image
        Image(
            painter = painterResource(id = imageId),
            contentDescription = "Image",
            contentScale = ContentScale.Crop,
            modifier = modifier
                .clip(
                    RoundedToolbarShape(hasTopOutline = false)
                )
//                .border(
//                    border = BorderStroke(
//                        width = 2.dp,
//                        color = Color.White
//                    ),
//                    shape = RoundedToolbarShape(hasTopOutline = false)
//                )
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
//                modifier = Modifier
//                    .drawBehind {
//                        drawCircle(
//                            color = Color.Black,
//                            radius = this.size.maxDimension / 4
//                        )
//                    },
                icon = Icons.Filled.ArrowBack,
                label = "Go back",
                tint = Color.White,
                onNavigateAction = onNavigateBackAction
            )
        }
    }
}

@Preview
@Composable
fun PreviewAuthenticationToolbar() {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    AuthenticationToolbar(
        modifier = Modifier
            .fillMaxWidth()
            .height(screenHeight / 2.5f)
            .background(color = MaterialTheme.colorScheme.primaryContainer),
        imageId = R.drawable.ic_default,
        onNavigateBackAction = {}
    )
}



