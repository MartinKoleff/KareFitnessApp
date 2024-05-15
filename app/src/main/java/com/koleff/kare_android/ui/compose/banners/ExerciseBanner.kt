package com.koleff.kare_android.ui.compose.banners

import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.koleff.kare_android.R
import com.koleff.kare_android.common.MockupDataGeneratorV2
import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.model.dto.MuscleGroup
import kotlin.math.roundToInt

@Composable
fun ExerciseBannerV1(
    modifier: Modifier,
    exercise: ExerciseDto,
    onClick: () -> Unit
) {
    val configuration = LocalConfiguration.current

    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    val textColor = MaterialTheme.colorScheme.onSurface
    val titleTextStyle = MaterialTheme.typography.titleMedium.copy(
        color = textColor
    )
    Box(
        modifier = modifier
            .clickable { onClick.invoke() }
            .background(
                brush = Brush.verticalGradient(
                    listOf(Color.Transparent, Color.Black) //Flowing effect
                )
            ),
    ) {
        Image(
            modifier = Modifier
                .fillMaxHeight()
                .width(screenWidth / 2)
                .align(Alignment.TopStart),
            painter = painterResource(id = R.drawable.background_exercise_banner_dark),
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
                style = titleTextStyle,
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
            painter = painterResource(id = MuscleGroup.getImage(exercise.muscleGroup)),
            contentDescription = exercise.name,
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun ExerciseBannerV2(
    modifier: Modifier,
    exercise: ExerciseDto,
    hasDescription: Boolean = false,
    onClick: (ExerciseDto) -> Unit
) {
    val configuration = LocalConfiguration.current

    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    val exerciseImage = MuscleGroup.getImage(exercise.muscleGroup)

    val titleTextColor = MaterialTheme.colorScheme.onSurface
    val titleTextStyle = MaterialTheme.typography.titleMedium.copy(
        color = titleTextColor,
    )

    val descriptionTextColor = MaterialTheme.colorScheme.onSurface
    val descriptionTextStyle = MaterialTheme.typography.titleSmall.copy(
        color = descriptionTextColor,
    )

    val bannerImage = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        if (configuration.isNightModeActive) {
            R.drawable.background_exercise_banner_dark
        } else {
            R.drawable.background_exercise_banner_light
        }
    } else {

        //No dark mode supported -> default banner
        R.drawable.background_exercise_banner_dark
    }

    Card(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
        shape = RoundedCornerShape(15.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        ),
        onClick = { onClick.invoke(exercise) }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {

            //Fixes left side white overlay
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(screenWidth / 2)
                    .align(Alignment.TopStart)
                    .drawBehind {

                        //Fixes white rectangle on left half side
                        drawRect(
                            color = Color.Black,
                            size = this.size.copy(
                                width = (screenWidth / 2).toPx()
                            )
                        )
                    }
                    .graphicsLayer { alpha = 0.80f }
            )


            //Exercise Image
            Image(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(screenWidth / 2)
                    .align(Alignment.TopEnd),
                painter = painterResource(id = exerciseImage),
                contentDescription = exercise.name,
                contentScale = ContentScale.Crop
            )

            //Parallax effect overflowing into exercise snapshot
            Image(
                painter = painterResource(bannerImage),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.TopStart)
                    .graphicsLayer { alpha = 0.55f }
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

            //Texts
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(screenWidth / 2)
                    .padding(end = 8.dp),
                verticalArrangement = Arrangement.Center,
            ) {

                //Exercise title
                Text(
                    modifier = Modifier.padding(
                        PaddingValues(
                            start = 16.dp,
                            end = 16.dp,
                            top = 16.dp,
                            bottom = 0.dp
                        )
                    ),
                    text = exercise.name,
                    style = titleTextStyle,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                //Exercise sub-title (description)
                if (hasDescription) {
                    Text(
                        modifier = Modifier.padding(
                            PaddingValues(
                                start = 16.dp,
                                end = 16.dp,
                                top = 8.dp,
                                bottom = 0.dp
                            )
                        ),
                        text = "Description", //TODO: wire with ExerciseDTO...
                        style = descriptionTextStyle,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
fun ExerciseList(
    innerPadding: PaddingValues = PaddingValues(0.dp),
    exerciseList: List<ExerciseDto>,
    navigateToExerciseDetails: (ExerciseDto) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = 8.dp,
                start = 8.dp + innerPadding.calculateStartPadding(LayoutDirection.Rtl),
                end = 8.dp + innerPadding.calculateEndPadding(LayoutDirection.Rtl),
                bottom = 8.dp + innerPadding.calculateBottomPadding()
            )
    ) {
        items(exerciseList) { exercise ->
            ExerciseBannerV2(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                exercise = exercise,
            ) {
                navigateToExerciseDetails(exercise)
            }
        }
    }
}

@Composable
fun SwipeableExerciseBanner(
    modifier: Modifier,
    exercise: ExerciseDto,
    hasDescription: Boolean = true,
    onClick: (ExerciseDto) -> Unit,
    onDelete: () -> Unit
) {
    val configuration = LocalConfiguration.current

    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    //Used for swipe left
    var offsetX by remember { mutableStateOf(0f) }
    val swipeLimit = screenWidth * 0.25f

    val deleteBoxWidth = screenWidth / 4
    val deleteBoxModifier = Modifier
        .height(200.dp) //Banner height
        .width(deleteBoxWidth)

    Box {
        ExerciseBannerV2(
            modifier = modifier
                .offset { IntOffset(offsetX.roundToInt(), 0) }
                .pointerInput(Unit) {
                    detectHorizontalDragGestures { change, dragAmount ->
                        val newOffsetX = (offsetX + dragAmount)
                            .coerceIn(-swipeLimit.toPx(), 0f)
                        offsetX = newOffsetX
                        change.consumeAllChanges()
                    }
                },
            exercise = exercise,
            hasDescription = hasDescription,
            onClick = onClick
        )

        //Delete option
        DeleteButton(
            modifier = deleteBoxModifier
                .offset {
                    IntOffset(
                        (screenWidth.toPx() + offsetX).roundToInt(), 0
                    )
                },
            onDelete = onDelete,
            title = "Delete Exercise"
        )
    }
}

@Preview
@Composable
fun ExerciseBannerV1AndV2ComparingPreview() {
    val exercise = MockupDataGeneratorV2.generateExercise()

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        ExerciseBannerV2(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            exercise = exercise,
            onClick = {}
        )

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        )

        ExerciseBannerV1(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            exercise = exercise,
            onClick = {}
        )
    }
}

@Preview
@PreviewLightDark
@Composable
fun SwipeableExerciseBannerPreview() {
    val exercise = MockupDataGeneratorV2.generateExercise()

    SwipeableExerciseBanner(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        exercise = exercise,
        onClick = {},
        onDelete = {}
    )
}

@Preview
@PreviewLightDark
@Composable
fun ExerciseListPreview() {
    val n = 5
    val exercisesList = MockupDataGeneratorV2.generateExerciseList(n)

    ExerciseList(exerciseList = exercisesList) { exercise ->

    }
}