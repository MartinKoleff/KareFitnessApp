package com.koleff.kare_android.ui.compose.components

import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.koleff.kare_android.common.timer.TimerUtil
import com.koleff.kare_android.data.model.dto.ExerciseTime
import com.koleff.kare_android.ui.state.ExerciseTimerStyle
import kotlin.math.cos
import kotlin.math.sin

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ExerciseTimer(
    modifier: Modifier = Modifier,
    timeLeft: ExerciseTime,
    totalTime: ExerciseTime,
    exerciseTimerStyle: ExerciseTimerStyle = ExerciseTimerStyle(
        lineColor = MaterialTheme.colorScheme.outline
    ),
    isLogging: Boolean = false
) {
    val timePercentageLeftState = remember {
        mutableFloatStateOf(100.0f)
    }

    val markedLinesState = remember {
        mutableIntStateOf(exerciseTimerStyle.totalLines)
    }

    val configuration = LocalConfiguration.current
    val textColor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        if (configuration.isNightModeActive) {
            Color.WHITE
        } else {
            Color.BLACK
        }
    } else {

        //No dark mode supported -> default color
        Color.BLACK
    }


    // Update state variables within LaunchedEffect when timeLeft changes
    LaunchedEffect(timeLeft) {
        timePercentageLeftState.floatValue =
            TimerUtil.calculateTimeLeftPercentage(totalTime, timeLeft)
        markedLinesState.intValue = TimerUtil.calculateMarkedLines(
            timePercentageLeftState.floatValue,
            exerciseTimerStyle.totalLines
        )

        if (isLogging) {
            Log.d(
                "ExerciseTimer",
                "Percentage of Time Left: ${timePercentageLeftState.floatValue}%"
            )
            Log.d("ExerciseTimer", "Marked Lines: ${markedLinesState.intValue}")
        }
    }

    Canvas(modifier = modifier) {
        drawContext.canvas.nativeCanvas.apply {
            val circleCenter = Offset(
                x = center.x,
                y = center.y + (exerciseTimerStyle.timerRadius.toPx() / 2)
            ) //height.toFloat()

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
                timeLeft.toString(),
                circleCenter.x,
                circleCenter.y - 10.dp.toPx(),
                Paint().apply {
                    color = textColor
                    textSize = 20.sp.toPx()
                    textAlign = Paint.Align.CENTER
                }
            )

            //TODO: add a layer that gradually fills the circle with the percentage taken of the time and clip it in the lines shape...

            //Lines
            for (i in 0..exerciseTimerStyle.totalLines) {
                val lineLength = exerciseTimerStyle.lineLength.toPx()
                val isElapsed = i >= markedLinesState.intValue
                val lineColor =
                    if (isElapsed) exerciseTimerStyle.elapsedLineColor else exerciseTimerStyle.lineColor

                val angleInRadian = -Math.toRadians(i * 6.0).toFloat()
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
        mutableStateOf(ExerciseTime(totalTime.hours, totalTime.minutes, totalTime.seconds))
    }
    val exerciseTimerStyle = ExerciseTimerStyle(
        lineColor = MaterialTheme.colorScheme.outline
    )
    val workoutTimer = TimerUtil()
    LaunchedEffect(Unit) {
        workoutTimer.startTimer(totalTime.toSeconds()) {
            currentTime = it
        }
    }

    ExerciseTimer(
        modifier = Modifier
            .fillMaxSize()
//            .size(exerciseTimerStyle.timerRadius * 2)
            .background(MaterialTheme.colorScheme.scrim),
        timeLeft = currentTime,
        totalTime = totalTime
    )
}