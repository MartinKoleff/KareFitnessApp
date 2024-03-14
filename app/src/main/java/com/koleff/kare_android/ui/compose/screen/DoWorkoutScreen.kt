package com.koleff.kare_android.ui.compose.screen

import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.koleff.kare_android.common.DegreeUtils
import com.koleff.kare_android.common.TimerUtil
import com.koleff.kare_android.common.navigation.Destination
import com.koleff.kare_android.common.navigation.NavigationEvent
import com.koleff.kare_android.data.model.dto.ExerciseTime
import com.koleff.kare_android.ui.compose.components.navigation_components.scaffolds.DoWorkoutScaffold
import com.koleff.kare_android.ui.state.ExerciseTimerStyle
import com.koleff.kare_android.ui.view_model.DoWorkoutViewModel
import kotlin.math.cos
import kotlin.math.sin

//TODO: screen between sets
// that counts down time before next set...
// (use the same for rest).
// Also display next exercise / set number (2 different cases).
// Have screen for finishing workout with stats...
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DoWorkoutScreen() {

    val time = ExerciseTime(hours = 0, minutes = 1, seconds = 30) //default time (wire with exercise time)

    //Navigation Callbacks
    val onNavigateBack = {
        doWorkoutViewModel.onNavigationEvent(
            NavigationEvent.ClearBackstackAndNavigateTo(Destination.Dashboard)
        )
    }

    DoWorkoutScaffold(
        screenTitle = "", //current exercise...
        onExitWorkoutAction = onNavigateBack
    ) {
        //Video player...

        DoWorkoutFooter(totalTime = time)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DoWorkoutFooter(
    exerciseTimerStyle: ExerciseTimerStyle = ExerciseTimerStyle(),
    totalTime: ExerciseTime
) {
    val exerciseDataPadding = PaddingValues(4.dp)
    val textColor = androidx.compose.ui.graphics.Color.White

    //Timer
    val hours = totalTime.hours
    val minutes = totalTime.minutes
    val seconds = totalTime.seconds
    var currentTime by remember {
        mutableStateOf(
            String.format(
                "%02d:%02d:%02d",
                hours,
                minutes,
                seconds
            )
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(exerciseTimerStyle.timerRadius)
    ) {

        //reps
        Column(
            modifier = Modifier.weight(1f).fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(
                    exerciseDataPadding
                ),
                text = "8",
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
        LaunchedEffect(Unit) {
            TimerUtil.startTimer(totalTime.toSeconds()) {
                currentTime = it
            }
        }

        ExerciseTimer(
            modifier = Modifier.weight(2.5f).size(exerciseTimerStyle.timerRadius),
            timeLeft = currentTime,
            totalTime = totalTime
        )

        //weight
        Column(
            modifier = Modifier.weight(1f).fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(
                    exerciseDataPadding
                ),
                text = "--",
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

    //TODO: add slider below for exercise data sheet...
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun DoWorkoutFooterPreview() {
    val time = ExerciseTime(hours = 0, minutes = 5, seconds = 30)
    DoWorkoutFooter(totalTime = time)
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ExerciseTimer(
    modifier: Modifier = Modifier,
    timeLeft: String,
    totalTime: ExerciseTime,
    exerciseTimerStyle: ExerciseTimerStyle = ExerciseTimerStyle()
) {
    var time: String by remember {
        mutableStateOf(timeLeft)
    }

    var hours: Float by remember {
        mutableFloatStateOf(time.split(":")[0].toFloat())
    }

    var minutes: Float by remember {
        mutableFloatStateOf(time.split(":")[1].toFloat())
    }

    var seconds: Float by remember {
        mutableFloatStateOf(time.split(":")[2].toFloat())
    }

    //When timeLeft updates -> update time...
    LaunchedEffect(timeLeft) {
        time = timeLeft
        hours = time.split(":")[0].toFloat()
        minutes = time.split(":")[1].toFloat()
        seconds = time.split(":")[2].toFloat()
    }

    //Time elapsed logic
    val timeLeftInSeconds = remember(time) {
        (hours * 3600 + minutes * 60 + seconds).toInt()
    }

    val percentageLeft = remember(timeLeftInSeconds) {
        timeLeftInSeconds.toFloat() / totalTime.toSeconds()
    }

    val linesToColorDifferently = remember(percentageLeft) {
        (exerciseTimerStyle.totalLines * percentageLeft).toInt()
    }

    Canvas(modifier = modifier) {
        drawContext.canvas.nativeCanvas.apply {
            val circleCenter = Offset(x = center.x, y = height.toFloat()) //center.y

            //Draw circle
            drawCircle(
                circleCenter.x,
                circleCenter.y,
                exerciseTimerStyle.timerRadius.toPx(),
                Paint().apply {
                    strokeWidth = 5.dp.toPx()
                    color = Color.TRANSPARENT
                    style = Paint.Style.FILL_AND_STROKE
                    setShadowLayer(
                        60f,
                        0f,
                        0f,
                        Color.argb(50, 0, 0, 0) //Black
                    )
                }
            )

            //Timer display
            drawText(
                time,
                circleCenter.x,
                circleCenter.y - 10.dp.toPx(),
                Paint().apply {
                    color = Color.WHITE
                    textSize = 20.sp.toPx()
                    textAlign = Paint.Align.CENTER
                }
            )

            //TODO: add a layer that gradually fills the circle with the percentage taken of the time and clip it in the lines shape...

            //Lines
            for (i in 0..exerciseTimerStyle.totalLines) {
                val lineLength = exerciseTimerStyle.lineLength.toPx()
                val isElapsed = i >= linesToColorDifferently
                val lineColor =
                    if (isElapsed) exerciseTimerStyle.elapsedLineColor else exerciseTimerStyle.lineColor

                val angleInRadian = -DegreeUtils.toRadian((i * 6).toFloat())
                val outerRadius = exerciseTimerStyle.timerRadius.toPx()

                val lineStart = Offset(
                    x = (outerRadius - lineLength) * cos(angleInRadian) + circleCenter.x,
                    y = (outerRadius - lineLength) * sin(angleInRadian) + circleCenter.y
                )

                val lineEnd = Offset(
                    x = outerRadius * cos(angleInRadian) + circleCenter.x,
                    y = outerRadius * sin(angleInRadian) + circleCenter.y
                )

                //Drawing timer lines
                drawLine(
                    color = lineColor,
                    start = lineStart,
                    end = lineEnd,
                    strokeWidth = 5.dp.toPx()
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun ExerciseTimerPreview() {
    val totalTime = ExerciseTime(hours = 0, minutes = 3, seconds = 30)
    var currentTime by remember {
        mutableStateOf(
            String.format(
                "%02d:%02d:%02d",
                totalTime.hours,
                totalTime.minutes,
                totalTime.seconds
            )
        )
    }

    LaunchedEffect(Unit) {
        TimerUtil.startTimer(totalTime.toSeconds()) {
            currentTime = it
        }
    }

    ExerciseTimer(
        modifier = Modifier.fillMaxSize(),
        timeLeft = currentTime,
        totalTime = totalTime
    )
}