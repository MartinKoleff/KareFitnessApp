package com.koleff.kare_android.ui.compose.screen

import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.data.model.dto.WorkoutConfigurationDto
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.ui.compose.banners.AddExerciseToWorkoutBanner
import com.koleff.kare_android.ui.compose.banners.SwipeableExerciseBanner
import com.koleff.kare_android.ui.compose.components.DeleteExercisesRow
import com.koleff.kare_android.ui.compose.components.LoadingWheel
import com.koleff.kare_android.ui.compose.components.SelectedExercisesRow
import com.koleff.kare_android.ui.compose.components.StartWorkoutHeader
import com.koleff.kare_android.ui.compose.components.StartWorkoutToolbar
import com.koleff.kare_android.ui.compose.components.SubmitExercisesRow
import com.koleff.kare_android.ui.compose.components.navigation_components.scaffolds.MainScreenScaffold
import com.koleff.kare_android.ui.compose.dialogs.DeleteExerciseDialog
import com.koleff.kare_android.ui.compose.dialogs.DeleteMultipleExercisesDialog
import com.koleff.kare_android.ui.compose.dialogs.EditWorkoutDialog
import com.koleff.kare_android.ui.compose.dialogs.ErrorDialog
import com.koleff.kare_android.ui.compose.dialogs.FavoriteWorkoutDialog
import com.koleff.kare_android.ui.compose.dialogs.WarningDialog
import com.koleff.kare_android.ui.compose.dialogs.WorkoutConfigurationDialog
import com.koleff.kare_android.ui.event.OnMultipleExercisesUpdateEvent
import com.koleff.kare_android.ui.state.AnimatedToolbarState
import com.koleff.kare_android.ui.state.BaseState
import com.koleff.kare_android.ui.view_model.WorkoutDetailsViewModel
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WorkoutDetailsScreen(
    workoutDetailsViewModel: WorkoutDetailsViewModel = hiltViewModel()
) {
    val workoutDetailsState by workoutDetailsViewModel.getWorkoutDetailsState.collectAsState()
    val updateWorkoutDetailsState by workoutDetailsViewModel.updateWorkoutDetailsState.collectAsState()
    val deleteWorkoutDetailsState by workoutDetailsViewModel.deleteWorkoutState.collectAsState()
    val deleteExerciseState by workoutDetailsViewModel.deleteExerciseState.collectAsState()
    val startWorkoutState by workoutDetailsViewModel.startWorkoutState.collectAsState()
    val createWorkoutState by workoutDetailsViewModel.createWorkoutState.collectAsState()
    val favoriteWorkoutState by workoutDetailsViewModel.favoriteWorkoutState.collectAsState()
    val unfavoriteWorkoutState by workoutDetailsViewModel.unfavoriteWorkoutState.collectAsState()

    val workoutTitle =
        if (workoutDetailsState.workoutDetails.name == "" || updateWorkoutDetailsState.isLoading) "Loading..."
        else workoutDetailsState.workoutDetails.name

    var selectedWorkout by remember {
        mutableStateOf(workoutDetailsState.workoutDetails)
    }
    var exercises by remember {
        mutableStateOf(selectedWorkout.exercises)
    }
    var selectedExercise by remember { mutableStateOf<ExerciseDto?>(null) }

    var showAddExerciseBanner by remember {
        mutableStateOf(workoutDetailsState.isSuccessful)
    }

    //Update workout on initial load
    LaunchedEffect(workoutDetailsState) {
        if (workoutDetailsState.isSuccessful) {
            Log.d("WorkoutDetailsScreen", "Initial load completed.")
            selectedWorkout = workoutDetailsState.workoutDetails
            exercises = selectedWorkout.exercises
            showAddExerciseBanner = true
        }
    }

    //When exercise is deleted -> update workout exercise list
    LaunchedEffect(deleteExerciseState) {
        if (deleteExerciseState.isSuccessful) {
            Log.d("WorkoutDetailsScreen", "Exercise successfully deleted.")
            selectedWorkout = deleteExerciseState.workoutDetails
            exercises = selectedWorkout.exercises
        }
    }

    //Dialog visibility
    var showDeleteExerciseDialog by remember { mutableStateOf(false) }
    var showDeleteMultipleExercisesDialog by remember { mutableStateOf(false) }
    var showEditWorkoutNameDialog by remember { mutableStateOf(false) }
    var showFavoriteDialog by remember { mutableStateOf(false) }
    var showUnfavoriteDialog by remember { mutableStateOf(false) }
    var showDeleteWorkoutDialog by remember { mutableStateOf(false) }
    var showWorkoutConfigureDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var showLoadingDialog by remember { mutableStateOf(false) }

    //Dialog callbacks
    val onDeleteWorkout: () -> Unit = {
        workoutDetailsViewModel.deleteWorkout()

        showDeleteWorkoutDialog = false
    }

    val onFavoriteWorkout: () -> Unit = {
        workoutDetailsViewModel.favoriteWorkout(selectedWorkout.workoutId)

        showFavoriteDialog = false
    }

    val onUnfavoriteWorkout: () -> Unit = {
        workoutDetailsViewModel.unfavoriteWorkout(selectedWorkout.workoutId)

        showUnfavoriteDialog = false
    }


    val onEditWorkoutName: (String) -> Unit = { newName ->
        val updatedWorkout = selectedWorkout.copy(name = newName)

        //Update workout
        workoutDetailsViewModel.updateWorkout(updatedWorkout)

        showEditWorkoutNameDialog = false
    }

    val onUpdateWorkoutConfiguration: (WorkoutConfigurationDto) -> Unit = { configuration ->
        workoutDetailsViewModel.updateWorkoutConfiguration(
            configuration
        )

        showWorkoutConfigureDialog = false
    }

    var isDeleteMode by remember {
        mutableStateOf(false)
    }
    val onDeleteModeEnabled = {
        isDeleteMode = !isDeleteMode
    }

    val selectedExercises = remember {
        mutableStateListOf<ExerciseDto>()
    }

    val onDeleteMultipleExercises: () -> Unit = {  //(List<ExerciseDto>) -> Unit
        workoutDetailsViewModel.onMultipleExercisesUpdateEvent(
            OnMultipleExercisesUpdateEvent.OnMultipleExercisesDelete(selectedExercises)
        )

        selectedExercises.clear()
        isDeleteMode = false
        showDeleteMultipleExercisesDialog = false
    }

    val onDeleteExercise = {
        selectedExercise?.let {
            workoutDetailsViewModel.deleteExercise(
                workoutDetailsState.workoutDetails.workoutId,
                selectedExercise!!.exerciseId
            )
        }

        showDeleteExerciseDialog = false
    }

    val onExerciseSelected: (ExerciseDto) -> Unit = { selectedExercise ->
        if (isDeleteMode) {
            val isNewExercise = !selectedExercises.map { it.exerciseId }
                .contains(selectedExercise.exerciseId)

            if (isNewExercise) {
                selectedExercises.add(
                    selectedExercise.copy(workoutId = workoutDetailsState.workoutDetails.workoutId)
                )
            } else {
                selectedExercises.removeAll { it.exerciseId == selectedExercise.exerciseId }

                isDeleteMode = selectedExercises.isNotEmpty()
            }
        } else {
            workoutDetailsViewModel.navigateToExerciseDetailsConfigurator(selectedExercise)
        }
    }

    //Error handling
    var error by remember { mutableStateOf<KareError?>(null) }
    val onErrorDialogDismiss = {
        showErrorDialog = false
        workoutDetailsViewModel.clearError() //Enters launched effect to update showErrorDialog...
    }

    LaunchedEffect(
        workoutDetailsState,
        deleteExerciseState,
        deleteWorkoutDetailsState,
        updateWorkoutDetailsState,
        startWorkoutState,
        createWorkoutState,
        favoriteWorkoutState,
        unfavoriteWorkoutState
    ) {
        val states = listOf(
            workoutDetailsState,
            deleteExerciseState,
            deleteWorkoutDetailsState,
            updateWorkoutDetailsState,
            startWorkoutState,
            createWorkoutState,
            favoriteWorkoutState,
            unfavoriteWorkoutState
        )

        val errorState: BaseState = states.firstOrNull { it.isError } ?: BaseState()
        error = errorState.error
        showErrorDialog = errorState.isError
        Log.d("WorkoutDetailsScreen", "Error detected -> $showErrorDialog")

        val loadingState = states.firstOrNull { it.isLoading } ?: BaseState()
        showLoadingDialog = loadingState.isLoading
    }

    //Dialogs
    if (showErrorDialog) {
        Log.d("WorkoutDetailsScreen", "Error: $error")
        error?.let {
            ErrorDialog(it, onErrorDialogDismiss)
        }
    }

    if (showDeleteExerciseDialog) {
        DeleteExerciseDialog(
            onClick = onDeleteExercise,
            onDismiss = { showDeleteExerciseDialog = false }
        )
    }

    if (showDeleteMultipleExercisesDialog) {
        DeleteMultipleExercisesDialog(
            totalExercises = selectedExercises.size,
            onClick =   onDeleteMultipleExercises,
            onDismiss = { showDeleteMultipleExercisesDialog = false }
        )
    }

    if (showEditWorkoutNameDialog) {
        EditWorkoutDialog(
            currentName = selectedWorkout.name,
            onDismiss = { showEditWorkoutNameDialog = false },
            onConfirm = onEditWorkoutName
        )
    }

    if (showDeleteWorkoutDialog) {
        WarningDialog(
            title = "Delete Workout",
            description = "Are you sure you want to delete this workout? This action cannot be undone.",
            positiveButtonTitle = "Delete",
            onClick = onDeleteWorkout,
            onDismiss = { showDeleteWorkoutDialog = false }
        )
    }


    if (showWorkoutConfigureDialog) {
        WorkoutConfigurationDialog(
            workoutConfiguration = workoutDetailsState.workoutDetails.configuration,
            onSave = onUpdateWorkoutConfiguration,
            onDismiss = { showWorkoutConfigureDialog = false }
        )
    }

    if (showFavoriteDialog) {
        FavoriteWorkoutDialog(actionTitle = "Favorite Workout",
            onClick = onFavoriteWorkout,
            onDismiss = { showFavoriteDialog = false }
        )
    }

    if (showUnfavoriteDialog) {
        FavoriteWorkoutDialog(actionTitle = "Unfavorite Workout",
            onClick = onUnfavoriteWorkout,
            onDismiss = {
                showUnfavoriteDialog = false
            }
        )
    }

    //Pull to refresh
    val pullRefreshState = rememberPullRefreshState(
        refreshing = workoutDetailsViewModel.isRefreshing,
        onRefresh = { workoutDetailsViewModel.getWorkoutDetails(workoutDetailsState.workoutDetails.workoutId) }
    )

    //Collapsable header state
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    val lazyListState = rememberLazyListState()
    val scrollThreshold = screenHeight / 2

    //firstVisibleItemIndex -> How many elements are scrolled from the screen.
    //First element is collapsable header. Second is workout banner and etc...
    //------------------------------------------------------------------------
    //firstVisibleItemScrollOffset -> current scroll session offset.
    //Resets once you stop scrolling.
    val showToolbar = remember {
        derivedStateOf {
            lazyListState.firstVisibleItemIndex > 0 ||
                    lazyListState.firstVisibleItemScrollOffset >= scrollThreshold.value
        }
    }
    val toolbarSize = 65.dp
    val toolbarHeight = animateDpAsState(
        targetValue = if (showToolbar.value) toolbarSize else 0.dp,
        label = "Main screen toolbar height"
    )
    val textAlpha by animateFloatAsState(
        targetValue = if (toolbarHeight.value > 40.dp) 1f else 0f,
        label = "Text alpha animation"
    )

    //Recompositions when scroll state changes
    val isLogging = false
    LaunchedEffect(lazyListState) {
        snapshotFlow { lazyListState.firstVisibleItemIndex }
            .collectLatest { index ->
                if (isLogging) Log.d("WorkoutDetailsScreen", "First visible item index: $index")
            }

        snapshotFlow { lazyListState.firstVisibleItemScrollOffset }
            .collectLatest { offset ->
                if (isLogging) Log.d(
                    "WorkoutDetailsScreen",
                    "First visible item scroll offset: $offset"
                )
            }
    }

    MainScreenScaffold(
        screenTitle = workoutTitle,
        onNavigateToDashboard = { workoutDetailsViewModel.onNavigateToDashboard() },
        onNavigateToWorkouts = { workoutDetailsViewModel.onNavigateToWorkouts() },
        onNavigateBackAction = { workoutDetailsViewModel.onNavigateBack() },
        onNavigateToSettings = { workoutDetailsViewModel.onNavigateToSettings() },
        animatedToolbarState = AnimatedToolbarState(
            showToolbar = showToolbar.value,
            toolbarHeight = toolbarHeight.value,
            textAlpha = textAlpha
        )
    ) { innerPadding ->
        val loadingWheelPadding = PaddingValues(
            top = toolbarSize + innerPadding.calculateTopPadding(), //Top toolbar padding
            start = innerPadding.calculateStartPadding(LayoutDirection.Rtl),
            end = innerPadding.calculateEndPadding(LayoutDirection.Rtl),
            bottom = innerPadding.calculateBottomPadding()
        )

        Box(
            Modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshState)
        ) {

            if (showLoadingDialog) {
                LoadingWheel(innerPadding = loadingWheelPadding)
            } else {

                //Exercises
                LazyColumn(
                    state = lazyListState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    //Collapsable header with toolbar
                    item {
                        StartWorkoutHeader(
                            modifier = Modifier.fillParentMaxHeight(),
                            title = workoutTitle,
                            subtitle = MuscleGroup.toDescription(selectedWorkout.muscleGroup),
                            isWorkoutFavorited = selectedWorkout.isFavorite,
                            onStartWorkoutAction = {
                                workoutDetailsViewModel.startWorkout()
                            },
                            onConfigureAction = { showWorkoutConfigureDialog = true },
                            onAddExerciseAction = { workoutDetailsViewModel.addExercise() },
                            onDeleteWorkoutAction = { showDeleteWorkoutDialog = true },
                            onEditWorkoutNameAction = { showEditWorkoutNameDialog = true },
                            onFavoriteWorkoutAction = { showFavoriteDialog = true },
                            onUnfavoriteWorkoutAction = { showUnfavoriteDialog = true },
                            onNavigateBackAction = { workoutDetailsViewModel.onNavigateBack() },
                            onNavigateToSettings = { workoutDetailsViewModel.onNavigateToSettings() }
                        )
                    }

                    Log.d("WorkoutDetailsScreen", "Exercises: $exercises")
                    items(exercises.size) { currentExerciseId ->
                        val currentExercise = exercises[currentExerciseId]

                        SwipeableExerciseBanner(
                            modifier = Modifier
                                .fillParentMaxWidth()
                                .height(200.dp),
                            exercise = currentExercise,
                            onClick = onExerciseSelected,
                            onLongPress = {
                                onDeleteModeEnabled()

                                selectedExercise = currentExercise
                                onExerciseSelected(currentExercise)
                            },
                            onDelete = {
                                selectedExercise = currentExercise
                                showDeleteExerciseDialog = true
                            },
                            isSelected = selectedExercises.contains(currentExercise)
                        )
                    }

                    item {
                        if (showAddExerciseBanner) { //Show only after data is fetched
                            AddExerciseToWorkoutBanner {

                                //Open search exercise screen
                                workoutDetailsViewModel.addExercise()
                            }
                        }
                    }
                }

                //Footer
                if (selectedExercises.isNotEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        SelectedExercisesRow(selectedExercises = selectedExercises)
                        DeleteExercisesRow(
                            modifier = Modifier.padding(
                                top = 8.dp,
                                start = 16.dp,
                                end = 16.dp,
                                bottom = 8.dp
                            ),
                            onDelete = {
                                showDeleteMultipleExercisesDialog = true
                            }
                        )
                    }
                }
            }

            PullRefreshIndicator(
                modifier = Modifier.align(Alignment.TopCenter),
                refreshing = workoutDetailsViewModel.isRefreshing,
                state = pullRefreshState
            ) //If put as first content -> hides behind the screen...
        }

        //Fixed place WorkoutDetails custom toolbar
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            StartWorkoutToolbar(
                onNavigateBackAction = { workoutDetailsViewModel.onNavigateBack() },
                onNavigateToSettings = { workoutDetailsViewModel.onNavigateToSettings() }
            )
        }
    }
}



