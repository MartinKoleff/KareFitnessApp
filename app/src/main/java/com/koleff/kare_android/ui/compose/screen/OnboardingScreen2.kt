package com.koleff.kare_android.ui.compose.screen

import android.graphics.Color
import android.graphics.Paint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.koleff.kare_android.ui.state.OnboardingSliderStyle
import kotlin.math.roundToInt
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import com.koleff.kare_android.ui.compose.components.navigation_components.NavigationItem
import com.koleff.kare_android.ui.view_model.OnboardingViewModel

@Preview
@Composable
fun SliderAdvancedExample(
    start: Float,
    end: Float,
    steps: Int = 1,
    initialValue: Float,
    onValueChange: (Float) -> Unit
) {
    var sliderPosition by remember { mutableFloatStateOf(initialValue) }
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Slider(
            value = sliderPosition,
            onValueChange = {
                sliderPosition = it.roundToInt().toFloat()
                onValueChange(it)
            },
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.primary,
                activeTrackColor = androidx.compose.ui.graphics.Color.Green,
                inactiveTrackColor = androidx.compose.ui.graphics.Color.Cyan //Light Green...
            ),
            steps = steps - 1,
            valueRange = start..end
        )
//        Text(text = sliderPosition.toString())
    }
}

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
                val currentNumber =
                    onboardingSliderStyle.startBound + (counter * onboardingSliderStyle.interval)

                val lineLength =
                    if (currentNumber % 10 == 0) onboardingSliderStyle.bigLineLength.toPx()
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

@Composable
fun SliderWithLines(
    modifier: Modifier = Modifier,
    onboardingSliderStyle: OnboardingSliderStyle = OnboardingSliderStyle(
        lineColor = MaterialTheme.colorScheme.outline
    ),
    sliderTitle: String,
    sliderMetrics: String,
    hasSteps: Boolean = false
) {
    var sliderValue by remember { mutableFloatStateOf(onboardingSliderStyle.initialValue.toFloat()) }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(sliderTitle)
            Text(sliderValue.toInt().toString() + " " + sliderMetrics)
        }

        SliderAdvancedExample(
            start = onboardingSliderStyle.startBound.toFloat(),
            end = onboardingSliderStyle.endBound.toFloat(),
            steps = if (hasSteps) onboardingSliderStyle.totalLines / onboardingSliderStyle.interval else 1,
            initialValue = onboardingSliderStyle.initialValue.toFloat()
        ) {
            sliderValue = it
        }
        SliderLines(onboardingSliderStyle)
    }
}

@Composable
fun OnboardingScreen(onboardingViewModel: OnboardingViewModel = hiltViewModel()) {
    Column(modifier = Modifier.fillMaxSize()) {

        //Screen
        Column(modifier = Modifier.weight(11f)) {
            OnboardingToolbar(
                "User metrics",
                "Please fill in the following information.",
            ) {
                onboardingViewModel.onNavigateBack()
            }
            Spacer(modifier = Modifier.size(16.dp))

            GenderSelectionGroup()
            Spacer(modifier = Modifier.size(32.dp))

            SliderWithLines(
                sliderTitle = "Height",
                sliderMetrics = "CM",
                onboardingSliderStyle = OnboardingSliderStyle(
                    lineColor = MaterialTheme.colorScheme.outline,
                    initialValue = 170,
                    startBound = 140,
                    endBound = 210,
                    interval = 5
                ),
                hasSteps = true
            )

            SliderWithLines(
                sliderTitle = "Age",
                sliderMetrics = "Years",
                onboardingSliderStyle = OnboardingSliderStyle(
                    lineColor = MaterialTheme.colorScheme.outline,
                    initialValue = 22,
                    startBound = 10,
                    endBound = 100,
                    interval = 5
                ),
                hasSteps = false
            )

            SliderWithLines(
                sliderTitle = "Weight",
                sliderMetrics = "KG",
                onboardingSliderStyle = OnboardingSliderStyle(
                    lineColor = MaterialTheme.colorScheme.outline,
                    initialValue = 79,
                    startBound = 35,
                    endBound = 200,
                    interval = 15
                ),
                hasSteps = false
            )
        }

        //Footer
        OnboardingButton(modifier = Modifier.weight(1f), "Proceed") {

        }
    }
}

@Preview
@Composable
private fun OnboardingScreenPreview() {
    OnboardingScreen()
}


@Composable
fun OnboardingToolbar(
    title: String,
    subtitle: String,
    onBack: () -> Unit
) {
    val titleTextColor = MaterialTheme.colorScheme.onSurface

    val titleTextStyle = MaterialTheme.typography.displayMedium.copy(
        color = titleTextColor
    )

    val subtitleTextStyle = MaterialTheme.typography.titleSmall.copy(
        color = titleTextColor
    )

    val titlePadding =
        PaddingValues(
            top = 8.dp,
            bottom = 0.dp,
            start = 8.dp,
            end = 8.dp
        )

    val subtitlePadding =
        PaddingValues(
            top = 2.dp,
            bottom = 8.dp,
            start = 8.dp,
            end = 8.dp
        )


    Box(
        Modifier
            .fillMaxWidth()
            .height(100.dp),
        contentAlignment = Alignment.Center
    ) {
        val tintColor = MaterialTheme.colorScheme.onSurface
        NavigationItem(
            modifier = Modifier
                .fillMaxHeight()
                .align(Alignment.CenterStart),
            icon = Icons.AutoMirrored.Filled.ArrowBackIos,
            label = "Go back",
            tint = tintColor,
            onNavigateAction = onBack
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            //Title
            Text(
                modifier = Modifier.padding(
                    titlePadding
                ),
                text = title,
                style = titleTextStyle,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            //Subtitle
            Text(
                modifier = Modifier.padding(
                    subtitlePadding
                ),
                text = subtitle,
                style = subtitleTextStyle,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}


@Preview
@Composable
private fun OnboardingToolbarPreview() {
    OnboardingToolbar(
        "User metrics",
        "Please fill in the following information.",
    ) {

    }
}

@Composable
fun OnboardingButton(
    modifier: Modifier,
    text: String,
    onClick: () -> Unit
) {
    val cornerSize = 24.dp
    val paddingValues = PaddingValues(
        horizontal = 32.dp,
        vertical = 8.dp
    )
    val textColor = MaterialTheme.colorScheme.onSurface
    val outlineColor = MaterialTheme.colorScheme.outlineVariant
    val backgroundColor = MaterialTheme.colorScheme.primaryContainer

    val buttonTextStyle = MaterialTheme.typography.titleLarge.copy(
        color = textColor
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(paddingValues)
            .height(50.dp)
            .clip(RoundedCornerShape(cornerSize))
            .border(
                border = BorderStroke(2.dp, color = outlineColor),
                shape = RoundedCornerShape(cornerSize)
            )
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(cornerSize)
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {

        Text(
            modifier = Modifier.padding(
                PaddingValues(8.dp)
            ),
            text = text,
            style = buttonTextStyle,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}


@Composable
fun GenderSelectionBox(
    title: String,
    isSelected: Boolean,
    onClick: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(75.dp)
            .padding(vertical = 8.dp, horizontal = 8.dp)
            .background(
                color = if (isSelected) androidx.compose.ui.graphics.Color(0xFFE8F5E9) else androidx.compose.ui.graphics.Color(
                    0xFFF5F5F5
                ),
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onClick(title) }
            .padding(vertical = 12.dp, horizontal = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = title,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = androidx.compose.ui.graphics.Color.Black
                ),
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = if (isSelected) Icons.Default.CheckCircle else Icons.Outlined.RadioButtonUnchecked,
                contentDescription = if (isSelected) "Selected" else "Not Selected",
                tint = if (isSelected) androidx.compose.ui.graphics.Color(0xFF4CAF50) else androidx.compose.ui.graphics.Color.Gray
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GenderSelectionGroup() {
    var selectedGender by remember { mutableStateOf("Male") }
    val options = listOf("Male", "Female")

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        options.forEach { option ->
            GenderSelectionBox(
                title = option,
                isSelected = option == selectedGender,
                onClick = { selectedGender = it }
            )
        }
    }
}

//TODO: lazy grid for options like the sites for looking for jobs...

