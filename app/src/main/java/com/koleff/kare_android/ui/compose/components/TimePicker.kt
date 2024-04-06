package com.koleff.kare_android.ui.compose.components

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
                        style = MaterialTheme.typography.overline.copy(
                            color = Color.Gray,
                            fontSize = LocalDensity.current.run { circularTimerStyle.cellTextSize.toSp() }
                        )
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CircularTimePickerPreview() {
    Row(modifier = Modifier.fillMaxSize()) {
        CircularTimePicker(TimeType.Hours, initialTime = 5)
        CircularTimePicker(TimeType.Minutes, initialTime = 15)
        CircularTimePicker(TimeType.Seconds, initialTime = 30)
    }
}