//package com.koleff.kare_android.ui.compose.components
//
//import android.graphics.Paint
//import android.graphics.Paint.Style
//import android.os.Build
//import android.util.Log
//import androidx.annotation.RequiresApi
//import androidx.compose.foundation.BorderStroke
//import androidx.compose.foundation.Canvas
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.ColumnScope
//import androidx.compose.foundation.layout.PaddingValues
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxHeight
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.BottomSheetScaffold
//import androidx.compose.material3.Checkbox
//import androidx.compose.material3.CheckboxDefaults
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.ModalBottomSheet
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextField
//import androidx.compose.material3.rememberBottomSheetScaffoldState
//import androidx.compose.material3.rememberModalBottomSheetState
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.key
//import androidx.compose.runtime.mutableFloatStateOf
//import androidx.compose.runtime.mutableIntStateOf
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.saveable.rememberSaveable
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.draw.drawBehind
//import androidx.compose.ui.geometry.Offset
//import androidx.compose.ui.geometry.Size
//import androidx.compose.ui.graphics.BlendMode
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.nativeCanvas
//import androidx.compose.ui.platform.LocalConfiguration
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.text.style.TextOverflow
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.koleff.kare_android.R
//import com.koleff.kare_android.common.manager.data.MockupDataGeneratorV2
//import com.koleff.kare_android.common.timer.TimerUtil
//import com.koleff.kare_android.data.model.dto.ExerciseDto
//import com.koleff.kare_android.data.model.dto.ExerciseProgressDto
//import com.koleff.kare_android.data.model.dto.ExerciseSetDto
//import com.koleff.kare_android.data.model.dto.ExerciseSetProgressDto
//import com.koleff.kare_android.data.model.dto.ExerciseTime
//import com.koleff.kare_android.ui.state.ExerciseTimerStyle
//import com.koleff.kare_android.ui.theme.LocalExtendedColors
//import kotlin.math.cos
//import kotlin.math.sin
//import kotlin.random.Random
//
//@RequiresApi(Build.VERSION_CODES.O)
//@Composable
//fun DoWorkoutFooterWithModal(
//    totalTime: ExerciseTime,
//    timeLeft: ExerciseTime,
//    exercise: ExerciseDto,
//    currentSetNumber: Int, //Used for CurrentExerciseInfoRow
//    defaultTotalSets: Int, //Used for CurrentExerciseInfoRow
//    isNextExercise: Boolean,
//    onSaveExerciseData: (ExerciseProgressDto) -> Unit
//) {
//    val currentSet = exercise.sets[currentSetNumber - 1]
//
//    ExerciseDataSheetModal2(
//        exercise = exercise,
//        currentSetNumber = currentSetNumber,
//        defaultTotalSets = defaultTotalSets,
//        isNextExercise = isNextExercise,
//        onSaveExerciseData = onSaveExerciseData
//    ) {
//
//        //Footer
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(it),
//            contentAlignment = Alignment.BottomCenter
//        ) {
//            DoWorkoutFooter(
//                totalTime = totalTime,
//                timeLeft = timeLeft,
//                currentSet = currentSet
//            )
//        }
//    }
//}
//
//@RequiresApi(Build.VERSION_CODES.O)
//@Preview
//@Composable
//fun DoWorkoutFooterWithModalPreview() {
//    val time = ExerciseTime(hours = 0, minutes = 1, seconds = 30)
//    val timeLeft = ExerciseTime(hours = 0, minutes = 1, seconds = 15)
//    val exercise = MockupDataGeneratorV2.generateExercise()
//    val currentSetNumber = 1
//    val defaultTotalSets = 4
//    val workoutTimer = TimerUtil(time.toSeconds())
//    val onTimePassed: (ExerciseTime) -> Unit = {
//
//    }
//    val onSaveExerciseData: (ExerciseProgressDto) -> Unit = {
//
//    }
//
//    DoWorkoutFooterWithModal(
//        totalTime = time,
//        timeLeft = timeLeft,
//        exercise = exercise,
//        currentSetNumber = currentSetNumber,
//        defaultTotalSets = defaultTotalSets,
//        isNextExercise = false,
//        onSaveExerciseData = onSaveExerciseData
//    )
//}
//
//@RequiresApi(Build.VERSION_CODES.O)
//@Composable
//fun DoWorkoutFooter(
//    exerciseTimerStyle: ExerciseTimerStyle = ExerciseTimerStyle(
//        lineColor = MaterialTheme.colorScheme.outline
//    ),
//    totalTime: ExerciseTime,
//    timeLeft: ExerciseTime,
//    currentSet: ExerciseSetDto
//) {
//    val exerciseDataPadding = PaddingValues(4.dp)
//    val textColor = MaterialTheme.colorScheme.onSurface
//    val textStyle = MaterialTheme.typography.titleMedium.copy(
//        color = textColor
//    )
//
//    val labelTextStyle = MaterialTheme.typography.titleSmall.copy(
//        color = textColor
//    )
//
//    val repsText = currentSet.reps.toString()
//    val weightText = if (currentSet.weight == 0.0f) "--" else currentSet.weight.toString()
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(exerciseTimerStyle.timerRadius * 1.5f)
//    ) {
//
//        //reps
//        Column(
//            modifier = Modifier
//                .weight(1f)
//                .fillMaxHeight(),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Text(
//                modifier = Modifier.padding(
//                    exerciseDataPadding
//                ),
//                text = repsText,
//                style = textStyle,
//                maxLines = 1,
//                overflow = TextOverflow.Ellipsis
//            )
//            Text(
//                modifier = Modifier.padding(
//                    exerciseDataPadding
//                ),
//                text = "Reps",
//                style = labelTextStyle,
//                maxLines = 1,
//                overflow = TextOverflow.Ellipsis
//            )
//        }
//
//        //timer
//        ExerciseTimerV2(
//            modifier = Modifier
//                .fillMaxHeight()
//                .weight(2.5f),
//            timeLeft = timeLeft,
//            totalTime = totalTime
//        )
//
//        //weight
//        Column(
//            modifier = Modifier
//                .weight(1f)
//                .fillMaxHeight(),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Text(
//                modifier = Modifier.padding(
//                    exerciseDataPadding
//                ),
//                text = weightText,
//                style = textStyle,
//                maxLines = 1,
//                overflow = TextOverflow.Ellipsis
//            )
//            Text(
//                modifier = Modifier.padding(
//                    exerciseDataPadding
//                ),
//                text = "Weight",
//                style = labelTextStyle,
//                maxLines = 1,
//                overflow = TextOverflow.Ellipsis
//            )
//        }
//    }
//}
//
//@RequiresApi(Build.VERSION_CODES.O)
//@Preview
//@Composable
//fun DoWorkoutFooterPreview() {
//    val time = ExerciseTime(hours = 0, minutes = 1, seconds = 30)
//    val timeLeft = ExerciseTime(hours = 0, minutes = 1, seconds = 15)
//    val currentSet = MockupDataGeneratorV2.generateExerciseSet(
//        workoutId = Random.nextInt(1, 100),
//        exerciseId = Random.nextInt(1, 100)
//    )
//    val workoutTimer = TimerUtil(time.toSeconds())
//    val onTimePassed: (ExerciseTime) -> Unit = {
//
//    }
//    DoWorkoutFooter(
//        totalTime = time,
//        timeLeft = timeLeft,
//        currentSet = currentSet
//    )
//}
//
//
////When set list is empty there are no rows showing
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun ExerciseDataSheetModal() {
//    val sheetState = rememberModalBottomSheetState()
//    var isSheetOpen by rememberSaveable {
//        mutableStateOf(false)
//    }
//
//    if (isSheetOpen) {
//        ModalBottomSheet(
//            sheetState = sheetState,
//            onDismissRequest = {
//                isSheetOpen = false
//            },
//            dragHandle = {
//                GrabHandle()
//            }
//        ) {
//            Image(
//                painter = painterResource(id = R.drawable.background_muscle_default),
//                contentDescription = null
//            )
//        }
//    }
//}
//
//@Preview
//@Composable
//fun ExerciseDataSheetModalPreview() {
//    ExerciseDataSheetModal()
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun ExerciseDataSheetModal2(
//    exercise: ExerciseDto,
//    currentSetNumber: Int,
//    defaultTotalSets: Int,
//    isNextExercise: Boolean = false,
//    onSaveExerciseData: (ExerciseProgressDto) -> Unit,
//    content: @Composable (paddingValues: PaddingValues) -> Unit
//) {
//    val configuration = LocalConfiguration.current
//    val screenHeight = configuration.screenHeightDp.dp
//
//    val sheetPeekHeight = 88.dp
//
//    var updatedExerciseWithProgressSets by remember {
//        mutableStateOf(
//            ExerciseProgressDto(
//                exerciseId = exercise.exerciseId,
//                workoutId = exercise.workoutId,
//                name = exercise.name,
//                muscleGroup = exercise.muscleGroup,
//                machineType = exercise.machineType,
//                snapshot = exercise.snapshot,
//                sets = emptyList(),
//            )
//        )
//    }
//
//    //Sets are inserted in exercise internally
//    val onExerciseDataChange: (ExerciseProgressDto) -> Unit = { updatedExercise ->
//        updatedExerciseWithProgressSets = updatedExercise
//    }
//
//    //Save data before changing the exercise in ExerciseDataSheet
//    LaunchedEffect(isNextExercise) {
//        if (isNextExercise) {
//            onSaveExerciseData(updatedExerciseWithProgressSets)
//        }
//    }
//
//    val scaffoldState = rememberBottomSheetScaffoldState()
//    BottomSheetScaffold(
//        modifier = Modifier
//            .fillMaxWidth(),
//        scaffoldState = scaffoldState,
//        sheetContent = {
//            CurrentExerciseInfoRow(
//                currentExercise = exercise,
//                currentSetNumber = currentSetNumber,
//                defaultTotalSets = defaultTotalSets
//            )
//
//            ExerciseDataSheet(
//                exercise = exercise,
//                onExerciseDataChange = onExerciseDataChange
//            )
//        },
//        sheetDragHandle = {
//            GrabHandle()
//        },
//        sheetPeekHeight = sheetPeekHeight
//    ) {
//
//        //Screen
//        content(it)
//    }
//}
//
//@Composable
//fun CurrentExerciseInfoRow(
//    currentExercise: ExerciseDto,
//    currentSetNumber: Int,
//    defaultTotalSets: Int
//) {
//    val textColor = MaterialTheme.colorScheme.onSurface
//    val outlineColor = MaterialTheme.colorScheme.outlineVariant
//    val backgroundColor = MaterialTheme.colorScheme.secondary
//
//    val exerciseNameTextStyle = MaterialTheme.typography.titleMedium.copy(
//        color = textColor
//    )
//
//    val setsTextColor = LocalExtendedColors.current.subtitle
//    val setTextStyle = MaterialTheme.typography.titleSmall.copy(
//        color = setsTextColor
//    )
//
//    val cornerSize = 24.dp
//
//    val totalSets =
//        if (currentExercise.sets.isNotEmpty()) currentExercise.sets.size else defaultTotalSets
//
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(50.dp)
//            .clip(
//                RoundedCornerShape(cornerSize)
//            )
//            .border(
//                border = BorderStroke(2.dp, color = outlineColor),
//                shape = RoundedCornerShape(cornerSize)
//            )
//            .background(backgroundColor),
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.SpaceBetween
//    ) {
//        Text(
//            modifier = Modifier
//                .padding(
//                    horizontal = 8.dp
//                )
//                .weight(5f),
//            text = currentExercise.name,
//            style = exerciseNameTextStyle,
//            maxLines = 2,
//            overflow = TextOverflow.Ellipsis
//        )
//
//        Text(
//            modifier = Modifier
//                .padding(
//                    horizontal = 8.dp
//                )
//                .weight(1f),
//            text = "$currentSetNumber of $totalSets",
//            style = setTextStyle,
//            maxLines = 2,
//            overflow = TextOverflow.Ellipsis
//        )
//    }
//}
//
//@Preview
//@Composable
//fun CurrentExerciseInfoRowInfoRowPreview() {
//    val currentExercise = MockupDataGeneratorV2.generateExercise()
//    val currentSetNumber = 1
//    val defaultTotalSets = 4
//    CurrentExerciseInfoRow(
//        currentExercise = currentExercise,
//        currentSetNumber = currentSetNumber,
//        defaultTotalSets = defaultTotalSets
//    )
//}
//
//@Preview
//@Composable
//fun ExerciseDataSheetModal2Preview() {
//    val exercise = MockupDataGeneratorV2.generateExercise()
//    val currentSetNumber = 1
//    val defaultTotalSets = 4
//    val onSaveExerciseData: (ExerciseProgressDto) -> Unit = {}
//    ExerciseDataSheetModal2(
//        exercise = exercise,
//        currentSetNumber = currentSetNumber,
//        defaultTotalSets = defaultTotalSets,
//        isNextExercise = false,
//        onSaveExerciseData = onSaveExerciseData
//    ) {
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(MaterialTheme.colorScheme.onSurface)
//        )
//    }
//}
//
//@Composable
//fun GrabHandle(modifier: Modifier = Modifier) {
//    val configuration = LocalConfiguration.current
//
//    val screenHeight = configuration.screenHeightDp.dp
//    val screenWidth = configuration.screenWidthDp.dp
//
//    Box(
//        modifier = modifier
//            .width(screenWidth / 2 - 75.dp) //.fillMaxWidth()
//            .padding(16.dp)
//            .height(5.dp)
//            .background(
//                color = MaterialTheme.colorScheme.onSurface,
//                shape = RoundedCornerShape(50)
//            )
//    )
//}
//
//@Preview
//@Composable
//fun GrabHandlePreview() {
//    GrabHandle()
//}
//
//@Composable
//fun ExerciseDataSheet(
//    exercise: ExerciseDto,
//    onExerciseDataChange: (ExerciseProgressDto) -> Unit
//) {
//    var sets by remember {
//        mutableStateOf(
//            exercise.sets.map {
//                ExerciseSetProgressDto(
//                    baseSet = it,
//                    isDone = false
//                )
//            }
//        )
//    }
//
//    //When a set is changed, update the list of sets.
//    fun handleSetChange(updatedSet: ExerciseSetProgressDto) {
//
//        //Updates
//        sets = sets.map {
//            if (it.baseSet.setId == updatedSet.baseSet.setId) updatedSet else it
//        }
//
//        //Callback
//        onExerciseDataChange(
//            ExerciseProgressDto(
//                exerciseId = exercise.exerciseId,
//                workoutId = exercise.workoutId,
//                name = exercise.name,
//                muscleGroup = exercise.muscleGroup,
//                machineType = exercise.machineType,
//                snapshot = exercise.snapshot,
//                sets = sets,
//            )
//        )
//    }
//
//    val configuration = LocalConfiguration.current
//    val screenHeight = configuration.screenHeightDp.dp
//
//    val cornerSize = 24.dp
//    val outlineColor = MaterialTheme.colorScheme.outlineVariant
//    val backgroundColor = MaterialTheme.colorScheme.secondary
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(screenHeight / 3)
//            .clip(RoundedCornerShape(cornerSize))
//            .border(
//                border = BorderStroke(2.dp, color = outlineColor),
//                shape = RoundedCornerShape(cornerSize)
//            )
//            .background(backgroundColor)
//    ) {
//        HorizontalLineWithText("Exercise data sheet")
//
//        ExerciseDataSheetTitleRow()
//
//        LazyColumn(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(screenHeight / 4)
//        ) {
//
//            //TODO: add functionality when last set has data typed in to add new row...
//            items(exercise.sets.size) { setId ->
//                ExerciseDataSheetRow(
//                    set = exercise.sets[setId],
//                    onSetChange = ::handleSetChange
//                )
//            }
//        }
//    }
//}
//
//@Composable
//fun ExerciseDataSheetTitleRow() {
//    val textColor = MaterialTheme.colorScheme.onSurface
//    val textStyle = MaterialTheme.typography.titleMedium.copy(
//        color = textColor
//    )
//
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(35.dp)
//
//            .padding(horizontal = 12.dp),
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//
//        //Set number
//        Text(
//            modifier = Modifier
//                .padding(4.dp)
//                .weight(0.5f),
//            text = "Set",
//            style = textStyle,
//            textAlign = TextAlign.Center,
//            maxLines = 1,
//            overflow = TextOverflow.Ellipsis
//        )
//
//        //Reps
//        Text(
//            modifier = Modifier
//                .padding(4.dp)
//                .weight(1.5f),
//            text = "Reps",
//            style = textStyle,
//            textAlign = TextAlign.Center,
//            maxLines = 1,
//            overflow = TextOverflow.Ellipsis
//        )
//
//        //Weight
//        Text(
//            modifier = Modifier
//                .padding(4.dp)
//                .weight(1.5f),
//            text = "Weight",
//            style = textStyle,
//            textAlign = TextAlign.Center,
//            maxLines = 1,
//            overflow = TextOverflow.Ellipsis
//        )
//
//        //Is done checkbox
//        Text(
//            modifier = Modifier
//                .padding(4.dp)
//                .weight(1f),
//            text = "Done",
//            style = textStyle,
//            textAlign = TextAlign.Center,
//            maxLines = 1,
//            overflow = TextOverflow.Ellipsis
//        )
//    }
//}
//
//@Composable
//fun ExerciseDataSheetRow(
//    set: ExerciseSetDto,
//    onSetChange: (ExerciseSetProgressDto) -> Unit
//) {
//    val textColor = MaterialTheme.colorScheme.onSurface
//    val textStyle = MaterialTheme.typography.titleMedium.copy(
//        color = textColor
//    )
//
//    val checkboxSelectedColor = Color.Green
//    val checkboxBorderColor = MaterialTheme.colorScheme.outlineVariant
//
//    //Forcing Re-composition on set change -> new exercise in ExerciseDataSheet
//    key(set.setId) {
//        var reps by remember { mutableStateOf(set.reps.toString()) }
//        var weight by remember { mutableStateOf(set.weight.toString()) }
//        var isDone by remember { mutableStateOf(false) }
//
//        //On data change -> callback called and updates up the ladder via state hoisting
//        LaunchedEffect(reps, weight, isDone) {
//            onSetChange(
//                ExerciseSetProgressDto(
//                    baseSet = set.copy(
//                        reps = reps.toIntOrNull() ?: set.reps,
//                        weight = weight.toFloatOrNull() ?: set.weight
//                    ),
//                    isDone = isDone
//                )
//            )
//        }
//
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(60.dp)
//                .padding(horizontal = 12.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//
//            //Set number
//            Text(
//                modifier = Modifier
//                    .padding(4.dp)
//                    .weight(0.5f),
//                text = set.number.toString(),
//                style = textStyle,
//                textAlign = TextAlign.Center,
//                maxLines = 1,
//                overflow = TextOverflow.Ellipsis
//            )
//
//            //Reps
//            ExerciseDataSheetTextField(
//                modifier = Modifier
//                    .padding(4.dp)
//                    .weight(1.5f),
//                text = reps,
//                onValueChange = {
//                    reps = it
//
//                    //Calls launched effect...
//                }
//            )
//
//            //Weight
//            ExerciseDataSheetTextField(
//                modifier = Modifier
//                    .padding(4.dp)
//                    .weight(1.5f),
//                text = weight,
//                onValueChange = {
//                    weight = it
//
//                    //Calls launched effect...
//                }
//            )
//
//            //Checkbox
//            Checkbox(
//                modifier = Modifier
//                    .weight(1f),
//                colors = CheckboxDefaults.colors(
//                    checkedColor = checkboxSelectedColor,
//                    uncheckedColor = checkboxBorderColor //Checkbox border
//                ),
//                checked = isDone,
//                onCheckedChange = {
//                    isDone = it
//
//                    //Calls launched effect...
//                }
//            )
//        }
//    }
//}
//
//@Composable
//fun ExerciseDataSheetTextField(
//    modifier: Modifier = Modifier,
//    text: String,
//    onValueChange: (String) -> Unit
//) {
//    val cornerSize = 16.dp
//    val textColor = MaterialTheme.colorScheme.onSurface
//    val outlineColor = MaterialTheme.colorScheme.outlineVariant
//    val backgroundColor = MaterialTheme.colorScheme.secondary
//
//    val textStyle = MaterialTheme.typography.titleSmall.copy(
//        color = textColor
//    )
//
//    TextField(
//        modifier = modifier
//            .clip(RoundedCornerShape(cornerSize))
//            .border(
//                border = BorderStroke(2.dp, color = outlineColor),
//                shape = RoundedCornerShape(cornerSize)
//            )
//            .background(
//                color = backgroundColor,
//                shape = RoundedCornerShape(cornerSize)
//            ),
//        value = text,
//        textStyle = textStyle,
//        onValueChange = onValueChange
//    )
//}
//
//@Preview
//@Composable
//fun ExerciseDataSheetRowPreview() {
//    val exerciseSet = MockupDataGeneratorV2.generateExerciseSet(
//        workoutId = Random.nextInt(1, 100),
//        exerciseId = Random.nextInt(1, 100)
//    )
//    val onSetChanged: (ExerciseSetProgressDto) -> Unit = { set ->
//
//    }
//    ExerciseDataSheetRow(exerciseSet, onSetChanged)
//}
//
//@Preview
//@Composable
//fun ExerciseDataSheetPreview() {
//    val exercise = MockupDataGeneratorV2.generateExercise()
//    val onExerciseDataChange: (ExerciseProgressDto) -> Unit = {
//
//    }
//    Box(
//        modifier = Modifier.fillMaxSize(),
//        contentAlignment = Alignment.BottomCenter
//    ) {
//        ExerciseDataSheet(exercise, onExerciseDataChange)
//    }
//}
//
//@Preview
//@Composable
//fun ExerciseDataSheetTextFieldPreview() {
//    ExerciseDataSheetTextField(text = "50.0", onValueChange = {})
//}
//
//@RequiresApi(Build.VERSION_CODES.O)
//@Composable
//fun ExerciseTimer(
//    modifier: Modifier = Modifier,
//    timeLeft: ExerciseTime,
//    totalTime: ExerciseTime,
//    exerciseTimerStyle: ExerciseTimerStyle = ExerciseTimerStyle(
//        lineColor = MaterialTheme.colorScheme.outline
//    ),
//    isLogging: Boolean = false
//) {
//    val timePercentageLeftState = remember {
//        mutableFloatStateOf(100.0f)
//    }
//
//    val markedLinesState = remember {
//        mutableIntStateOf(exerciseTimerStyle.totalLines)
//    }
//
//    val configuration = LocalConfiguration.current
//    val textColor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//        if (configuration.isNightModeActive) {
//            android.graphics.Color.WHITE
//        } else {
//            android.graphics.Color.BLACK
//        }
//    } else {
//
//        //No dark mode supported -> default color
//        android.graphics.Color.BLACK
//    }
//
//
//    // Update state variables within LaunchedEffect when timeLeft changes
//    LaunchedEffect(timeLeft) {
//        timePercentageLeftState.floatValue =
//            TimerUtil.calculateTimeLeftPercentage(totalTime, timeLeft)
//        markedLinesState.intValue = TimerUtil.calculateMarkedLines(
//            timePercentageLeftState.floatValue,
//            exerciseTimerStyle.totalLines
//        )
//
//        if (isLogging) {
//            Log.d(
//                "ExerciseTimer",
//                "Percentage of Time Left: ${timePercentageLeftState.floatValue}%"
//            )
//            Log.d("ExerciseTimer", "Marked Lines: ${markedLinesState.intValue}")
//        }
//    }
//
//    Canvas(modifier = modifier) {
//        drawContext.canvas.nativeCanvas.apply {
//            val circleCenter = Offset(
//                x = center.x,
//                y = center.y + (exerciseTimerStyle.timerRadius.toPx() / 2)
//            ) //height.toFloat()
//
//            //Draw circle
//            drawCircle(
//                circleCenter.x,
//                circleCenter.y,
//                exerciseTimerStyle.timerRadius.toPx(),
//                Paint().apply {
//                    strokeWidth = 5.dp.toPx()
//                    color = android.graphics.Color.TRANSPARENT
//                    style = Paint.Style.FILL_AND_STROKE
//                    setShadowLayer(
//                        60f,
//                        0f,
//                        0f,
//                        android.graphics.Color.argb(50, 0, 0, 0) //Black
//                    )
//                }
//            )
//
//            //Timer display
//            drawText(
//                timeLeft.toString(),
//                circleCenter.x,
//                circleCenter.y - 10.dp.toPx(),
//                Paint().apply {
//                    color = textColor
//                    style = Style.FILL
//                    textSize = 20.sp.toPx()
//                    textAlign = Paint.Align.CENTER
//                }
//            )
//
//            //TODO: add a layer that gradually fills the circle with the percentage taken of the time and clip it in the lines shape...
//
//            //Lines
//            for (i in 0..exerciseTimerStyle.totalLines) {
//                val lineLength = exerciseTimerStyle.lineLength.toPx()
//                val isElapsed = i >= markedLinesState.intValue
//                val lineColor =
//                    if (isElapsed) exerciseTimerStyle.elapsedLineColor else exerciseTimerStyle.lineColor
//
//                val angleInRadian = -Math.toRadians(i * 6.0).toFloat()
//                val outerRadius = exerciseTimerStyle.timerRadius.toPx()
//
//                val lineStart = Offset(
//                    x = (outerRadius - lineLength) * cos(angleInRadian) + circleCenter.x,
//                    y = (outerRadius - lineLength) * sin(angleInRadian) + circleCenter.y
//                )
//
//                val lineEnd = Offset(
//                    x = outerRadius * cos(angleInRadian) + circleCenter.x,
//                    y = outerRadius * sin(angleInRadian) + circleCenter.y
//                )
//
//                //Drawing timer lines
//                drawLine(
//                    color = lineColor,
//                    start = lineStart,
//                    end = lineEnd,
//                    strokeWidth = 5.dp.toPx()
//                )
//            }
//        }
//    }
//}
//
//@RequiresApi(Build.VERSION_CODES.O)
//@Preview
//@Composable
//fun ExerciseTimerPreview() {
//    val totalTime = ExerciseTime(hours = 0, minutes = 3, seconds = 30)
//    var currentTime by remember {
//        mutableStateOf(ExerciseTime(totalTime.hours, totalTime.minutes, totalTime.seconds))
//    }
//    val exerciseTimerStyle = ExerciseTimerStyle(
//        lineColor = MaterialTheme.colorScheme.outline
//    )
//    val workoutTimer = TimerUtil()
//    LaunchedEffect(Unit) {
//        workoutTimer.startTimer(totalTime.toSeconds()) {
//            currentTime = it
//        }
//    }
//
//    ExerciseTimer(
//        modifier = Modifier
//            .fillMaxSize()
////            .size(exerciseTimerStyle.timerRadius * 2)
//            .background(MaterialTheme.colorScheme.surface),
//        timeLeft = currentTime,
//        totalTime = totalTime
//    )
//}
//
//
////Cant be separate screen because of navigation delay...
////Rather be overlay on the existing DoWorkoutScreen blurring the background until it gets configured
//// (next exercise, reset timer, etc...)
//@Composable
//fun NextExerciseCountdownScreen(
//    nextExercise: ExerciseDto,
//    currentSetNumber: Int,
//    isWorkoutComplete: Boolean = false,
//    countdownTime: ExerciseTime,
//    defaultTotalSets: Int,
//    countdownNumberPadding: PaddingValues = PaddingValues(vertical = 12.dp, horizontal = 6.dp)
//) {
//    val alpha = 0.5f
//    val motivationalQuote = MockupDataGeneratorV2.generateMotivationalQuote()
//    val totalSets = if (nextExercise.sets.isNotEmpty()) nextExercise.sets.size else defaultTotalSets
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .drawBehind {
//                val colors = listOf(
//                    Color.Red.copy(alpha = alpha),
//                    Color.Red.copy(alpha = alpha),
//                    Color.Red.copy(alpha = alpha),
//                    Color.Blue.copy(alpha = alpha),
//                    Color.Blue.copy(alpha = alpha),
//                    Color.Cyan.copy(alpha = alpha),
//                    Color.Yellow.copy(alpha = alpha)
//                )
//                drawRect(
//                    brush = Brush.linearGradient(colors),
//                    blendMode = BlendMode.Darken
//                )
//            },
//        verticalArrangement = Arrangement.Top
//    ) {
//        val exerciseText = if (isWorkoutComplete) {
//            "Workout completed!"
//        } else {
//            "Next exercise: ${nextExercise.name}."
//        }
//
//        val setText =
//            "Currently doing set $currentSetNumber out of $totalSets total sets."
//
//        val textColor = MaterialTheme.colorScheme.onSurface
//        val titleTextStyle = MaterialTheme.typography.displaySmall.copy(
//            color = textColor
//        )
//
//        val subtitleTextStyle = MaterialTheme.typography.headlineSmall.copy(
//            color = textColor
//        )
//
//        val timerTextStyle = MaterialTheme.typography.displayLarge.copy(
//            color = textColor
//        )
//
//        //Texts
//        Text(
//            modifier = Modifier
//                .padding(vertical = 12.dp, horizontal = 6.dp),
//            text = exerciseText,
//            style = titleTextStyle,
//            maxLines = 3,
//            overflow = TextOverflow.Ellipsis
//        )
//
//        Text(
//            modifier = Modifier
//                .padding(vertical = 12.dp, horizontal = 6.dp),
//            text = setText,
//            style = subtitleTextStyle,
//            maxLines = 2,
//            overflow = TextOverflow.Ellipsis
//        )
//
//        //Time countdown until next exercise
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .weight(7f),
//            contentAlignment = Alignment.Center
//        ) {
//            Text(
//                modifier = Modifier.padding(countdownNumberPadding),
//                text = countdownTime.toSeconds().toString(),
//                style = timerTextStyle,
//                maxLines = 2,
//                overflow = TextOverflow.Ellipsis
//            )
//        }
//    }
//}
//
//@Preview
//@Composable
//fun NextExerciseCountdownScreenPreview() {
//    val nextExercise = MockupDataGeneratorV2.generateExercise()
//    val currentSetNumber = 2
//    val countdownTime = ExerciseTime(hours = 0, minutes = 0, seconds = 10)
//    val defaultTotalSets = 4
//    val onTimePassed: (ExerciseTime) -> Unit = {}
//    val countdownTimer = TimerUtil(countdownTime.toSeconds())
//    NextExerciseCountdownScreen(
//        nextExercise = nextExercise,
//        currentSetNumber = currentSetNumber,
//        countdownTime = countdownTime,
//        defaultTotalSets = defaultTotalSets
//    )
//}
//
//@Composable
//fun HalfTransparentColumn(alpha: Float = 0.3f, content: @Composable ColumnScope.() -> Unit) {
//    Column {
//        content()
//
//        Canvas(modifier = Modifier.fillMaxSize()) {
//            val height = size.height / 2
//            drawRect(
//                color = Color.Black.copy(alpha = alpha),
//                size = Size(size.width, height)
//            )
//        }
//    }
//}
