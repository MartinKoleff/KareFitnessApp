package com.koleff.kare_android.ui.compose.components

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.koleff.kare_android.ui.state.CircularTimerStyle

enum class TimeType(val maxTime: Int) {
    Hours(13), //Start from 0 until n - 1
    Minutes(61),
    Seconds(61)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CircularTimePicker(
    timeType: TimeType,
    initialTime: Int,
    circularTimerStyle: CircularTimerStyle = CircularTimerStyle()
) {
    val expandedSize = timeType.maxTime * 10_000_000
    val initialListPoint = expandedSize / 2
    val targetIndex = initialListPoint + initialTime - 1 //Middle time is the selected one -> - 1

    val scrollState = rememberLazyListState(initialFirstVisibleItemIndex = targetIndex)
    val flingBehavior = rememberSnapFlingBehavior(scrollState)

    val textColor = MaterialTheme.colorScheme.onSurface
    val outlineColor = MaterialTheme.colorScheme.outlineVariant

    val textStyle = MaterialTheme.typography.titleLarge.copy(
        color = textColor
    )

    LaunchedEffect(scrollState.isScrollInProgress) {
        if (!scrollState.isScrollInProgress) {
            val currentTime =
                if (scrollState.firstVisibleItemIndex / 10_000_000 == timeType.maxTime) timeType.maxTime //Max time reached...
                else ((scrollState.firstVisibleItemIndex + 1) % timeType.maxTime) //Middle time is the selected one -> + 1

            Log.e("Time", "$timeType: $currentTime")
        }
    }

    Box(
        modifier = Modifier
            .height(circularTimerStyle.size)
            .wrapContentWidth()
    ) {
        LazyColumn(
            modifier = Modifier.wrapContentWidth(),
            state = scrollState,
            flingBehavior = flingBehavior
        ) {
            items(expandedSize) { index ->
                val num = (index % timeType.maxTime)
                Box(
                    modifier = Modifier.size(circularTimerStyle.cellSize),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = String.format("%02d", num),
                        style = textStyle,
                        fontSize = LocalDensity.current.run { circularTimerStyle.cellTextSize.toSp() }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@PreviewLightDark
@Composable
fun CircularTimePickerPreview() {
    val size = CircularTimerStyle().size
    val backgroundColor = MaterialTheme.colorScheme.scrim

    Row(
        modifier = Modifier
            .size(size)
            .background(backgroundColor)
    ) {
        CircularTimePicker(TimeType.Hours, initialTime = 5)
        CircularTimePicker(TimeType.Minutes, initialTime = 15)
        CircularTimePicker(TimeType.Seconds, initialTime = 30)
    }
}

@Composable
fun CircularTimerFooter(
    modifier: Modifier = Modifier,
    circularTimerStyle: CircularTimerStyle = CircularTimerStyle()
) {
    val textColor = MaterialTheme.colorScheme.onSurface

    val textStyle = MaterialTheme.typography.titleLarge.copy(
        color = textColor
    )

    val cornerSize = 20.dp
    Row(
        modifier = modifier
            .height(circularTimerStyle.size),
        verticalAlignment = Alignment.CenterVertically
    ) {

        //Text
        Text(
            modifier = Modifier
                .padding(4.dp),
            text = "Time:",
            style = textStyle,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        //Timer
        Row(
            modifier = Modifier
                .width(300.dp)
                .height(circularTimerStyle.size)
                .padding(horizontal = 16.dp)
                .drawBehind {

                    //Selector
                    drawRoundRect(
                        color = Color.Gray,
                        topLeft = Offset(x = 0.dp.toPx(), y = circularTimerStyle.cellSize.toPx()),
                        size = Size(
                            width = this.size.width,
                            height = circularTimerStyle.cellSize.toPx()
                        ),
                        cornerRadius = CornerRadius(x = cornerSize.toPx(), y = cornerSize.toPx()),
                        colorFilter = ColorFilter.tint(Color.Gray, BlendMode.Screen)
                    )
                }
        ) {


            Box(modifier = Modifier.weight(1f)) {
                CircularTimePicker(TimeType.Minutes, initialTime = 0)

                //Text
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        modifier = Modifier
                            .padding(4.dp),
                        text = "min.",
                        style = textStyle,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            Box(modifier = Modifier.weight(1f)) {
                CircularTimePicker(TimeType.Seconds, initialTime = 0)

                //Text
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        modifier = Modifier
                            .padding(4.dp),
                        text = "sec.",
                        style = textStyle,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@PreviewLightDark
@Composable
fun CircularTimerFooterPreview() {
    val backgroundColor = MaterialTheme.colorScheme.scrim

    CircularTimerFooter(modifier = Modifier.background(backgroundColor))
}

@Composable
fun RestBetweenSetsFooter(
    modifier: Modifier = Modifier,
    onCheckedChange: (Boolean) -> Unit,
    isChecked: Boolean = false
) {
    val textColor = MaterialTheme.colorScheme.onSurface

    val textStyle = MaterialTheme.typography.titleLarge.copy(
        color = textColor
    )

    Column(
        modifier = modifier.padding(horizontal = 16.dp, vertical = 4.dp)
    ) {

        //Toggle switch header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            //Text
            Text(
                modifier = Modifier
                    .padding(4.dp),
                text = "Rest between sets:",
                style = textStyle,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            //Switch
            SwitchButton(isChecked = isChecked, onCheckedChange = onCheckedChange)
        }

        if (isChecked) {
            CircularTimerFooter()
        }
    }
}

@Preview
@PreviewLightDark
@Composable
fun RestBetweenSetsFooterPreview() {
    val backgroundColor = MaterialTheme.colorScheme.scrim

    RestBetweenSetsFooter(
        modifier = Modifier.background(backgroundColor),
        onCheckedChange = {},
        isChecked = true
    )
}

@Preview
@PreviewLightDark
@Composable
fun RestBetweenSetsFooterPreview2() {
    val backgroundColor = MaterialTheme.colorScheme.scrim

    RestBetweenSetsFooter(
        modifier = Modifier.background(backgroundColor),
        onCheckedChange = {},
        isChecked = false
    )
}