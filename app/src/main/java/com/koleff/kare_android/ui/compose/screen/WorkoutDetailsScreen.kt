package com.koleff.kare_android.ui.compose.screen

import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.koleff.kare_android.R
import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.data.model.dto.WorkoutConfigurationDto
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.ui.compose.banners.AddExerciseToWorkoutBanner
import com.koleff.kare_android.ui.compose.banners.SwipeableExerciseBanner
import com.koleff.kare_android.ui.compose.components.LoadingWheel
import com.koleff.kare_android.ui.compose.components.navigation_components.NavigationItem
import com.koleff.kare_android.ui.compose.components.navigation_components.scaffolds.MainScreenScaffold
import com.koleff.kare_android.ui.compose.dialogs.EditWorkoutDialog
import com.koleff.kare_android.ui.compose.dialogs.ErrorDialog
import com.koleff.kare_android.ui.compose.dialogs.WarningDialog
import com.koleff.kare_android.ui.compose.dialogs.WorkoutConfigurationDialog
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

    val workoutTitle =
        if (workoutDetailsState.workoutDetails.name == "" || updateWorkoutDetailsState.isLoading) "Loading..."
        else workoutDetailsState.workoutDetails.name

    val onExerciseSelected: (ExerciseDto) -> Unit = { selectedExercise ->
        workoutDetailsViewModel.navigateToExerciseDetailsConfigurator(selectedExercise)
    }

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
    var showEditWorkoutNameDialog by remember { mutableStateOf(false) }
    var showSelectDialog by remember { mutableStateOf(false) }
    var showDeleteWorkoutDialog by remember { mutableStateOf(false) }
    var showWorkoutConfigureDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var showLoadingDialog by remember { mutableStateOf(false) }

    //Dialog callbacks
    val onDeleteWorkout: () -> Unit = {
        workoutDetailsViewModel.deleteWorkout()

        showDeleteWorkoutDialog = false
    }

//    val onSelectWorkout: () -> Unit = {
//        workoutDetailsViewModel.selectWorkout(selectedWorkout.workoutId)
//
//        showSelectDialog = false
//    }

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
        createWorkoutState
    ) {
        val states = listOf(
            workoutDetailsState,
            deleteExerciseState,
            deleteWorkoutDetailsState,
            updateWorkoutDetailsState,
            startWorkoutState,
            createWorkoutState
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
        WarningDialog(
            title = "Delete Exercise",
            description = "Are you sure you want to delete this exercise? This action cannot be undone.",
            actionButtonTitle = "Delete",
            onClick = {
                selectedExercise?.let {
                    workoutDetailsViewModel.deleteExercise(
                        workoutDetailsState.workoutDetails.workoutId,
                        selectedExercise!!.exerciseId
                    )
                }

            },
            onDismiss = { showDeleteExerciseDialog = false }
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
            actionButtonTitle = "Delete",
            onClick = onDeleteWorkout,
            onDismiss = { showDeleteWorkoutDialog = false }
        )
    }


    if (showWorkoutConfigureDialog) {
        WorkoutConfigurationDialog(
            workoutConfiguration = workoutDetailsState.workoutDetails.configuration,
            onSave = onUpdateWorkoutConfiguration,
            onDismiss = {
                showWorkoutConfigureDialog = false
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

        //Fixed WorkoutDetails custom toolbar
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
            StartWorkoutToolbar(
                onNavigateBackAction = { workoutDetailsViewModel.onNavigateBack() },
                onNavigateToSettings = { workoutDetailsViewModel.onNavigateToSettings() }
            )
        }

        val contentModifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)

        val paddingValues = PaddingValues(
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
                LoadingWheel(innerPadding = paddingValues)
            } else {
                //Exercises
                LazyColumn(
                    state = lazyListState,
                    modifier = contentModifier,
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    //Collapsable header with toolbar
                    item {
                        StartWorkoutHeader(
                            modifier = Modifier.fillParentMaxHeight(),
                            title = workoutTitle,
                            subtitle = MuscleGroup.toDescription(selectedWorkout.muscleGroup),
                            onStartWorkoutAction = {
                                workoutDetailsViewModel.startWorkout()
                            },
                            onConfigureAction = { showWorkoutConfigureDialog = true },
                            onAddExerciseAction = { workoutDetailsViewModel.addExercise() },
                            onDeleteWorkoutAction = { showDeleteWorkoutDialog = true },
                            onEditWorkoutNameAction = { showEditWorkoutNameDialog = true },
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
                            onDelete = {
                                selectedExercise = currentExercise
                                showDeleteExerciseDialog = true
                            }
                        )
                    }

                    item {
                        if (showAddExerciseBanner) { //Show only after data is fetched
                            AddExerciseToWorkoutBanner {

                                //Open search exercise screen...
                                workoutDetailsViewModel.addExercise()
                            }
                        }
                    }
                }
            }

            PullRefreshIndicator(
                modifier = Modifier.align(Alignment.TopCenter),
                refreshing = workoutDetailsViewModel.isRefreshing,
                state = pullRefreshState
            ) //If put as first content -> hides behind the screen...
        }
    }
}

@Composable
fun StartWorkoutButton(
    text: String,
    onStartWorkoutAction: () -> Unit,
) {
    val cornerSize = 24.dp
    val paddingValues = PaddingValues(
        horizontal = 32.dp,
        vertical = 8.dp
    )
    val textColor = Color.White

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingValues)
            .height(50.dp)
            .clip(RoundedCornerShape(cornerSize))
            .border(
                border = BorderStroke(2.dp, color = Color.White),
                shape = RoundedCornerShape(cornerSize)
            )
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(cornerSize)
            )
            .clickable(onClick = { onStartWorkoutAction() }),
        contentAlignment = Alignment.Center
    ) {

        //Sign in text
        Text( //TODO: and cooler font...
            modifier = Modifier.padding(
                PaddingValues(8.dp)
            ),
            text = text,
            style = TextStyle(
                color = textColor,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun StartWorkoutHeader(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    onStartWorkoutAction: () -> Unit,
    onConfigureAction: () -> Unit,
    onAddExerciseAction: () -> Unit,
    onDeleteWorkoutAction: () -> Unit,
    onEditWorkoutNameAction: () -> Unit,
    onNavigateBackAction: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    Box(modifier = modifier) {
//        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
//            StartWorkoutToolbar(onNavigateBackAction, onNavigateToSettings)
//        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            StartWorkoutTitleAndSubtitle(title, subtitle)

            StartWorkoutActionRow(
                onConfigureAction = onConfigureAction,
                onAddExerciseAction = onAddExerciseAction,
                onDeleteWorkoutAction = onDeleteWorkoutAction,
                onEditWorkoutNameAction = onEditWorkoutNameAction
            )

            StartWorkoutButton(text = "Start workout!", onStartWorkoutAction = onStartWorkoutAction)
        }
    }
}

@Composable
fun StartWorkoutToolbar(
    onNavigateBackAction: () -> Unit,
    onNavigateToSettings: () -> Unit,
    toolbarHeight: Dp = 65.dp
) {
    val color = Color.White
    val tintColor = Color.Black
    val cornerSize = 24.dp
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(toolbarHeight),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        NavigationItem(
            modifier = Modifier
                .drawBehind {
                    drawCircle(
                        color = color,
                        radius = this.size.maxDimension / 3
                    )
                },
            icon = Icons.AutoMirrored.Filled.ArrowBackIos,
            label = "Go back",
            onNavigateAction = onNavigateBackAction,
            tint = tintColor
        )

        NavigationItem(
            modifier = Modifier
                .drawBehind {
                    drawCircle(
                        color = color,
                        radius = this.size.maxDimension / 3
                    )
                },
            icon = Icons.Filled.Settings,
            label = "Settings",
            onNavigateAction = onNavigateToSettings,
            tint = tintColor
        )
    }
}

@Preview
@Composable
fun StartWorkoutToolbarPreview() {
    StartWorkoutToolbar(
        onNavigateBackAction = {},
        onNavigateToSettings = {}
    )
}

@Composable
fun StartWorkoutActionRow(
    onConfigureAction: () -> Unit,
    onAddExerciseAction: () -> Unit,
    onDeleteWorkoutAction: () -> Unit,
    onEditWorkoutNameAction: () -> Unit
) {

    //Same as StartWorkoutButton
    val paddingValues = PaddingValues(
        horizontal = 32.dp
    )
    val actionColumnHeight = 150.dp

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(actionColumnHeight)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(actionColumnHeight / 2)
                .padding(paddingValues),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            StartWorkoutDynamicActionButton(
                modifier = Modifier.weight(1f),
                initialText = "Save",
                changedText = "Saved",
                initialIconResourceId = R.drawable.ic_heart_outline,
                changedIconResourceId = R.drawable.ic_heart_full
            )

            StartWorkoutActionButton(
                modifier = Modifier.weight(1f),
                text = "Add exercise",
                iconResourceId = R.drawable.ic_vector_add,
                onAction = onAddExerciseAction
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(actionColumnHeight / 2)
                .padding(paddingValues),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            StartWorkoutActionButton(
                modifier = Modifier.weight(1f),
                text = "Edit workout name",
                iconResourceId = R.drawable.ic_edit,
                onAction = onEditWorkoutNameAction
            )

            StartWorkoutActionButton(
                modifier = Modifier.weight(1f),
                text = "Configure workout",
                iconResourceId = R.drawable.ic_vector_settings,
                onAction = onConfigureAction
            )

            StartWorkoutActionButton(
                modifier = Modifier.weight(1f),
                text = "Delete workout",
                iconResourceId = R.drawable.ic_delete,
                onAction = onDeleteWorkoutAction
            )
        }
    }
}

@Composable
fun StartWorkoutActionButton(
    modifier: Modifier = Modifier,
    text: String,
    iconResourceId: Int,
    onAction: () -> Unit
) {
    val textColor = Color.White
    val tintColor = Color.White
    val iconSize = 30.dp
    val paddingValues = PaddingValues(2.dp)

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        //Image
        IconButton(
            modifier = Modifier
                .size(iconSize)
                .padding(paddingValues),
            onClick = {
                onAction()
            }) {

            Icon(
                painter = painterResource(id = iconResourceId),
                contentDescription = "Start workout action button",
                tint = tintColor
            )
        }
        //Text
        Text(
            modifier = Modifier.padding(paddingValues),
            text = text,
            style = TextStyle(
                color = textColor,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal
            ),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun StartWorkoutDynamicActionButton(
    modifier: Modifier = Modifier,
    initialText: String,
    changedText: String,
    initialIconResourceId: Int,
    changedIconResourceId: Int,
    onAction: () -> Unit = {},
) {
    var isSaved by remember {
        mutableStateOf(false) //TODO: wire with shared preferences...
    }

    val textColor = Color.White
    val tintColor = Color.White
    val iconSize = 30.dp
    val paddingValues = 2.dp

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        //Image
        IconButton(
            modifier = Modifier.size(iconSize),
            onClick = {
                isSaved = !isSaved

                onAction()
            }) {
            Icon(
                painter = painterResource(id = if (isSaved) changedIconResourceId else initialIconResourceId),
                contentDescription = "Start workout action button",
                tint = tintColor
            )
        }

        //Text
        Text(
            modifier = Modifier.padding(
                paddingValues
            ),
            text = if (isSaved) changedText else initialText,
            style = TextStyle(
                color = textColor,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview
@Composable
fun StartWorkoutActionButtonPreview() {
    StartWorkoutActionButton(text = "Save", iconResourceId = R.drawable.ic_heart_outline) {

    }
}

@Preview
@Composable
fun StartWorkoutActionRowPreview() {
    val onAddExerciseAction = {}
    val onConfigureAction = {}
    val onDeleteWorkoutAction = {}
    val onEditWorkoutNameAction = {}
    Box(
        modifier = Modifier
            .background(Color.Black)
    ) {
        StartWorkoutActionRow(
            onAddExerciseAction = onAddExerciseAction,
            onConfigureAction = onConfigureAction,
            onDeleteWorkoutAction = onDeleteWorkoutAction,
            onEditWorkoutNameAction = onEditWorkoutNameAction
        )
    }

}

@Composable
fun StartWorkoutTitleAndSubtitle(
    title: String,
    subtitle: String
) {
    val textColor = Color.White

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
            bottom = 64.dp,
            start = 8.dp,
            end = 8.dp
        )

    //Title
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.padding(
                titlePadding
            ),
            text = title,
            style = TextStyle(
                color = textColor,
                fontSize = 40.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            ),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }

    //Subtitle
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.padding(
                subtitlePadding
            ),
            text = subtitle,
            style = TextStyle(
                color = textColor,
                fontSize = 18.sp,
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.Center
            ),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview
@Composable
fun StartWorkoutHeaderPreview() {
    val onStartWorkoutAction = {}
    val onConfigureAction = {}
    val onAddExerciseAction = {}
    val onEditWorkoutNameAction = {}
    val onDeleteWorkoutAction = {}
    val onNavigateBackAction = {}
    val onNavigateToSettings = {}
    StartWorkoutHeader(
        modifier = Modifier.fillMaxSize(),
        title = "Arnold destroy back workout",
        subtitle = "Biceps, triceps and forearms.",
        onStartWorkoutAction = onStartWorkoutAction,
        onConfigureAction = onConfigureAction,
        onAddExerciseAction = onAddExerciseAction,
        onEditWorkoutNameAction = onEditWorkoutNameAction,
        onDeleteWorkoutAction = onDeleteWorkoutAction,
        onNavigateBackAction = onNavigateBackAction,
        onNavigateToSettings = onNavigateToSettings
    )
}

//TODO: Image background...

//TODO: workout configuration menu


