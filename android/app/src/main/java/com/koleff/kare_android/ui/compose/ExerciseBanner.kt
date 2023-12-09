package com.koleff.kare_android.ui.compose

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.koleff.kare_android.R
import com.koleff.kare_android.data.model.dto.ExerciseData
import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.model.dto.MachineType
import com.koleff.kare_android.data.model.dto.MuscleGroup

@Composable
fun ExerciseBannerV1(
    modifier: Modifier,
    exercise: ExerciseData,
    onClick: () -> Unit
) {
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
fun ExerciseBannerV2(
    modifier: Modifier,
    exercise: ExerciseData,
    hasDescription: Boolean = false,
    onClick: (ExerciseData) -> Unit
) {
    val configuration = LocalConfiguration.current

    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    val exerciseImage: Int = when (exercise.muscleGroup) {
        MuscleGroup.CHEST -> R.drawable.ic_chest
        MuscleGroup.BACK -> R.drawable.ic_back
        MuscleGroup.TRICEPS -> R.drawable.ic_triceps
        MuscleGroup.BICEPS -> R.drawable.ic_biceps
        MuscleGroup.SHOULDERS -> R.drawable.ic_shoulder
        MuscleGroup.LEGS -> R.drawable.ic_legs
        else -> -1 //TODO: handle invalid muscle group...
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
        Box(modifier = modifier.fillMaxSize()) {

            //Exercise Image
            Image(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(screenWidth / 2)
                    .align(Alignment.TopEnd),
                painter = painterResource(id = exerciseImage), //TODO: change to url
                contentDescription = exercise.name,
                contentScale = ContentScale.Crop
            )

            //Parallax effect overflowing into exercise snapshot
            Image(
                painter = painterResource(R.drawable.ic_exercise_banner_effect),
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
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(end = 8.dp),
                    verticalArrangement = Arrangement.Center,
                ) {

                    //Exercise title
                    Text( //TODO: and cooler font...
                        modifier = Modifier.padding(
                            PaddingValues(
                                start = 16.dp,
                                end = 16.dp,
                                top = 16.dp,
                                bottom = 0.dp
                            )
                        ),
                        text = exercise.name,
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    //Exercise sub-title (description)
                    if (hasDescription) {
                        Text( //TODO: and cooler font...
                            modifier = Modifier.padding(
                                PaddingValues(
                                    start = 16.dp,
                                    end = 16.dp,
                                    top = 8.dp,
                                    bottom = 0.dp
                                )
                            ),
                            text = "Description", //TODO: wire with ExerciseDTO...
                            style = TextStyle(
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ExerciseList(
    innerPadding: PaddingValues = PaddingValues(0.dp),
    exerciseList: List<ExerciseData>
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
                openExerciseDetailsScreen(exercise)
            }
        }
    }
}

fun openExerciseDetailsScreen(exercise: ExerciseData) {
    //TODO: Navigate to exercise details screen...
    Log.d("Exercise Details", "Selected exercise with id ${exercise.name}")
}


@Preview
@Composable
fun ExerciseBannerV1AndV2ComparingPreview() {
    val exercise = ExerciseData(
        ExerciseDto(
            -1,
            "BARBELL BENCH PRESS",
            MuscleGroup.CHEST,
            MachineType.BARBELL,
            ""
        )
    )

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
            Modifier
                .fillMaxWidth()
                .height(200.dp), exercise, {}
        )
    }
}

@Preview
@Composable
fun ExerciseListPreview() {
    val n = 10
    val exercisesList: MutableList<ExerciseData> = mutableListOf()

    repeat(n) { index ->
        val currentExercise = ExerciseData(
            ExerciseDto(
                -1,
                "BARBELL BENCH PRESS $index",
                MuscleGroup.CHEST,
                MachineType.BARBELL,
                ""
            )
        )
        exercisesList.add(currentExercise)
    }

    ExerciseList(exerciseList = exercisesList)
}