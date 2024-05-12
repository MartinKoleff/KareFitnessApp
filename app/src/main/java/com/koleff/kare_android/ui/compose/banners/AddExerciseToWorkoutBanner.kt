package com.koleff.kare_android.ui.compose.banners

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.koleff.kare_android.R

@Composable
fun AddExerciseToWorkoutBanner(
    onClick: () -> Unit
) {
    val configuration = LocalConfiguration.current

    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    val primaryColor = MaterialTheme.colorScheme.primary
    val titleTextColor = MaterialTheme.colorScheme.onSurface
    val subtitleTextColor = MaterialTheme.colorScheme.onSurface
    val circleColor = MaterialTheme.colorScheme.onSurface
    val addNewWorkoutTint = Color.Green

    val titleTextStyle = MaterialTheme.typography.titleMedium
    val subtitleTextStyle = MaterialTheme.typography.titleSmall

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(8.dp),
        shape = RoundedCornerShape(15.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        ),
        onClick = { onClick.invoke() }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {

            //Parallax effect overflowing into snapshot
            Image(
                painter = painterResource(R.drawable.background_add_button),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.TopStart)
                    .graphicsLayer { alpha = 0.80f },
                colorFilter = ColorFilter.tint(primaryColor, BlendMode.Multiply)
            )

            Image(
                painter = painterResource(R.drawable.ic_vector_add),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(50.dp)
                    .align(Alignment.CenterEnd)
                    .padding(end = 16.dp)
                    .drawBehind {
                        drawCircle(
                            color = circleColor,
                            radius = this.size.maxDimension / 2
                        )
                    },
                colorFilter = ColorFilter.tint(addNewWorkoutTint)
            )

            //Text
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(screenWidth / 2),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(end = 8.dp),
                    verticalArrangement = Arrangement.Center,
                ) {

                    //Title
                    Text(
                        modifier = Modifier.padding(
                            PaddingValues(
                                start = 16.dp,
                                end = 16.dp,
                                top = 16.dp,
                                bottom = 0.dp
                            )
                        ),
                        text = "Want to add exercise to your workout?",
                        style = titleTextStyle,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    //Sub-title (description)
                    Text(
                        modifier = Modifier.padding(
                            PaddingValues(
                                start = 16.dp,
                                end = 16.dp,
                                top = 8.dp,
                                bottom = 0.dp
                            )
                        ),
                        text = "Click to select an exercise.",
                        style = subtitleTextStyle,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Preview
@PreviewLightDark
@Composable
fun AddExerciseToWorkoutBannerPreview() {
    AddExerciseToWorkoutBanner() {

    }
}