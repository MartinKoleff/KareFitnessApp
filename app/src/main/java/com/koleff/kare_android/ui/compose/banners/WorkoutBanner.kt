package com.koleff.kare_android.ui.compose.banners

import androidx.annotation.Px
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.koleff.kare_android.R
import com.koleff.kare_android.common.MockupDataGeneratorV2
import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.data.model.dto.WorkoutDto
import com.koleff.kare_android.ui.theme.ExtendedColorScheme
import com.koleff.kare_android.ui.theme.LocalExtendedColorScheme
import kotlin.math.roundToInt

@Composable
fun WorkoutBanner(
    modifier: Modifier,
    workout: WorkoutDto,
    hasDescription: Boolean = true,
    onClick: (WorkoutDto) -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    val workoutImage: Int = MuscleGroup.getImage(workout.muscleGroup)
    val titleTextColor = MaterialTheme.colorScheme.onSurface
    val subtitleTextColor = MaterialTheme.colorScheme.onSurface
    Card(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
        shape = RoundedCornerShape(15.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        ),
        onClick = { onClick.invoke(workout) }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {

            //Workout Image
            Image(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(screenWidth / 2)
                    .align(Alignment.TopEnd),
                painter = painterResource(id = workoutImage),
                contentDescription = workout.name,
                contentScale = ContentScale.Crop
            )

            //Hexagon effect overflowing into workout snapshot
            Image(
                painter = painterResource(R.drawable.background_workout_banner),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
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
                    .drawWithContent {
                        drawContent()

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

                        drawRect(
                            brush = Brush.horizontalGradient(colors),
                            blendMode = BlendMode.DstIn
                        )
                    }
            )

            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(screenWidth / 2),
            ) {

                //Texts
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(end = 8.dp),
//                        .weight(7f),
                    verticalArrangement = Arrangement.Center, //TODO: change to Top when adding sets...
                ) {

                    //Workout title
                    Text(
                        modifier = Modifier.padding(
                            PaddingValues(
                                start = 16.dp,
                                end = 16.dp,
                                top = 16.dp,
                                bottom = 0.dp
                            )
                        ),
                        text = workout.name,
                        style = TextStyle(
                            color = titleTextColor,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    //Workout sub-title (total exercises)
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
                            text = "Exercises: ${workout.totalExercises}",
                            style = TextStyle(
                                color = subtitleTextColor,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        //TODO: add the first 3 sets and their reps, weight/duration...
                    }
                }

//                //Start workout button
//                NavigationItem(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .weight(1.5f)
//                        .drawBehind {
//                            drawCircle(
//                                color = Color.White,
//                                radius = this.size.width / 2
//                            )
//                        },
//                    icon = painterResource(id = androidx.media3.ui.R.drawable.exo_legacy_controls_play),
//                    label = "Start workout",
//                    tint = Color.Green,
//                    onNavigateAction = onStartWorkoutAction
//                )
            }
        }
    }
}

@Composable
fun SwipeableWorkoutBanner(
    modifier: Modifier = Modifier,
    workout: WorkoutDto,
    hasDescription: Boolean = true,
    onClick: (WorkoutDto) -> Unit,
    onDelete: () -> Unit,
    onSelect: () -> Unit,
    onEdit: () -> Unit
) {
    val configuration = LocalConfiguration.current

    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    //Used for swipe left
    var offsetX by remember { mutableStateOf(0f) }
    val swipeLimit = screenWidth * 0.25f //This is 1/4 of the screen.

    val optionBoxWidth = screenWidth / 4
    val optionBoxModifier = Modifier
        .height(200.dp / 3) //3 options to fill in the column
        .width(optionBoxWidth)

    val optionColumnModifier = Modifier
        .height(200.dp) //Banner height
        .width(optionBoxWidth)

    Box {
        WorkoutBanner(
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
            workout = workout,
            hasDescription = hasDescription,
            onClick = onClick
        )

        //Options
        Column(modifier = optionColumnModifier.offset {
            IntOffset(
                (screenWidth.toPx() + offsetX).roundToInt(), 0
            )
        }) {

            //Select option
            EditButton(
                modifier = optionBoxModifier,
                onEdit = onEdit,
                title = "Edit Workout Name"
            )

            //Select option
            SelectButton(
                modifier = optionBoxModifier,
                onSelect = onSelect,
                title = "Select Workout"
            )

            //Delete option
            DeleteButton(
                modifier = optionBoxModifier,
                onDelete = onDelete,
                title = "Delete Workout"
            )
        }
    }
}

@Composable
fun DeleteButton(
    modifier: Modifier,
    title: String,
    onDelete: () -> Unit
) {
    val iconSize = 20.dp
    val cornerSize = 24.dp
    val paddingValues = PaddingValues(bottom = 8.dp)
    val textColor = MaterialTheme.colorScheme.onSurface
    val outlineColor = MaterialTheme.colorScheme.outlineVariant
    val tintColor = MaterialTheme.colorScheme.onSurface

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .clip(RoundedCornerShape(cornerSize))
            .border(
                border = BorderStroke(2.dp, color = outlineColor),
                shape = RoundedCornerShape(cornerSize)
            )
            .background(
                color = LocalExtendedColorScheme.current.workoutBannerColors.deleteButtonColor,
                shape = RoundedCornerShape(cornerSize)
            )
            .clickable(onClick = onDelete)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_delete),
            contentDescription = "Delete",
            tint = tintColor,
            modifier = Modifier
                .size(iconSize)
        )

        Spacer(
            modifier = Modifier
                .height(5.dp)
                .fillMaxWidth()
        )

        Text(
            text = title,
            style = TextStyle(
                color = textColor,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun SelectButton(
    modifier: Modifier,
    title: String,
    onSelect: () -> Unit
) {
    val iconSize = 20.dp
    val cornerSize = 24.dp
    val paddingValues = PaddingValues(bottom = 8.dp)

    val textColor = MaterialTheme.colorScheme.onSurface
    val outlineColor = MaterialTheme.colorScheme.outlineVariant
    val tintColor = MaterialTheme.colorScheme.onSurface

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .clip(RoundedCornerShape(cornerSize))
            .border(
                border = BorderStroke(2.dp, color = outlineColor),
                shape = RoundedCornerShape(cornerSize)
            )
            .background(
                color = LocalExtendedColorScheme.current.workoutBannerColors.selectButtonColor,
                shape = RoundedCornerShape(cornerSize)
            )
            .clickable(onClick = onSelect)
    ) {
        val image: Painter = painterResource(id = R.drawable.ic_vector_select)

        Image(
            painter = image,
            contentDescription = "Select",
            modifier = Modifier
                .size(iconSize),
            colorFilter = ColorFilter.tint(tintColor),
            contentScale = ContentScale.Crop
        )

        Spacer(
            modifier = Modifier
                .height(5.dp)
                .fillMaxWidth()
        )

        Text(
            text = title,
            style = TextStyle(
                color = textColor,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun EditButton(
    modifier: Modifier,
    title: String,
    onEdit: () -> Unit
) {
    val iconSize = 20.dp
    val cornerSize = 24.dp
    val paddingValues = PaddingValues(bottom = 8.dp)

    val textColor = MaterialTheme.colorScheme.onSurface
    val outlineColor = MaterialTheme.colorScheme.outlineVariant
    val tintColor = MaterialTheme.colorScheme.onSurface

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .clip(RoundedCornerShape(cornerSize))
            .border(
                border = BorderStroke(2.dp, color = outlineColor),
                shape = RoundedCornerShape(cornerSize)
            )
            .background(
                color = LocalExtendedColorScheme.current.workoutBannerColors.editButtonColor,
                shape = RoundedCornerShape(cornerSize)
            )
            .clickable(onClick = onEdit)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_edit),
            contentDescription = "Edit",
            tint = tintColor,
            modifier = Modifier
                .size(iconSize)
        )

        Spacer(
            modifier = Modifier
                .height(5.dp)
                .fillMaxWidth()
        )

        Text(
            text = title,
            style = TextStyle(
                color = textColor,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            ),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
    }
}


@Composable
fun WorkoutList(
    modifier: Modifier,
    workoutList: List<WorkoutDto>,
    navigateToWorkoutDetails: (WorkoutDto) -> Unit
) {
    LazyColumn(modifier = modifier) {
        items(workoutList) { workout ->
            WorkoutBanner(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                workout = workout,
            ) {
                navigateToWorkoutDetails(workout)
            }
        }
    }
}

@Preview
@Composable
fun WorkoutListPreview() {
    val n = 5
    val workoutList: MutableList<WorkoutDto> = mutableListOf()
    repeat(n) { index ->
        val currentWorkout = WorkoutDto(
            workoutId = index,
            name = "Chest workout $index",
            muscleGroup = MuscleGroup.fromId(index + 1),
            snapshot = "",
            totalExercises = 5,
            isSelected = false
        )
        workoutList.add(currentWorkout)
    }

    val innerPadding: PaddingValues = PaddingValues(0.dp)     //if inside Scaffold...
    val modifier = Modifier
        .fillMaxSize()
        .padding(
            top = 8.dp,
            start = 8.dp + innerPadding.calculateStartPadding(LayoutDirection.Rtl),
            end = 8.dp + innerPadding.calculateEndPadding(LayoutDirection.Rtl),
            bottom = 8.dp + innerPadding.calculateBottomPadding()
        )

    WorkoutList(
        modifier = modifier,
        workoutList = workoutList,
        navigateToWorkoutDetails = { workoutDto ->

        }
    )
}

@Preview
@Composable
fun WorkoutBannerPreview() {
    val workout = MockupDataGeneratorV2.generateWorkout()

    WorkoutBanner(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        onClick = {},
        workout = workout
    )
}

@Preview
@Composable
fun SwipeableWorkoutBannerPreview() {
    val workout = MockupDataGeneratorV2.generateWorkout()

    SwipeableWorkoutBanner(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        onClick = {},
        onDelete = {},
        onSelect = {},
        onEdit = {},
        workout = workout
    )
}

@Preview
@Composable
fun DeleteButtonPreview() {
    val configuration = LocalConfiguration.current

    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    DeleteButton(
        modifier = Modifier
            .width(screenWidth / 4)
            .height(200.dp),
        onDelete = {},
        title = "Delete Workout"
    )
}

@Preview
@Composable
fun SelectButtonPreview() {
    val configuration = LocalConfiguration.current

    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    SelectButton(
        modifier = Modifier
            .width(screenWidth / 4)
            .height(200.dp),
        onSelect = {},
        title = "Select Workout"
    )
}

@Preview
@Composable
fun EditButtonPreview() {
    val configuration = LocalConfiguration.current

    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    EditButton(
        modifier = Modifier
            .width(screenWidth / 4)
            .height(200.dp),
        onEdit = {},
        title = "Edit Workout Name"
    )
}