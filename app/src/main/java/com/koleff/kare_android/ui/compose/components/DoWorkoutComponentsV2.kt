package com.koleff.kare_android.ui.compose.components

//import android.graphics.Paint
import WorkoutConfigurationOption
import android.graphics.Paint.Style
import android.os.Build
import android.util.Log
import androidx.annotation.FloatRange
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.koleff.kare_android.R
import com.koleff.kare_android.common.MockupDataGeneratorV2
import com.koleff.kare_android.common.timer.TimerUtil
import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.model.dto.ExerciseProgressDto
import com.koleff.kare_android.data.model.dto.ExerciseSetDto
import com.koleff.kare_android.data.model.dto.ExerciseSetProgressDto
import com.koleff.kare_android.data.model.dto.ExerciseTime
import com.koleff.kare_android.ui.state.ExerciseTimerStyle
import com.koleff.kare_android.ui.theme.LocalExtendedColors
import com.pierfrancescosoffritti.androidyoutubeplayer.core.customui.DefaultPlayerUiController
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.loadOrCueVideo
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.random.Random

//NextExerciseInfoScreen

@Composable
fun MiniRow(text: String, progressText: String) {
    val labelTextColor = LocalExtendedColors.current.label
    val labelTextStyle = MaterialTheme.typography.labelMedium.copy(
        color = labelTextColor,
    )

    val cornerSize = 32.dp
    val outlineColor = MaterialTheme.colorScheme.outlineVariant
    val tintColor = LocalExtendedColors.current.title

    Row(
        modifier = Modifier
            .padding(6.dp)
            .height(30.dp)
            .clip(RoundedCornerShape(cornerSize))
            .border(
                border = BorderStroke(2.dp, color = outlineColor),
                shape = RoundedCornerShape(cornerSize)
            )
            .background(
                color = Color.Transparent,
                shape = RoundedCornerShape(cornerSize)
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 6.dp),
            text = text,
            style = labelTextStyle,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            modifier = Modifier.padding(horizontal = 6.dp),
            text = progressText,
            style = labelTextStyle.copy(color = tintColor),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun CurrentSetInfo(currentSet: Int, totalSets: Int) {
    MiniRow("Set", "$currentSet/$totalSets")
}

@Preview
@Composable
private fun CurrentSetInfoPreview() {
    CurrentSetInfo(
        currentSet = 2,
        totalSets = 4
    )
}

@Composable
fun CurrentRepsInfo(reps: Int) {
    MiniRow("Reps", "$reps")
}

@Preview
@Composable
private fun CurrentRepsInfoPreview() {
    CurrentRepsInfo(
        reps = 12
    )
}

@Composable
fun CurrentWeightInfo(weight: Float, weightType: String = "kg") {
    MiniRow("Weight", "$weight $weightType")
}

@Preview
@Composable
private fun CurrentWeightInfoPreview() {
    CurrentWeightInfo(
        weight = 225.0f
    )
}


@Composable
fun PagerIndicator(currentPage: Int, totalPages: Int) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.padding(top = 20.dp)
    ) {
        repeat(totalPages) {
            Indicator(isSelected = it + 1 <= currentPage)
        }
    }
}

@Composable
fun Indicator(isSelected: Boolean, color: Color = MaterialTheme.colorScheme.primary) {
    val width = animateDpAsState(targetValue = if (isSelected) 40.dp else 10.dp)

    Box(
        modifier = Modifier
            .padding(4.dp)
            .height(10.dp)
            .width(width.value)
            .clip(CircleShape)
            .background(
                if (isSelected) color else Color.Gray.copy(alpha = 0.5f)
            )
    )
}

@ExperimentalPagerApi
@Composable
fun rememberPagerState(
    @androidx.annotation.IntRange(from = 0) pageCount: Int,
    @androidx.annotation.IntRange(from = 1) initialPage: Int = 1,
    @FloatRange(from = 0.0, to = 1.0) initialPageOffset: Float = 0f,
    @androidx.annotation.IntRange(from = 1) initialOffscreenLimit: Int = 1,
    infiniteLoop: Boolean = false
): PagerState = rememberSaveable(saver = PagerState.Saver) {
    PagerState(
        pageCount = pageCount,
        currentPage = initialPage,
        currentPageOffset = initialPageOffset,
        offscreenLimit = initialOffscreenLimit,
        infiniteLoop = infiniteLoop
    )
}

@OptIn(ExperimentalPagerApi::class)
@Preview
@Composable
private fun NextExercisePagerIndicatorPreview() {
    val pagerState = rememberPagerState(pageCount = 4, initialPage = 1)

    Column {
        PagerIndicator(currentPage = pagerState.currentPage, totalPages = pagerState.pageCount)

        Row {
            Button(onClick = {
                GlobalScope.launch {
                    pagerState.scrollToPage(
                        if (pagerState.currentPage + 1 >= pagerState.pageCount) pagerState.pageCount - 1 else pagerState.currentPage + 1,
                        pageOffset = 0f
                    )
                }
            }) {
                Text(text = "Next")
            }

            Spacer(modifier = Modifier.width(10.dp))

            Button(onClick = {
                GlobalScope.launch {
                    pagerState.scrollToPage(
                        if (pagerState.currentPage - 1 <= 0) 1 else pagerState.currentPage - 1,
                        pageOffset = 0f
                    )
                }
            }) {
                Text(text = "Previous")
            }
        }
    }
}


@Preview
@Composable
private fun DescriptionBox2Preview() {
    DescriptionBox(
        title = "Exercise name",
        description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc interdum nibh nec pharetra iaculis. Aenean ultricies egestas leo at ultricies.",
        hasDivider = false
    )
}

@Composable
fun PauseButton(onClick: () -> Unit, isPause: Boolean) {
    WorkoutConfigurationOption(
        if (isPause) R.drawable.ic_resume else R.drawable.pause,
        onClick
    )
}

@Preview
@Composable
private fun PauseButtonPreview() {
    PauseButton(
        onClick = {},
        isPause = Random.nextBoolean()
    )
}

@Composable
fun SkipButton(onClick: () -> Unit) {
    WorkoutConfigurationOption(
        R.drawable.ic_forward,
        onClick
    )
}

@Preview
@Composable
private fun SkipButtonPreview() {
    SkipButton(
        onClick = {}
    )
}

@Preview
@Composable
private fun PagerIndicatorWithButtonsPreview() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        PauseButton(onClick = {}, isPause = Random.nextBoolean())
        PagerIndicator(currentPage = 0, totalPages = 4)
        SkipButton(onClick = {})
    }
}

@Preview
@Composable
private fun RepsSetAndWeightInfoRowPreview() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        CurrentSetInfo(1, 4)
        CurrentRepsInfo(12)
        CurrentWeightInfo(225.0f)
    }
}

@Composable
fun NextExerciseInfoScreen(
    nextExercise: ExerciseDto,
    set: Int,
    reps: Int,
    weight: Float,
    totalSets: Int,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onSkipSet: () -> Unit,
    isWorkoutComplete: Boolean = false,
    countdownTime: ExerciseTime,
) {
    var isPause by remember {
        mutableStateOf(false)
    }

    //TODO: add vertical video player
    val alpha = 0.3f
    val backgroundColors = listOf(
        MaterialTheme.colorScheme.primary.copy(alpha = alpha),
        MaterialTheme.colorScheme.primary.copy(alpha = alpha),
        MaterialTheme.colorScheme.primary.copy(alpha = alpha),
        MaterialTheme.colorScheme.secondary.copy(alpha = alpha),
        MaterialTheme.colorScheme.secondary.copy(alpha = alpha),
        MaterialTheme.colorScheme.tertiary.copy(alpha = alpha),
        MaterialTheme.colorScheme.tertiary.copy(alpha = alpha),
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .drawBehind {
                drawRect(
                    brush = Brush.linearGradient(backgroundColors),
                    blendMode = BlendMode.Darken
                )
            },
    ) {

        //Time countdown until next exercise
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            RestTime(countdownTime)
        }

        Column(
            modifier = Modifier
                .weight(1f),
            verticalArrangement = Arrangement.Bottom
        ) {
            DescriptionBox(
                title = nextExercise.name,
                description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc interdum nibh nec pharetra iaculis. Aenean ultricies egestas leo at ultricies.", //nextExercise.description
                hasDivider = false
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                CurrentSetInfo(currentSet = set, totalSets = totalSets)
                CurrentRepsInfo(reps = reps)
                CurrentWeightInfo(weight = weight)
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 64.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                PauseButton(
                    onClick = {
                        isPause = !isPause

                        if (isPause) onPause() else onResume()
                    }, isPause = isPause
                )

                Log.d("NextExerciseInfoScreen", "Current set: $set | Total sets: $totalSets")
                PagerIndicator(currentPage = set, totalPages = totalSets)
                SkipButton(onClick = {
                    isPause = false

                    onSkipSet()
                })
            }

            //Exercise data sheet margin
            Spacer(modifier = Modifier.height(64.dp))
        }
    }
}

@Preview
@Composable
fun NextExerciseInfoScreenPreview() {
    val nextExercise = MockupDataGeneratorV2.generateExercise()
    val countdownTime = ExerciseTime(hours = 0, minutes = 0, seconds = 10)
    NextExerciseInfoScreen(
        nextExercise = nextExercise,
        set = 2,
        reps = 12,
        weight = 225.0f,
        totalSets = 4,
        onPause = {},
        onResume = {},
        onSkipSet = {},
        isWorkoutComplete = false,
        countdownTime = countdownTime,
    )
}

@Composable
fun RestTime(
    countdownTime: ExerciseTime
) {
    val countdownNumberPadding = PaddingValues(vertical = 12.dp, horizontal = 6.dp)
    val textColor = LocalExtendedColors.current.title
    val timerTextStyle = MaterialTheme.typography.displayLarge.copy(
        color = textColor
    )

    Text(
        modifier = Modifier.padding(countdownNumberPadding),
        text = countdownTime.toSeconds().toString(),
        style = timerTextStyle,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
    )
}

@Preview
@Composable
private fun RestTimePreview() {
    RestTime(countdownTime = ExerciseTime(0, 0, 10))
}

//DoWorkoutScreen

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
    val textColor = LocalExtendedColors.current.title
    val outlineColor = MaterialTheme.colorScheme.outlineVariant
    val backgroundColor = MaterialTheme.colorScheme.secondary

    val exerciseNameTextStyle = MaterialTheme.typography.titleMedium.copy(
        color = textColor
    )

    val setsTextColor = LocalExtendedColors.current.title
    val setTextStyle = MaterialTheme.typography.titleSmall.copy(
        color = setsTextColor
    )

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
                border = BorderStroke(2.dp, color = outlineColor),
                shape = RoundedCornerShape(cornerSize)
            )
            .background(backgroundColor),
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
            style = exerciseNameTextStyle,
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
            style = setTextStyle,
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

@Composable
fun ExerciseDataSheet(
    exercise: ExerciseDto,
    onExerciseDataChange: (ExerciseProgressDto) -> Unit
) {
    var sets by remember {
        mutableStateOf(
            exercise.sets.map {
                ExerciseSetProgressDto(
                    baseSet = it,
                    isDone = false
                )
            }
        )
    }

    //When a set is changed, update the list of sets.
    fun handleSetChange(updatedSet: ExerciseSetProgressDto) {

        //Updates
        sets = sets.map {
            if (it.baseSet.setId == updatedSet.baseSet.setId) updatedSet else it
        }

        //Callback
        onExerciseDataChange(
            ExerciseProgressDto(
                exerciseId = exercise.exerciseId,
                workoutId = exercise.workoutId,
                name = exercise.name,
                muscleGroup = exercise.muscleGroup,
                machineType = exercise.machineType,
                snapshot = exercise.snapshot,
                sets = sets,
            )
        )
    }

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    val cornerSize = 24.dp
    val outlineColor = MaterialTheme.colorScheme.outlineVariant
    val backgroundColor = MaterialTheme.colorScheme.secondary
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(screenHeight / 3)
            .clip(RoundedCornerShape(cornerSize))
            .border(
                border = BorderStroke(2.dp, color = outlineColor),
                shape = RoundedCornerShape(cornerSize)
            )
            .background(backgroundColor)
    ) {
        HorizontalLineWithText("Exercise data sheet")

        ExerciseDataSheetTitleRow()

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(screenHeight / 4)
        ) {

            //TODO: add functionality when last set has data typed in to add new row...
            items(exercise.sets.size) { setId ->
                ExerciseDataSheetRow(
                    set = exercise.sets[setId],
                    onSetChange = ::handleSetChange
                )
            }
        }
    }
}

@Composable
fun ExerciseDataSheetTitleRow() {
    val textColor = LocalExtendedColors.current.title
    val textStyle = MaterialTheme.typography.titleMedium.copy(
        color = textColor
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(35.dp)

            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        //Set number
        Text(
            modifier = Modifier
                .padding(4.dp)
                .weight(0.5f),
            text = "Set",
            style = textStyle,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        //Reps
        Text(
            modifier = Modifier
                .padding(4.dp)
                .weight(1.5f),
            text = "Reps",
            style = textStyle,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        //Weight
        Text(
            modifier = Modifier
                .padding(4.dp)
                .weight(1.5f),
            text = "Weight",
            style = textStyle,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        //Is done checkbox
        Text(
            modifier = Modifier
                .padding(4.dp)
                .weight(1f),
            text = "Done",
            style = textStyle,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun ExerciseDataSheetRow(
    set: ExerciseSetDto,
    onSetChange: (ExerciseSetProgressDto) -> Unit
) {

    //Keyboard
    val repsFocusRequester = remember { FocusRequester() }
    val weightFocusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val textColor = LocalExtendedColors.current.label
    val textStyle = MaterialTheme.typography.titleMedium.copy(
        color = textColor
    )

    val checkboxSelectedColor = Color.Green
    val checkboxBorderColor = MaterialTheme.colorScheme.outlineVariant

    //Forcing Re-composition on set change -> new exercise in ExerciseDataSheet
    key(set.setId) {
        var reps by remember { mutableStateOf(set.reps.toString()) }
        var weight by remember { mutableStateOf(set.weight.toString()) }
        var isDone by remember { mutableStateOf(false) }

        //On data change -> callback called and updates up the ladder via state hoisting
        LaunchedEffect(reps, weight, isDone) {
            onSetChange(
                ExerciseSetProgressDto(
                    baseSet = set.copy(
                        reps = reps.toIntOrNull() ?: set.reps,
                        weight = weight.toFloatOrNull() ?: set.weight
                    ),
                    isDone = isDone
                )
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            //Set number
            Text(
                modifier = Modifier
                    .padding(4.dp)
                    .weight(0.5f),
                text = set.number.toString(),
                style = textStyle,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            //Reps
            ExerciseDataSheetTextField(
                modifier = Modifier
                    .padding(4.dp)
                    .weight(1.5f)
                    .focusRequester(repsFocusRequester),
                text = reps,
                onValueChange = {
                    reps = it

                    //Calls launched effect...
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        weightFocusRequester.requestFocus()
                    }
                )
            )

            //Weight
            ExerciseDataSheetTextField(
                modifier = Modifier
                    .padding(4.dp)
                    .weight(1.5f)
                    .focusRequester(weightFocusRequester),
                text = weight,
                onValueChange = {
                    weight = it

                    //Calls launched effect...
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    }
                )
            )

            //Checkbox
            Checkbox(
                modifier = Modifier
                    .weight(1f),
                colors = CheckboxDefaults.colors(
                    checkedColor = checkboxSelectedColor,
                    uncheckedColor = checkboxBorderColor //Checkbox border
                ),
                checked = isDone,
                onCheckedChange = {
                    isDone = it

                    //Calls launched effect...
                }
            )
        }
    }
}

@Composable
fun ExerciseDataSheetTextField(
    modifier: Modifier = Modifier,
    text: String,
    onValueChange: (String) -> Unit,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    val cornerSize = 16.dp
    val textColor = LocalExtendedColors.current.label
    val outlineColor = MaterialTheme.colorScheme.outlineVariant
    val backgroundColor = MaterialTheme.colorScheme.secondary

    val textStyle = MaterialTheme.typography.titleSmall.copy(
        color = textColor
    )

    TextField(
        modifier = modifier
            .clip(RoundedCornerShape(cornerSize))
            .border(
                border = BorderStroke(2.dp, color = outlineColor),
                shape = RoundedCornerShape(cornerSize)
            )
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(cornerSize)
            ),
        value = text,
        textStyle = textStyle,
        onValueChange = onValueChange,
        keyboardActions = keyboardActions,
        keyboardOptions = keyboardOptions
    )
}

@Preview
@Composable
fun ExerciseDataSheetRowPreview() {
    val exerciseSet = MockupDataGeneratorV2.generateExerciseSet(
        workoutId = Random.nextInt(1, 100),
        exerciseId = Random.nextInt(1, 100)
    )
    val onSetChanged: (ExerciseSetProgressDto) -> Unit = { set ->

    }
    ExerciseDataSheetRow(exerciseSet, onSetChanged)
}

@Preview
@Composable
fun ExerciseDataSheetPreview() {
    val exercise = MockupDataGeneratorV2.generateExercise()
    val onExerciseDataChange: (ExerciseProgressDto) -> Unit = {

    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        ExerciseDataSheet(exercise, onExerciseDataChange)
    }
}

@Preview
@Composable
fun ExerciseDataSheetTextFieldPreview() {
    ExerciseDataSheetTextField(text = "50.0", onValueChange = {})
}

@Composable
fun ExerciseTimerV2(
    modifier: Modifier = Modifier,
    totalTime: ExerciseTime,
    timeLeft: ExerciseTime,
    exerciseTimerStyle: ExerciseTimerStyle = ExerciseTimerStyle()
) {
    val strokeWidth = 10f
    val textColor =
        if (isSystemInDarkTheme()) android.graphics.Color.WHITE else android.graphics.Color.BLACK
    val lineColor = exerciseTimerStyle.lineColor

    Canvas(modifier = modifier) {
        val circleCenter = Offset(size.width / 2, size.height / 2) // center of the canvas
        val path = Path()

        // Loop to create the radial lines and add to the path
        for (i in 0 until exerciseTimerStyle.totalLines) {
            val lineLength = exerciseTimerStyle.lineLength.toPx()
            val angleInRadian =
                Math.toRadians(i * -180.0 / (exerciseTimerStyle.totalLines - 1)).toFloat()
            val outerRadius = exerciseTimerStyle.timerRadius.toPx()

            val lineStart = Offset(
                x = (outerRadius - lineLength) * cos(angleInRadian) + circleCenter.x,
                y = (outerRadius - lineLength) * sin(angleInRadian) + circleCenter.y
            )

            val lineEnd = Offset(
                x = outerRadius * cos(angleInRadian) + circleCenter.x,
                y = outerRadius * sin(angleInRadian) + circleCenter.y
            )

            // Calculate the perpendicular vector to create a rectangle

            val dx = lineEnd.x - lineStart.x
            val dy = lineEnd.y - lineStart.y
            val length = sqrt(dx * dx + dy * dy)
            val unitDx = dx / length
            val unitDy = dy / length

            // Perpendicular direction for the width of the line
            val perpendicularDx = -unitDy * strokeWidth / 2
            val perpendicularDy = unitDx * strokeWidth / 2

            // Define the four corners of the rectangle
            val rectStart = Offset(lineStart.x + perpendicularDx, lineStart.y + perpendicularDy)
            val rectEnd = Offset(lineEnd.x + perpendicularDx, lineEnd.y + perpendicularDy)
            val rectStartOpposite =
                Offset(lineStart.x - perpendicularDx, lineStart.y - perpendicularDy)
            val rectEndOpposite = Offset(lineEnd.x - perpendicularDx, lineEnd.y - perpendicularDy)

            // Draw a rectangle (four sides of the path)
            path.moveTo(rectStart.x, rectStart.y)
            path.lineTo(rectEnd.x, rectEnd.y)
            path.lineTo(rectEndOpposite.x, rectEndOpposite.y)
            path.lineTo(rectStartOpposite.x, rectStartOpposite.y)
            path.close()
        }


        clipPath(path, clipOp = ClipOp.Intersect) {
            val sweepAngle =
                (timeLeft.toSeconds().toFloat() / totalTime.toSeconds()
                    .toFloat()) * -180f // Calculate fill percentage
//            Log.d("Sweep angle", "$sweepAngle")

            drawArc(
                color = exerciseTimerStyle.elapsedLineColor,
                startAngle = sweepAngle,
                sweepAngle = -180f,
                useCenter = false,
                topLeft = Offset(
                    center.x - exerciseTimerStyle.timerRadius.toPx(),
                    center.y - exerciseTimerStyle.timerRadius.toPx()
                ),
                size = Size(
                    exerciseTimerStyle.timerRadius.toPx() * 2,
                    exerciseTimerStyle.timerRadius.toPx() * 2 + strokeWidth
                ),
                style = Fill
            )
        }

        // Draw the radial lines as before (outside of the clip region)
        drawPath(
            path = path,
            color = lineColor,
            style = Stroke(width = 1f) // Stroke style for the radial lines
        )

        //Timer display
        drawContext.canvas.nativeCanvas.apply {
            drawText(
                timeLeft.toString(),
                circleCenter.x,
                circleCenter.y - 10.dp.toPx(),
                android.graphics.Paint().apply {
                    color = textColor
                    style = Style.FILL
                    textSize = 20.sp.toPx()
                    textAlign = android.graphics.Paint.Align.CENTER
                }
            )
        }
    }
}

@Preview
@Composable
fun ExerciseTimerV2Preview() {
    val totalTime = ExerciseTime(hours = 0, minutes = 3, seconds = 30)
    var currentTime by remember {
        mutableStateOf(ExerciseTime(totalTime.hours, totalTime.minutes, totalTime.seconds))
    }
    val exerciseTimerStyle = ExerciseTimerStyle()
    val workoutTimer = TimerUtil()
    LaunchedEffect(Unit) {
        workoutTimer.startTimer(totalTime.toSeconds()) {
            currentTime = it
        }
    }

    ExerciseTimerV2(
        modifier = Modifier
            .fillMaxSize()
//            .size(exerciseTimerStyle.timerRadius * 2)
            .background(MaterialTheme.colorScheme.surface),
        timeLeft = currentTime,
        totalTime = totalTime,
        exerciseTimerStyle = exerciseTimerStyle
    )
}

//@Preview
//@Composable
//fun ExerciseTimerV2Preview() {
//    val totalTime = 60
//    var remainingTime by remember { mutableStateOf(totalTime) }
//
//    LaunchedEffect(key1 = remainingTime) {
//        while (remainingTime > 0) {
//            delay(1000L) //TODO: make the timer more precise...
//            remainingTime -= 1
//            Log.d("test", remainingTime.toString())
//        }
//    }
//
//    ExerciseTimerV2(
//        totalTime = ExerciseTime(0, 0, totalTime),
//        timeLeft = ExerciseTime(0, 0, remainingTime)
//    )
//}
//@Composable
//fun DrawClippedArcAndLine() {
//    Canvas(modifier = Modifier.fillMaxSize()) {
//
//        val circlePath = Path().apply {
//            addArc(
//                oval = Rect(center, size.minDimension / 2),
//                startAngleDegrees = 0f,
//                sweepAngleDegrees = -180f
//            )
//        }
//
//        clipPath(circlePath, clipOp = ClipOp.Difference) {
//            drawLine(
//                start = Offset(0f, 0f),
//                end = Offset(center.x + 100f, center.y + 100f),
//                color = Color.Black.copy(alpha = 0.8f),
//                strokeWidth = 100f
//            )
//        }
//    }
//}


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
    exerciseTimerStyle: ExerciseTimerStyle = ExerciseTimerStyle(),
    totalTime: ExerciseTime,
    timeLeft: ExerciseTime,
    currentSet: ExerciseSetDto
) {
    val exerciseDataPadding = PaddingValues(4.dp)
    val textColor = LocalExtendedColors.current.title
    val textStyle = MaterialTheme.typography.titleMedium.copy(
        color = textColor
    )

    val labelTextStyle = MaterialTheme.typography.titleSmall.copy(
        color = textColor
    )

    val repsText = currentSet.reps.toString()
    val weightText = if (currentSet.weight == 0.0f) "--" else currentSet.weight.toString()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(exerciseTimerStyle.timerRadius)
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
                style = textStyle,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                modifier = Modifier.padding(
                    exerciseDataPadding
                ),
                text = "Reps",
                style = labelTextStyle,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        //timer
        ExerciseTimerV2(
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
                style = textStyle,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                modifier = Modifier.padding(
                    exerciseDataPadding
                ),
                text = "Weight",
                style = labelTextStyle,
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

//Vertical video player
@Composable
fun YoutubeVerticalVideoPlayer(
    modifier: Modifier = Modifier,
    lifecycleOwner: LifecycleOwner,
    videoUrl: String,
    onLoadingCompleted: () -> Unit
) {
    var isVideoInitialLoadingCompleted by remember { mutableStateOf(false) }

    val iFramePlayerOptions = IFramePlayerOptions.Builder()
        .controls(0)
        .fullscreen(0)
        .ivLoadPolicy(3)
        .ccLoadPolicy(0)
        .modestBranding(1)
        .autoplay(0)
//            .listType("playlist")
//            .list("P92RE0NV-5c")
        .build()

    var youTubePlayerRef by remember { mutableStateOf<YouTubePlayer?>(null) }
    LaunchedEffect(videoUrl) {
        Log.d("YoutubeVerticalVideoPlayer", "On new video url")

        youTubePlayerRef?.loadVideo(videoUrl, 0f)
    }

    AndroidView(
        modifier = modifier
            .alpha(0.65f)
            .padding(bottom = 60.dp), //Video title padding
        factory = { context ->
            YouTubePlayerView(context = context).apply {
                lifecycleOwner.lifecycle.addObserver(this)

                enableAutomaticInitialization = false

                val youtubePlayerView = this
                var currentVideoDuration = 0.0f

                initialize(
                    object : AbstractYouTubePlayerListener() {
                        override fun onReady(youTubePlayer: YouTubePlayer) {
                            Log.d("YoutubeVerticalVideoPlayer", "onReady")
                            youTubePlayerRef = youTubePlayer // Keep reference

                            matchParent()

                            val defaultPlayerUiController =
                                DefaultPlayerUiController(youtubePlayerView, youTubePlayer)
                            defaultPlayerUiController.showMenuButton(false)
                            defaultPlayerUiController.showDuration(false)
                            defaultPlayerUiController.showUi(false)
                            defaultPlayerUiController.showSeekBar(false)
                            defaultPlayerUiController.showFullscreenButton(false)
                            defaultPlayerUiController.showCurrentTime(false)
                            defaultPlayerUiController.showYouTubeButton(false)
                            defaultPlayerUiController.showVideoTitle(false)
                            setCustomPlayerUi(defaultPlayerUiController.rootView)

//                            youTubePlayer.loadOrCueVideo(lifecycleOwner.lifecycle, videoUrl, 0f)
                            youTubePlayer.loadVideo(videoUrl, 0f)
                        }

                        override fun onStateChange(
                            youTubePlayer: YouTubePlayer,
                            state: PlayerConstants.PlayerState
                        ) {
                            Log.d("YoutubeVerticalVideoPlayer", "onStateChange: $state")
                            if (state == PlayerConstants.PlayerState.PLAYING && !isVideoInitialLoadingCompleted) {
                                onLoadingCompleted()

                                Log.d("YoutubeVerticalVideoPlayer", "isVideoInitialLoadingCompleted: ${isVideoInitialLoadingCompleted}")
                                isVideoInitialLoadingCompleted = true
                                Log.d("YoutubeVerticalVideoPlayer", "isVideoInitialLoadingCompleted: ${isVideoInitialLoadingCompleted}")
                            }
                            super.onStateChange(youTubePlayer, state)
                        }

                        override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {
                            Log.d("YoutubeVerticalVideoPlayer", "onCurrentSecond: $second")
                            if (second >= currentVideoDuration - 1f) { //Cuts 1 second repeat without END state
                                youTubePlayer.seekTo(0f)
                            }

                            super.onCurrentSecond(youTubePlayer, second)
                        }

                        override fun onVideoDuration(
                            youTubePlayer: YouTubePlayer,
                            duration: Float
                        ) {
                            Log.d("YoutubeVerticalVideoPlayer", "onVideoDuration: $duration")

                            currentVideoDuration = duration
                            super.onVideoDuration(youTubePlayer, duration)
                        }
                    },
                    iFramePlayerOptions
                )
            }
        })
}

@Preview
@Composable
private fun YoutubeVerticalVideoPlayerPreview() {
    val lifecycleOwner = LocalLifecycleOwner.current
    val videoUrl = "_FkbD0FhgVE"
    val isVideoInitialLoadingCompleted = true
    YoutubeVerticalVideoPlayer(
        lifecycleOwner = lifecycleOwner,
        videoUrl = videoUrl
    ) {
    }
}

@Composable
fun PauseScreenOverlay(isPaused: Boolean) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier.size(75.dp),
            painter = painterResource(
                if (isPaused) R.drawable.ic_pause else R.drawable.ic_resume
            ),
            contentDescription = "Pause/Resume",
            contentScale = ContentScale.Crop
        )
    }
}