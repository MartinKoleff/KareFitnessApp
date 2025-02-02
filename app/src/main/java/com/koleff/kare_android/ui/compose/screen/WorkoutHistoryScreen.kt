package com.koleff.kare_android.ui.compose.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.koleff.kare_android.R
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.window.Dialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.hilt.navigation.compose.hiltViewModel
import com.koleff.kare_android.ui.compose.banners.WorkoutBanner
import com.koleff.kare_android.ui.compose.components.LoadingWheel
import com.koleff.kare_android.ui.compose.components.navigation_components.scaffolds.MainScreenScaffold
import com.koleff.kare_android.ui.compose.dialogs.DatePickerModal
import com.koleff.kare_android.ui.compose.dialogs.DatePickerTarget
import com.koleff.kare_android.ui.compose.dialogs.DateRangeDialog
import com.koleff.kare_android.ui.view_model.WorkoutHistoryViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.ZoneId



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WorkoutHistoryScreen(
    workoutHistoryViewModel: WorkoutHistoryViewModel = hiltViewModel(),
) {
    MainScreenScaffold(
        "Workout History",
        onNavigateToDashboard = { workoutHistoryViewModel.onNavigateToDashboard() },
        onNavigateToWorkouts = { workoutHistoryViewModel.onNavigateToWorkouts() },
        onNavigateBackAction = { workoutHistoryViewModel.onNavigateBack() },
        onNavigateToSettings = { workoutHistoryViewModel.onNavigateToSettings() }
    ) { innerPadding ->
        val workoutHistoryState by workoutHistoryViewModel.getPerformanceMetricListState.collectAsState()

        if (workoutHistoryState.isLoading && workoutHistoryState.doWorkoutPerformanceMetricsList.isEmpty()) {
            LoadingWheel(innerPadding = innerPadding)
        } else {
            val modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)

            val titleTextColor = MaterialTheme.colorScheme.onSurface
            val titleTextStyle = MaterialTheme.typography.titleMedium.copy(
                color = titleTextColor
            )
            val titlePadding = PaddingValues(
                top = 8.dp,
                bottom = 0.dp,
                start = 8.dp,
                end = 8.dp
            )

            val subtitleTextColor = MaterialTheme.colorScheme.onSurface
            val subtitleTextStyle = MaterialTheme.typography.titleMedium.copy(
                color = subtitleTextColor
            )

            //Dialogs
            var showDateDialog by remember {
                mutableStateOf(false)
            }

            var showDatePickerDialog by remember { mutableStateOf(false) }
            var selectedTarget by remember { mutableStateOf<DatePickerTarget?>(null) }

            var fromDate by remember { mutableStateOf<LocalDate?>(null) }
            var toDate by remember { mutableStateOf<LocalDate?>(null) }

            val dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy")

            //Filter dialog
            if (showDateDialog) {
                DateRangeDialog(
                    fromDate = fromDate,
                    toDate = toDate,
                    onFromDateClick = {
                        selectedTarget = DatePickerTarget.FROM
                        showDatePickerDialog = true
                    },
                    onToDateClick = {
                        selectedTarget = DatePickerTarget.TO
                        showDatePickerDialog = true
                    },
                    onCancelClick = {
                        showDateDialog = false
                    },
                    onApplyClick = {
                        showDateDialog = false

                        workoutHistoryViewModel.filterByDate(fromDate, toDate)
                    }
                )
            }

            //Calendar date picker
            if (showDatePickerDialog) {
                DatePickerModal(
                    onDismissRequest = {
                        showDatePickerDialog = false
                        selectedTarget = null
                    },
                    onDateSelected = { selectedDate ->
                        when (selectedTarget) {
                            DatePickerTarget.FROM -> fromDate = selectedDate
                            DatePickerTarget.TO -> toDate = selectedDate
                            null -> {}
                        }
                        showDatePickerDialog = false
                        selectedTarget = null
                    }
                )
            }

            LazyColumn(modifier = modifier) {

                //Header
                item {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(Modifier.weight(1f))

                        Text(
                            modifier = Modifier
                                .padding(6.dp),
                            text = "Filter",
                            style = subtitleTextStyle,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )

                        Image(
                            modifier = Modifier
                                .size(36.dp)
                                .padding(horizontal = 8.dp)
                                .clickable {
                                    showDateDialog = true
                                },
                            painter = painterResource(id = R.drawable.ic_filter),
                            contentDescription = null,
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                items(workoutHistoryState.doWorkoutPerformanceMetricsList.size) { index ->
                    val performanceMetrics = workoutHistoryState.doWorkoutPerformanceMetricsList[index]
                    val workoutDate =
                        performanceMetrics.date
                    val currentWorkout = performanceMetrics.workout
                    val localDate: LocalDate = workoutDate.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()

                    //Date
                    Text(
                        modifier = Modifier.padding(
                            titlePadding
                        ),
                        text = localDate.format(dateFormatter),
                        style = titleTextStyle,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    HorizontalDivider()

                    //Workout banner
                    WorkoutBanner(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .padding(vertical = 8.dp),
                        workout = currentWorkout,
                        hasDescription = false
                    ) { workout ->
                        //TODO: on workout click -> show doWorkoutExerciseSets with stats data... -> new screen...
                    }
                }
            }
        }
    }
}
