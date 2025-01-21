package com.koleff.kare_android.ui.compose.screen

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.Style
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.koleff.kare_android.ui.state.OnboardingSliderStyle
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

//
//@Preview
//@Composable
//fun SliderMinimalExample() {
//    var sliderPosition by remember { mutableFloatStateOf(0f) }
//    Column {
//        Slider(
//            value = sliderPosition,
//            onValueChange = { sliderPosition = it }
//        )
//        Text(text = sliderPosition.toString())
//    }
//}

@Preview
@Composable
fun SliderAdvancedExample(
    start: Float,
    end: Float,
    steps: Int,
    onValueChange: (Float) -> Unit
) {
    var sliderPosition by remember { mutableFloatStateOf(0f) }
    Column(modifier = Modifier.padding(horizontal = 16.dp)){
        Slider(
            value = sliderPosition,
            onValueChange = {
                sliderPosition = it.roundToInt().toFloat()
                onValueChange(it)
            },
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.secondary, //Green
                activeTrackColor = MaterialTheme.colorScheme.secondary,
                inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            steps = steps - 1,
            valueRange = start..end
        )
//        Text(text = sliderPosition.toString())
    }
}

//@Composable
//fun SliderLines(
//    onboardingSliderStyle: OnboardingSliderStyle = OnboardingSliderStyle(
//        lineColor = MaterialTheme.colorScheme.outline
//    )
//) {
//    Surface(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        color = androidx.compose.ui.graphics.Color.White
//    ) {
//        Canvas(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(100.dp)
//        ) {
//            for (i in onboardingSliderStyle.startBound..onboardingSliderStyle.endBound) {
//                val lineLength = if (i % 10 == 0) onboardingSliderStyle.bigLineLength.toPx()
//                else onboardingSliderStyle.smallLineLength.toPx()
//                val lineColor = onboardingSliderStyle.lineColor
//
//                val totalLines = onboardingSliderStyle.totalLines
//                val screenWidth = size.width
//                val lineSpacing = screenWidth / (totalLines - 1)
//                val x = i * lineSpacing
//
//                drawLine(
//                    color = lineColor,
//                    start = Offset(x, 0f),
//                    end = Offset(x, lineLength),
//                    strokeWidth = 5.dp.toPx()
//                )
//            }
//        }
//    }
//}
//
//@Preview
//@Composable
//private fun SliderLinesPreview() {
//    SliderLines()
//}
//
//
//@Composable
//fun HorizontalRuler(start: Int, end: Int) {
//    Surface(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        color = androidx.compose.ui.graphics.Color.White
//    ) {
//        Canvas(modifier = Modifier.fillMaxSize()) {
//            val totalLines = end - start + 1
//            val screenWidth = size.width
//            val spacing = screenWidth / (totalLines - 1)
//
//            for (i in 0 until totalLines) {
//                val x = i * spacing
//                val currentValue = start + i
//                val lineLength = if (currentValue % 10 == 0) 40f else 20f
//
//                drawLine(
//                    color = androidx.compose.ui.graphics.Color.Black,
//                    start = Offset(x, 0f),
//                    end = Offset(x, lineLength),
//                    strokeWidth = 2f
//                )
//            }
//        }
//    }
//}
//
//@Preview
//@Composable
//private fun HorizontalRulerPreview() {
//    HorizontalRuler(start = 140, end = 210)
//}


@Composable
fun SliderLines(
    onboardingSliderStyle: OnboardingSliderStyle = OnboardingSliderStyle(
        lineColor = MaterialTheme.colorScheme.outline
    )
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(onboardingSliderStyle.bigLineLength + 30.dp)
            .padding(horizontal = 16.dp)
    ) {
        Canvas(
            modifier = Modifier.fillMaxWidth()
        ) {
            val totalLines = onboardingSliderStyle.totalLines / onboardingSliderStyle.interval - 1
            val screenWidth = this.size.width
            val padding = 12.dp.toPx()
            val spacing = (screenWidth - 2 * padding) / (totalLines + 1)

            for ((counter, _) in (onboardingSliderStyle.startBound..onboardingSliderStyle.endBound).withIndex()) {
                val x = padding + counter * spacing
                val currentNumber = onboardingSliderStyle.startBound + (counter * onboardingSliderStyle.interval)

                val lineLength = if (currentNumber % 10 == 0) onboardingSliderStyle.bigLineLength.toPx()
                else onboardingSliderStyle.smallLineLength.toPx()
                val lineColor = onboardingSliderStyle.lineColor

                drawLine(
                    color = lineColor,
                    start = Offset(x, 0f),
                    end = Offset(x, lineLength),
                    strokeWidth = 3.dp.toPx()
                )

                if (currentNumber % 10 == 0) {
                    drawContext.canvas.nativeCanvas.drawText(
                        currentNumber.toString(),
                        x,
                        lineLength + 30f, // Adjust label position
                        Paint().apply {
                            color = Color.BLACK
                            textSize = 28f
                            textAlign = android.graphics.Paint.Align.CENTER
                        }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun SliderWithLinesPreview() {
    var sliderValue by remember { mutableFloatStateOf(150f) }
    val onboardingSliderStyle: OnboardingSliderStyle = OnboardingSliderStyle(
        lineColor = MaterialTheme.colorScheme.outline
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Height")
            Text(sliderValue.toInt().toString())
        }

        SliderAdvancedExample(
            start = onboardingSliderStyle.startBound.toFloat(),
            end = onboardingSliderStyle.endBound.toFloat(),
            steps = onboardingSliderStyle.totalLines / onboardingSliderStyle.interval
        ) {
            sliderValue = it
        }
        SliderLines()
    }
}
//
//@Composable
//fun HorizontalRulerWithHandle(start: Int, end: Int) {
//    var currentValue by remember { mutableStateOf((start + end) / 2) }
//
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        // Label showing the current value
//        Text(
//            text = "$currentValue",
//            fontSize = 20.sp,
//            color = androidx.compose.ui.graphics.Color.Black,
//            modifier = Modifier.padding(bottom = 16.dp)
//        )
//
//        Box(modifier = Modifier.fillMaxWidth()) {
//            Canvas(modifier = Modifier
//                .fillMaxWidth()
//                .height(60.dp)
//                .pointerInput(Unit) {
//                    detectDragGestures { change, _ ->
//                        val screenWidth = size.width
//                        val spacing = screenWidth / (end - start)
//                        val newValue =
//                            ((change.position.x / spacing).roundToInt() + start).coerceIn(
//                                start,
//                                end
//                            )
//                        currentValue = newValue
//                    }
//                }) {
//                val totalLines = end - start + 1
//                val screenWidth = size.width
//                val spacing = screenWidth / (totalLines - 1)
//
//                // Draw the ruler lines
//                for (i in 0 until totalLines) {
//                    val x = i * spacing
//                    val value = start + i
//                    val lineLength = if (value % 10 == 0) 40f else 20f
//
//                    drawLine(
//                        color = androidx.compose.ui.graphics.Color.Gray,
//                    start = Offset(x, 0f),
//                    end = Offset(x, lineLength),
//                    strokeWidth = 2f
//                )
//
//                // Draw labels for multiples of 10
//                if (value % 10 == 0) {
//                    drawContext.canvas.nativeCanvas.drawText(
//                        value.toString(),
//                        x,
//                        lineLength + 30f,
//                        Paint().apply {
//                            color = Color.BLACK
//                            textSize = 28f
//                            textAlign = android.graphics.Paint.Align.CENTER
//                        }
//                    )
//                }
//            }
//
//            // Draw the green progress line
//            val progressX = (currentValue - start) * spacing
//            drawLine(
//                brush = Brush.linearGradient(
//                    colors = listOf(
//                        androidx.compose.ui.graphics.Color.Green,
//                        androidx.compose.ui.graphics.Color.Cyan
//                    )
//                ),
//                start = Offset(0f, 0f),
//                end = Offset(progressX, 0f),
//                strokeWidth = 6f
//            )
//
//            // Draw the handle
//            drawCircle(
//                color = androidx.compose.ui.graphics.Color.Green,
//                center = Offset(progressX, 0f),
//                radius = 15.dp.toPx()
//            )
//            drawCircle(
//                color = androidx.compose.ui.graphics.Color.White,
//                center = Offset(progressX, 0f),
//                radius = 10.dp.toPx()
//            )
//        }
//    }
//}
//}
//
//
//@Preview
//@Composable
//private fun HorizontalRuler2Preview() {
//HorizontalRulerWithHandle(140, 210)
//}
