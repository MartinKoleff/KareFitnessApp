package com.koleff.kare_android.ui.compose.banners

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.koleff.kare_android.R
import com.koleff.kare_android.data.MainScreen
import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.data.model.dto.WorkoutDto

@Composable
fun ListItemBannerV1(
    modifier: Modifier,
    exercise: ExerciseDto,
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

        //Title
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

        //Image
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
fun ListItemBannerV2(
    modifier: Modifier,
    title: String,
    muscleGroup: MuscleGroup,
    hasDescription: Boolean = false,
    onClick: () -> Unit
) {
    val configuration = LocalConfiguration.current

    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    val itemImage: Int = when (muscleGroup) {
        MuscleGroup.CHEST -> R.drawable.ic_chest
        MuscleGroup.BACK -> R.drawable.ic_back
        MuscleGroup.TRICEPS -> R.drawable.ic_triceps
        MuscleGroup.BICEPS, MuscleGroup.ARMS -> R.drawable.ic_biceps
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
        onClick = { onClick.invoke() }
    ) {
        Box(modifier = modifier.fillMaxSize()) {

            //Image
            Image(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(screenWidth / 2)
                    .align(Alignment.TopEnd),
                painter = painterResource(id = itemImage), //TODO: change to url
                contentDescription = title,
                contentScale = ContentScale.Crop
            )

            //Parallax effect overflowing into snapshot
            Image(
                painter = painterResource(
                    if (hasDescription) R.drawable.ic_workout_banner_effect
                    else R.drawable.ic_exercise_banner_effect
                ),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.TopStart)
                    .graphicsLayer { alpha = 0.80f } 
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
                    Text( //TODO: and cooler font...
                        modifier = Modifier.padding(
                            PaddingValues(
                                start = 16.dp,
                                end = 16.dp,
                                top = 16.dp,
                                bottom = 0.dp
                            )
                        ),
                        text = title,
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    //Sub-title (description)
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
                            text = "Description",
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
fun ExerciseBannerList(
    innerPadding: PaddingValues = PaddingValues(0.dp),
    exerciseList: List<ExerciseDto>,
    navController: NavHostController
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
            ListItemBannerV2(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                title = exercise.name,
                muscleGroup = exercise.muscleGroup,
                hasDescription = false
            ) {
                openExerciseDetailsScreen(exercise, navController)
            }
        }
    }
}

@Composable
fun WorkoutBannerList(
    innerPadding: PaddingValues = PaddingValues(0.dp),
    workoutList: List<WorkoutDto>,
    navController: NavHostController
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
        items(workoutList) { workout ->
            ListItemBannerV2(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                title = workout.name,
                muscleGroup = workout.muscleGroup, //TODO: Add totalExercises...
                hasDescription = true
            ) {
                openWorkoutDetailsScreen(workout, navController)
            }
        }
    }
}

fun openExerciseDetailsScreen(exercise: ExerciseDto, navController: NavHostController) {
    navController.navigate(
        MainScreen.ExerciseDetails.createRoute(
            exerciseId = exercise.exerciseId,
            muscleGroupId = exercise.muscleGroup.muscleGroupId
        )
    )
}

fun openWorkoutDetailsScreen(workout: WorkoutDto, navController: NavHostController) {
    navController.navigate(MainScreen.WorkoutDetails.createRoute(workoutId = workout.workoutId))
}