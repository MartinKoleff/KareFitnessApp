package com.koleff.kare_android.ui.compose.screen

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.koleff.kare_android.common.manager.data.StatisticsManager
import com.koleff.kare_android.data.model.dto.StatisticScreenType
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.ui.compose.components.ExerciseBannerV2
import com.koleff.kare_android.ui.compose.components.LoadingWheel
import com.koleff.kare_android.ui.compose.components.SearchBar
import com.koleff.kare_android.ui.compose.components.StatisticsGrid
import com.koleff.kare_android.ui.compose.components.StatisticsSegmentButton
import com.koleff.kare_android.ui.compose.components.WorkoutBannerV2
import com.koleff.kare_android.ui.compose.components.WorkoutCalendarCard
import com.koleff.kare_android.ui.compose.components.navigation_components.scaffolds.MainScreenScaffold
import com.koleff.kare_android.ui.state.BaseState
import com.koleff.kare_android.ui.view_model.ExerciseStatisticsViewModel
import com.koleff.kare_android.ui.view_model.GeneralStatisticsViewModel
import com.koleff.kare_android.ui.view_model.WorkoutStatisticsViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StatisticsScreen(
    generalStatisticsViewModel: GeneralStatisticsViewModel = hiltViewModel(),
    workoutStatisticsViewModel: WorkoutStatisticsViewModel = hiltViewModel(),
    exerciseStatisticsViewModel: ExerciseStatisticsViewModel = hiltViewModel(),
) {
    val generalStatisticsState by generalStatisticsViewModel.state.collectAsState()
    val workoutStatisticsState by workoutStatisticsViewModel.state.collectAsState()
    val exerciseStatisticsState by exerciseStatisticsViewModel.state.collectAsState()

    var selectedScreenState by remember { mutableStateOf(StatisticScreenType.GENERAL) }
    val onScreenChange: (Int) -> Unit = { selectedScreenId ->
        selectedScreenState = StatisticScreenType.fromId(selectedScreenId)
    }

    val selectedViewModel = when (selectedScreenState) {
        StatisticScreenType.GENERAL -> generalStatisticsViewModel
        StatisticScreenType.WORKOUT -> workoutStatisticsViewModel
        StatisticScreenType.EXERCISE -> exerciseStatisticsViewModel
    }

    var showLoadingDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<KareError?>(null) }
    LaunchedEffect(generalStatisticsState, workoutStatisticsState, exerciseStatisticsState) {
        val states = listOf(generalStatisticsState, workoutStatisticsState, exerciseStatisticsState)

        val errorState: BaseState = states.firstOrNull { it.isError } ?: BaseState()
        error = errorState.error
        showErrorDialog = errorState.isError
        Log.d("StatisticsScreen", "Error detected -> $showErrorDialog")

        val loadingState: BaseState = states.firstOrNull { it.isLoading } ?: BaseState()
        showLoadingDialog = loadingState.isLoading
        Log.d("StatisticsScreen", "Loading: $showLoadingDialog")
    }

    val selectedStatistics = when (selectedScreenState) {
        StatisticScreenType.GENERAL -> {
            StatisticsManager.extractGeneralStatistics(generalStatisticsState)
        }

        StatisticScreenType.WORKOUT -> {
            StatisticsManager.extractWorkoutStatistics(workoutStatisticsState)
        }

        StatisticScreenType.EXERCISE -> {
            StatisticsManager.extractExerciseStatistics(exerciseStatisticsState)
        }
    }

    var searchText by remember { mutableStateOf("") }
    val selectedWorkout by remember { mutableStateOf(WorkoutDto()) }
    val selectedExercise by remember { mutableStateOf(ExerciseDto()) }

    MainScreenScaffold(
        "Statistics",
        onNavigateToDashboard = { selectedViewModel.onNavigateToDashboard() },
        onNavigateToWorkouts = { selectedViewModel.onNavigateToWorkouts() },
        onNavigateBackAction = { selectedViewModel.onNavigateBack() },
        onNavigateToSettings = { selectedViewModel.onNavigateToSettings() },
    ) { innerPadding ->

        if (showLoadingDialog) {
            LoadingWheel(
                innerPadding = innerPadding
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                    //Segment buttons
                    StatisticsSegmentButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp),
                        selectedOptionIndex = 0, //Statistics screen
                        isDisabled = showLoadingDialog,
                        onScreenChange = {
                            onScreenChange(it)
                        }
                    )

                    if (selectedScreenState == StatisticScreenType.WORKOUT) {
                        SearchBar(
                            searchText = searchText,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            onSearch = { text ->
                                workoutStatisticsViewModel.onTextChange(text)
                            },
                            onSearchTextChange = {
                                searchText = it
                            },
                            onToggleSearch = {
                                workoutStatisticsViewModel.onToggleSearch()
                            })

                        if (selectedWorkout != WorkoutDto()) {
                            WorkoutBannerV2(workout = selectedWorkout, onClick = {})
                        }
                    } else if (selectedScreenState == StatisticScreenType.EXERCISE) {
                        SearchBar(
                            searchText = searchText,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            onSearch = { text ->
                                exerciseStatisticsViewModel.onTextChange(text)
                            },
                            onSearchTextChange = {
                                searchText = it
                            },
                            onToggleSearch = {
                                exerciseStatisticsViewModel.onToggleSearch()
                            })

                        if (selectedExercise != ExerciseDto()) {
                            ExerciseBannerV2(
                                exercise = selectedExercise,
                                showDifficulty = false,
                                onClick = {})
                        }
                    }

                    StatisticsGrid(
                        statistics = selectedStatistics
                    )

                    if (selectedScreenState == StatisticScreenType.WORKOUT) {
                        WorkoutCalendarCard(workoutDates = emptyList())
                    } else if (selectedScreenState == StatisticScreenType.EXERCISE) {
                        //Graph
                    }
                }
            }
        }
}
