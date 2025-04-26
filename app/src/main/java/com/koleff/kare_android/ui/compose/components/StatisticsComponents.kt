package com.koleff.kare_android.ui.compose.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.koleff.kare_android.R
import com.koleff.kare_android.ui.style.StatisticsDataUI
import com.koleff.kare_android.ui.theme.LocalExtendedColors
import java.text.DateFormatSymbols
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.ceil


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsSegmentButton(
    modifier: Modifier = Modifier,
    selectedOptionIndex: Int = -1,
    isDisabled: Boolean,
    onScreenChange: (Int) -> Unit
) {
    val labelColor = MaterialTheme.colorScheme.onSurface
    val buttonColor = MaterialTheme.colorScheme.primaryContainer
    val outlineColor = MaterialTheme.colorScheme.outlineVariant

    val labelTextStyle = MaterialTheme.typography.bodySmall.copy(
        color = labelColor
    )

    val cornerSize = 6.dp

    var selectedIndex by remember { mutableStateOf(selectedOptionIndex) }
    val options = listOf("General", "Exercise", "Workout")
    SingleChoiceSegmentedButtonRow(modifier) {
        options.forEachIndexed { index, label ->
            SegmentedButton(
                colors = SegmentedButtonDefaults.colors(
                    activeContainerColor = buttonColor,
                    disabledActiveContainerColor = buttonColor,
                    activeBorderColor = outlineColor,
                    disabledInactiveBorderColor = outlineColor,
                    inactiveBorderColor = outlineColor,
                    activeContentColor = labelColor,
                    inactiveContentColor = MaterialTheme.colorScheme.onSurface,
                    inactiveContainerColor = MaterialTheme.colorScheme.surfaceContainer
                ),
                icon = {}, //No checkmark
                shape = when (index) {
                    0 -> RoundedCornerShape(
                        topStart = cornerSize,
                        topEnd = 0.dp,
                        bottomStart = cornerSize,
                        bottomEnd = 0.dp
                    )

                    2 -> RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = cornerSize,
                        bottomStart = 0.dp,
                        bottomEnd = cornerSize
                    )

                    else -> RoundedCornerShape(0.dp)
                },
                onClick = {
                    selectedIndex = index
                    onScreenChange(selectedIndex)
                },
                selected = index == selectedIndex,
                enabled = !isDisabled
            ) {
                Text(
                    text = label,
                    style = labelTextStyle,
                )
            }
        }
    }
}

@Preview
@Composable
private fun StatisticsSegmentButtonPreview() {
    StatisticsSegmentButton(
        modifier = Modifier,
        selectedOptionIndex = 1, //Exercise
        isDisabled = false,
        onScreenChange = {}
    )
}

//Half width card
@Composable
fun InfoCardSmall(imageId: Int, title: String, description: String) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val cornerSize = 32.dp
    val iconSize = 40.dp

    val titleTextColor = LocalExtendedColors.current.title
    val titleTextStyle = MaterialTheme.typography.titleSmall.copy(
        color = titleTextColor,
        fontWeight = FontWeight.Bold
    )

    val subtitleTextColor = LocalExtendedColors.current.title
    val subtitleTextStyle = MaterialTheme.typography.bodyMedium.copy(
        color = subtitleTextColor
    )

    val tintColor = if (isSystemInDarkTheme()) Color.White else Color.Black

    Card(
        modifier = Modifier
            .width(screenWidth / 2)
            .height(160.dp)
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(cornerSize)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(id = imageId),
                contentDescription = "",
                tint = tintColor,
                modifier = Modifier
                    .size(iconSize)
            )
            Text(
                text = title,
                style = titleTextStyle,
                textAlign = TextAlign.Center,
                maxLines = 2
            )

            Box(
                modifier = Modifier
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    description,
                    style = subtitleTextStyle,
                    textAlign = TextAlign.Center,
                    maxLines = 2
                )
            }
        }
    }
}

@Preview
@Composable
private fun InfoCardSmallPreview() {
    InfoCardSmall(
        imageId = R.drawable.weight,
        title = "1 Rep Max\n test test",
        description = "100kg\n test test"
    )
}

@Preview
@Composable
private fun InfoCardSmallPreview2() {
    InfoCardSmall(
        imageId = R.drawable.weight,
        title = "1 Rep Max",
        description = "100kg"
    )
}

//Calendar with dates of which you have trained -> big box
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WorkoutCalendarCard(workoutDates: List<LocalDate>) {
    var selectedMonth by remember { mutableStateOf(LocalDate.now().withDayOfMonth(1)) }
    var showMonthPicker by remember { mutableStateOf(false) }

    val cornerSize = 12.dp

    val subtitleTextColor = LocalExtendedColors.current.subtitle
    val subtitleTextStyle = MaterialTheme.typography.titleSmall.copy(
        color = subtitleTextColor,
        fontWeight = FontWeight.Bold
    )

    val headlinerTextColor = LocalExtendedColors.current.title
    val headlinerTextStyle = MaterialTheme.typography.headlineSmall.copy(
        color = headlinerTextColor,
        fontWeight = FontWeight.Bold
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(LocalConfiguration.current.screenHeightDp.dp * 2 / 5)
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(cornerSize)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            //Month Selector
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { selectedMonth = selectedMonth.minusMonths(1) }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Previous Month")
                }

                Text(
                    modifier = Modifier.clickable { showMonthPicker = true },
                    style = headlinerTextStyle,
                    text = selectedMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy"))
                )

                IconButton(onClick = { selectedMonth = selectedMonth.plusMonths(1) }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next Month")
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            //Weekday Headers
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                listOf("S", "M", "T", "W", "T", "F", "S").forEach { day ->
                    Text(
                        text = day,
                        modifier = Modifier.weight(1f),
                        style = subtitleTextStyle,
                        textAlign = TextAlign.Center
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))

            //Generate Calendar Grid
            CalendarGrid(selectedMonth, workoutDates)

            //Month Picker Dialog
            if (showMonthPicker) {
                MonthYearPickerDialog(
                    selectedMonth = selectedMonth,
                    onDismiss = { showMonthPicker = false },
                    onMonthSelected = { newMonth -> selectedMonth = newMonth }
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MonthYearPickerDialog(
    selectedMonth: LocalDate,
    onDismiss: () -> Unit,
    onMonthSelected: (LocalDate) -> Unit
) {
    val months = DateFormatSymbols().months
    var selectedYear by remember { mutableStateOf(selectedMonth.year) }
    var selectedMonthIndex by remember { mutableStateOf(selectedMonth.monthValue - 1) }

    val titleTextColor = LocalExtendedColors.current.title
    val titleTextStyle = MaterialTheme.typography.titleSmall.copy(
        color = titleTextColor,
        fontWeight = FontWeight.Bold
    )

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = "Select Month & Year", style = titleTextStyle) },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                //Month Dropdown
                DropdownMenuBox(
                    items = months.toList(),
                    selectedIndex = selectedMonthIndex,
                    onSelected = { index -> selectedMonthIndex = index }
                )
                Spacer(modifier = Modifier.height(8.dp))

                //Year Selector (Simple Row)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { selectedYear-- }) {
                        Icon(
                            Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "Previous Year"
                        )
                    }

                    Text(text = "$selectedYear", style = titleTextStyle)

                    IconButton(onClick = { selectedYear++ }) {
                        Icon(
                            Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = "Next Year"
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onMonthSelected(LocalDate.of(selectedYear, selectedMonthIndex + 1, 1))
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun DropdownMenuBox(items: List<String>, selectedIndex: Int, onSelected: (Int) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    val textColor = LocalExtendedColors.current.label
    val textStyle = MaterialTheme.typography.bodyMedium.copy(
        color = textColor
    )

    Box(
        modifier = Modifier
            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
            .clickable { expanded = true }
            .padding(8.dp)
    ) {
        Text(text = items[selectedIndex], style = textStyle, textAlign = TextAlign.Center)

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            items.forEachIndexed { index, item ->
                DropdownMenuItem(
                    text = { Text(text = item, style = textStyle) },
                    onClick = {
                        onSelected(index)
                        expanded = false
                    })
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarGrid(selectedMonth: LocalDate, workoutDates: List<LocalDate>) {
    val startOfMonth = selectedMonth.withDayOfMonth(1)
    val daysInMonth = selectedMonth.lengthOfMonth()
    val firstDayOfWeek = startOfMonth.dayOfWeek.value % 7 // Adjust for Sunday start

    val titleTextStyle = MaterialTheme.typography.titleSmall.copy(
        fontWeight = FontWeight.Bold
    )

    LazyVerticalGrid(columns = GridCells.Fixed(7), modifier = Modifier.fillMaxWidth()) {
        // Empty spaces before first day
        items(firstDayOfWeek) { Box(modifier = Modifier.size(40.dp)) }

        // Days of the month
        items(daysInMonth) { day ->
            val date = startOfMonth.plusDays(day.toLong())
            val isWorkoutDay = workoutDates.contains(date)

            Box(
                modifier = Modifier
                    .size(40.dp),
                contentAlignment = Alignment.Center
            ) {
                if (isWorkoutDay) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(Color.Red.copy(alpha = 0.5f)),
                    )
                }

                Text(
                    text = (day + 1).toString(),
                    style = titleTextStyle,
                    color = if (isWorkoutDay) Color.White else Color.Black
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun DaysTrainedCalendarPreview() {
    val workoutDays = listOf(
        LocalDate.now().minusDays(1),
        LocalDate.now().minusDays(5),
        LocalDate.now().minusDays(7),
        LocalDate.now().minusDays(10)
    )

    WorkoutCalendarCard(workoutDays)
}

//TODO: Graph for weight lifted over time per exercise...

@Composable
fun StatisticsGrid(
    statistics: List<StatisticsDataUI>,
    modifier: Modifier = Modifier
) {
    val cardHeight = 160.dp + 8.dp
    val calculatedHeight = ceil(statistics.size / 2f) * cardHeight
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(calculatedHeight)) {
        LazyVerticalStaggeredGrid(
            modifier = modifier,
            columns = StaggeredGridCells.Fixed(2),
            verticalItemSpacing = 2.dp,
            horizontalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            items(statistics.size) { currentStatisticsId ->
                val currentMuscleGroup = statistics[currentStatisticsId]

                InfoCardSmall(
                    imageId = currentMuscleGroup.imageId,
                    title = currentMuscleGroup.title,
                    description = currentMuscleGroup.desc
                )
            }
        }
    }
}

@Preview
@Composable
private fun StatisticsGridPreview() {
    StatisticsGrid(
        statistics = listOf(
            StatisticsDataUI(
                imageId = R.drawable.weight,
                title = "1 Rep Max",
                desc = "Description"
            ),
            StatisticsDataUI(
                imageId = R.drawable.timer,
                title = "Longest workout streak",
                desc = "Description"
            )
        )
    )
}