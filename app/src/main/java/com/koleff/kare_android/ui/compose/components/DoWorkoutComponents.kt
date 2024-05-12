package com.koleff.kare_android.ui.compose.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.koleff.kare_android.R
import com.koleff.kare_android.common.MockupDataGeneratorV2
import com.koleff.kare_android.common.timer.TimerUtil
import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.model.dto.ExerciseProgressDto
import com.koleff.kare_android.data.model.dto.ExerciseSetDto
import com.koleff.kare_android.data.model.dto.ExerciseTime
import com.koleff.kare_android.ui.state.ExerciseTimerStyle
import kotlin.random.Random

@Composable
fun HalfTransparentColumn(alpha: Float = 0.3f, content: @Composable ColumnScope.() -> Unit) {
    Column {
        content()

        Canvas(modifier = Modifier.fillMaxSize()) {
            val height = size.height / 2
            drawRect(
                color = Color.Black.copy(alpha = alpha),
                size = Size(size.width, height)
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DoWorkoutFooterWithModal(
    totalTime: ExerciseTime,
    timeLeft: ExerciseTime,
    exercise: ExerciseDto,
    currentSetNumber: Int, //Used for CurrentExerciseInfoRow
    defaultTotalSets: Int, //Used for CurrentExerciseInfoRow
    isNextExercise: Boolean,
    onSaveExerciseData: (ExerciseProgressDto) -> Unit
) {
    val currentSet = exercise.sets[currentSetNumber - 1]

    ExerciseDataSheetModal2(
        exercise = exercise,
        currentSetNumber = currentSetNumber,
        defaultTotalSets = defaultTotalSets,
        isNextExercise = isNextExercise,
        onSaveExerciseData = onSaveExerciseData
    ) {

        //Footer
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            contentAlignment = Alignment.BottomCenter
        ) {
            DoWorkoutFooter(
                totalTime = totalTime,
                timeLeft = timeLeft,
                currentSet = currentSet
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun DoWorkoutFooterWithModalPreview() {
    val time = ExerciseTime(hours = 0, minutes = 1, seconds = 30)
    val timeLeft = ExerciseTime(hours = 0, minutes = 1, seconds = 15)
    val exercise = MockupDataGeneratorV2.generateExercise()
    val currentSetNumber = 1
    val defaultTotalSets = 4
    val workoutTimer = TimerUtil(time.toSeconds())
    val onTimePassed: (ExerciseTime) -> Unit = {

    }
    val onSaveExerciseData: (ExerciseProgressDto) -> Unit = {

    }

    DoWorkoutFooterWithModal(
        totalTime = time,
        timeLeft = timeLeft,
        exercise = exercise,
        currentSetNumber = currentSetNumber,
        defaultTotalSets = defaultTotalSets,
        isNextExercise = false,
        onSaveExerciseData = onSaveExerciseData
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DoWorkoutFooter(
    exerciseTimerStyle: ExerciseTimerStyle = ExerciseTimerStyle(
        lineColor = MaterialTheme.colorScheme.outline
    ),
    totalTime: ExerciseTime,
    timeLeft: ExerciseTime,
    currentSet: ExerciseSetDto
) {
    val exerciseDataPadding = PaddingValues(4.dp)
    val textColor = MaterialTheme.colorScheme.onSurface

    val repsText = currentSet.reps.toString()
    val weightText = if (currentSet.weight == 0.0f) "--" else currentSet.weight.toString()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(exerciseTimerStyle.timerRadius * 1.5f)
    ) {

        //reps
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(
                    exerciseDataPadding
                ),
                text = repsText,
                style = TextStyle(
                    color = textColor,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                modifier = Modifier.padding(
                    exerciseDataPadding
                ),
                text = "Reps",
                style = TextStyle(
                    color = textColor,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        //timer
        ExerciseTimer(
            modifier = Modifier
                .fillMaxHeight()
                .weight(2.5f),
            timeLeft = timeLeft,
            totalTime = totalTime
        )

        //weight
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(
                    exerciseDataPadding
                ),
                text = weightText,
                style = TextStyle(
                    color = textColor,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                modifier = Modifier.padding(
                    exerciseDataPadding
                ),
                text = "Weight",
                style = TextStyle(
                    color = textColor,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun DoWorkoutFooterPreview() {
    val time = ExerciseTime(hours = 0, minutes = 1, seconds = 30)
    val timeLeft = ExerciseTime(hours = 0, minutes = 1, seconds = 15)
    val currentSet = MockupDataGeneratorV2.generateExerciseSet(
        workoutId = Random.nextInt(1, 100),
        exerciseId = Random.nextInt(1, 100)
    )
    val workoutTimer = TimerUtil(time.toSeconds())
    val onTimePassed: (ExerciseTime) -> Unit = {

    }
    DoWorkoutFooter(
        totalTime = time,
        timeLeft = timeLeft,
        currentSet = currentSet
    )
}


//When set list is empty there are no rows showing
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseDataSheetModal() {
    val sheetState = rememberModalBottomSheetState()
    var isSheetOpen by rememberSaveable {
        mutableStateOf(false)
    }

    if (isSheetOpen) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = {
                isSheetOpen = false
            },
            dragHandle = {
                GrabHandle()
            }
        ) {
            Image(
                painter = painterResource(id = R.drawable.background_muscle_default),
                contentDescription = null
            )
        }
    }
}

@Preview
@Composable
fun ExerciseDataSheetModalPreview() {
    ExerciseDataSheetModal()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseDataSheetModal2(
    exercise: ExerciseDto,
    currentSetNumber: Int,
    defaultTotalSets: Int,
    isNextExercise: Boolean = false,
    onSaveExerciseData: (ExerciseProgressDto) -> Unit,
    content: @Composable (paddingValues: PaddingValues) -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    val sheetPeekHeight = 88.dp

    var updatedExerciseWithProgressSets by remember {
        mutableStateOf(
            ExerciseProgressDto(
                exerciseId = exercise.exerciseId,
                workoutId = exercise.workoutId,
                name = exercise.name,
                muscleGroup = exercise.muscleGroup,
                machineType = exercise.machineType,
                snapshot = exercise.snapshot,
                sets = emptyList(),
            )
        )
    }

    //Sets are inserted in exercise internally
    val onExerciseDataChange: (ExerciseProgressDto) -> Unit = { updatedExercise ->
        updatedExerciseWithProgressSets = updatedExercise
    }

    //Save data before changing the exercise in ExerciseDataSheet
    LaunchedEffect(isNextExercise) {
        if (isNextExercise) {
            onSaveExerciseData(updatedExerciseWithProgressSets)
        }
    }

    val scaffoldState = rememberBottomSheetScaffoldState()
    BottomSheetScaffold(
        modifier = Modifier
            .fillMaxWidth(),
        scaffoldState = scaffoldState,
        sheetContent = {
            CurrentExerciseInfoRow(
                currentExercise = exercise,
                currentSetNumber = currentSetNumber,
                defaultTotalSets = defaultTotalSets
            )

            ExerciseDataSheet(
                exercise = exercise,
                onExerciseDataChange = onExerciseDataChange
            )
        },
        sheetDragHandle = {
            GrabHandle()
        },
        sheetPeekHeight = sheetPeekHeight
    ) {

        //Screen
        content(it)
    }
}

@Composable
fun CurrentExerciseInfoRow(
    currentExercise: ExerciseDto,
    currentSetNumber: Int,
    defaultTotalSets: Int
) {
    val textColor = MaterialTheme.colorScheme.onSurface
    val setsTextColor = Color.Yellow
    val cornerSize = 24.dp

    val totalSets =
        if (currentExercise.sets.isNotEmpty()) currentExercise.sets.size else defaultTotalSets

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clip(
                RoundedCornerShape(cornerSize)
            )
            .border(
                border = BorderStroke(2.dp, color = MaterialTheme.colorScheme.outline),
                shape = RoundedCornerShape(cornerSize)
            )
            .background(MaterialTheme.colorScheme.onSurfaceVariant), //outline
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier
                .padding(
                    horizontal = 8.dp
                )
                .weight(5f),
            text = currentExercise.name,
            style = TextStyle(
                color = textColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            ),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            modifier = Modifier
                .padding(
                    horizontal = 8.dp
                )
                .weight(1f),
            text = "$currentSetNumber of $totalSets",
            style = TextStyle(
                color = setsTextColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            ),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview
@Composable
fun CurrentExerciseInfoRowInfoRowPreview() {
    val currentExercise = MockupDataGeneratorV2.generateExercise()
    val currentSetNumber = 1
    val defaultTotalSets = 4
    CurrentExerciseInfoRow(
        currentExercise = currentExercise,
        currentSetNumber = currentSetNumber,
        defaultTotalSets = defaultTotalSets
    )
}

@Preview
@Composable
fun ExerciseDataSheetModal2Preview() {
    val exercise = MockupDataGeneratorV2.generateExercise()
    val currentSetNumber = 1
    val defaultTotalSets = 4
    val onSaveExerciseData: (ExerciseProgressDto) -> Unit = {}
    ExerciseDataSheetModal2(
        exercise = exercise,
        currentSetNumber = currentSetNumber,
        defaultTotalSets = defaultTotalSets,
        isNextExercise = false,
        onSaveExerciseData = onSaveExerciseData
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.onSurface)
        )
    }
}

@Composable
fun GrabHandle(modifier: Modifier = Modifier) {
    val configuration = LocalConfiguration.current

    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    Box(
        modifier = modifier
            .width(screenWidth / 2 - 75.dp) //.fillMaxWidth()
            .padding(16.dp)
            .height(5.dp)
            .background(
                color = MaterialTheme.colorScheme.onSurface,
                shape = RoundedCornerShape(50)
            )
    )
}

@Preview
@Composable
fun GrabHandlePreview() {
    GrabHandle()
}
