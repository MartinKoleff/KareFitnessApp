package com.koleff.kare_android.ui.compose.screen

import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.koleff.kare_android.common.DegreeUtils
import com.koleff.kare_android.common.TimerUtil
import com.koleff.kare_android.ui.state.ExerciseTimerStyle
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Timer
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun DoWorkoutScreen() {
//normal scaffold
}

@Preview
@Composable
fun DoWorkoutScreenPreview() {
    DoWorkoutScreen()
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ExerciseTimer(
    modifier: Modifier = Modifier,
    timeLeft: String,
    totalTime: Int, //In seconds
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
        timeLeftInSeconds.toFloat() / totalTime
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

            //Lines
            for (i in 0..exerciseTimerStyle.totalLines) {
                val lineLength = exerciseTimerStyle.lineLength.toPx()
                val isElapsed = i >= linesToColorDifferently
                val lineColor = if (isElapsed) exerciseTimerStyle.elapsedLineColor else exerciseTimerStyle.lineColor

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
    val totalTime = 30 //all parts are 30 in the timer
    val hours = totalTime / 24
    val minutes = totalTime / (24 * 60)
    val seconds = totalTime / (24 * 60 * 60)
    var currentTime by remember { mutableStateOf(String.format("%02d:%02d:%02d", hours, minutes, seconds)) }

    LaunchedEffect(Unit) {
        TimerUtil.startTimer(totalTime){
            currentTime = it
        }
    }

    ExerciseTimer(
        modifier = Modifier.fillMaxSize(),
        timeLeft = currentTime,
        totalTime = totalTime
    )
}