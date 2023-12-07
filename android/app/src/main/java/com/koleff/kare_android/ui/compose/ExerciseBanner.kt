package com.koleff.kare_android.ui.compose

import android.graphics.Paint
import android.text.Layout
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.koleff.kare_android.R
import com.koleff.kare_android.data.model.dto.ExerciseData
import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.model.dto.MachineType
import com.koleff.kare_android.data.model.dto.MuscleGroup

@Composable
fun ExerciseBanner(modifier: Modifier, exercise: ExerciseData, onClick: () -> Unit) {
    val configuration = LocalConfiguration.current

    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    Box(
        modifier = modifier
            .clickable { onClick.invoke() }
            .background(
                brush = Brush.verticalGradient(
                    listOf(Color.Transparent, Color.Black)
                )
            ),
    ) {
        //Parallax effect
        Image(
            modifier = Modifier
                .fillMaxHeight()
                .width(screenWidth / 2)
                .align(Alignment.TopStart),
            painter = painterResource(id = R.drawable.ic_exercise_banner_effect), //TODO: change to url
            contentDescription = "Background",
            contentScale = ContentScale.Crop
        )

        //Exercise Title TextBox
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(screenWidth / 2),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                modifier = Modifier.padding(16.dp),
                text = exercise.name,
                style = TextStyle(
                    color = Color.White,
                    fontSize = 16.sp,
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }

        //Exercise Image
        Image(
            modifier = Modifier
                .fillMaxHeight()
                .width(screenWidth / 2)
                .align(Alignment.TopEnd),
            painter = painterResource(id = R.drawable.ic_chest), //TODO: change to url
            contentDescription = exercise.name,
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun ExerciseBanner2(modifier: Modifier, exercise: ExerciseData, onClick: () -> Unit) {
    val configuration = LocalConfiguration.current

    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    Box(
        modifier = modifier
            .clickable { onClick.invoke() }
            .background(
                brush = Brush.verticalGradient(
                    listOf(Color.Transparent, Color.Black)
                )
            ),
    ) {

        //Exercise Image
        Image(
            modifier = Modifier
                .fillMaxHeight()
                .width(screenWidth / 2)
                .align(Alignment.TopEnd),
            painter = painterResource(id = R.drawable.ic_chest), //TODO: change to url
            contentDescription = exercise.name,
            contentScale = ContentScale.Crop
        )

        //Parallax effect overflowing into exercise snapshot
        Image(
            painterResource(R.drawable.ic_exercise_banner_effect),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.TopStart)
                .graphicsLayer { alpha = 0.80f } //TODO: add opacity / 0.66f alpha works
                .drawWithContent {

                    //Fill 5/8 of the screen with effect gradient
                    val colors = listOf(
                        Color.Black,
                        Color.Black,
                        Color.Black,
                        Color.Black,
                        Color.Black,
                        Color.Transparent,
                        Color.Transparent,
                        Color.Transparent
                    )
                    drawContent()
                    drawRect(
                        brush = Brush.horizontalGradient(colors),
                        blendMode = BlendMode.DstIn
                    )
                }
        )

        //Exercise Title TextBox
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(screenWidth / 2),
            contentAlignment = Alignment.CenterStart
        ) {
            Text( //TODO: and cooler font
                modifier = Modifier.padding(16.dp),
                text = exercise.name,
                style = TextStyle(
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview
@Composable
fun SimpleComposablePreview() {
    val exercise = ExerciseData(
        ExerciseDto(
            -1,
            "BARBELL BENCH PRESS",
            MuscleGroup.CHEST,
            MachineType.BARBELL,
            ""
        )
    )
    ExerciseBanner2(
        Modifier
            .fillMaxWidth()
            .height(200.dp), exercise, {}
    )
}