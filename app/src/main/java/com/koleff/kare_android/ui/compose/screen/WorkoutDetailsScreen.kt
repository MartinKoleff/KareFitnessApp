package com.koleff.kare_android.ui.compose.screen

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.koleff.kare_android.R
import com.koleff.kare_android.common.navigation.Destination
import com.koleff.kare_android.common.navigation.NavigationEvent
import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.ui.compose.components.LoadingWheel
import com.koleff.kare_android.ui.compose.banners.AddExerciseToWorkoutBanner
import com.koleff.kare_android.ui.compose.banners.SwipeableExerciseBanner
import com.koleff.kare_android.ui.compose.dialogs.WarningDialog
import com.koleff.kare_android.ui.compose.components.navigation_components.scaffolds.MainScreenScaffold
import com.koleff.kare_android.ui.compose.dialogs.ErrorDialog
import com.koleff.kare_android.ui.view_model.WorkoutDetailsViewModel


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WorkoutDetailsScreen(
    workoutDetailsViewModel: WorkoutDetailsViewModel = hiltViewModel()
) {
    val workoutDetailsState by workoutDetailsViewModel.getWorkoutDetailsState.collectAsState()
    val workoutTitle =
        if (workoutDetailsState.workoutDetails.name == "") "Loading..." else workoutDetailsState.workoutDetails.name

    val onExerciseSelected: (ExerciseDto) -> Unit = { selectedExercise ->
        //TODO: select multiple exercises rework...

        //TODO: submit exercise directly and skip exercise details configurator...

        //Temporary use as start workout...
        workoutDetailsViewModel.onNavigationEvent(
            NavigationEvent.NavigateTo(
                Destination.DoWorkoutScreen(workoutId = workoutDetailsState.workoutDetails.workoutId)
            )
        )
    }

    val deleteExerciseState by workoutDetailsViewModel.deleteExerciseState.collectAsState()

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
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }


    //Dialog callbacks
    val onErrorDialogDismiss = {
        showErrorDialog = false
        workoutDetailsViewModel.clearError() //Enters launched effect to update showErrorDialog...
    }

    //Error handling
    var error by remember { mutableStateOf<KareError?>(null) }
    LaunchedEffect(workoutDetailsState.isError, deleteExerciseState.isError) {

        error = if (workoutDetailsState.isError) {
            workoutDetailsState.error
        } else if (deleteExerciseState.isError) {
            deleteExerciseState.error
        } else {
            null
        }

        showErrorDialog =
            workoutDetailsState.isError || deleteExerciseState.isError

        Log.d("WorkoutDetailsScreen", "Error detected -> $showErrorDialog")
    }

    //Dialogs
    if (showDeleteDialog) {
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
            onDismiss = { showDeleteDialog = false }
        )
    }

    if (showErrorDialog) {
        error?.let {
            ErrorDialog(error!!, onErrorDialogDismiss)
        }
    }

    //Pull to refresh
    val pullRefreshState = rememberPullRefreshState(
        refreshing = workoutDetailsViewModel.isRefreshing,
        onRefresh = { workoutDetailsViewModel.getWorkoutDetails(workoutDetailsState.workoutDetails.workoutId) }
    )

    //Navigation Callbacks
    val onNavigateToDashboard = {
        workoutDetailsViewModel.onNavigationEvent(NavigationEvent.NavigateTo(Destination.Dashboard))
    }
    val onNavigateToWorkouts = {
        workoutDetailsViewModel.onNavigationEvent(NavigationEvent.NavigateTo(Destination.Workouts))
    }
    val onNavigateToSettings = {
        workoutDetailsViewModel.onNavigationEvent(NavigationEvent.NavigateTo(Destination.Settings))
    }
    val onNavigateBack = { workoutDetailsViewModel.onNavigationEvent(NavigationEvent.NavigateBack) }

    MainScreenScaffold(
        workoutTitle,
        onNavigateToDashboard = onNavigateToDashboard,
        onNavigateToWorkouts = onNavigateToWorkouts,
        onNavigateBackAction = onNavigateBack,
        onNavigateToSettings = onNavigateToSettings
    ) { innerPadding ->
        val contentModifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)

        Box(
            Modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshState)
        ) {

            if (workoutDetailsState.isLoading || deleteExerciseState.isLoading) {
                LoadingWheel(innerPadding = innerPadding)
            } else {

                //Collapsable header


                //Exercises
                LazyColumn(
                    modifier = contentModifier,
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Log.d("WorkoutDetailsScreen", "Exercises: $exercises")
                    items(exercises.size) { currentExerciseId ->
                        val currentExercise = exercises[currentExerciseId]

                        SwipeableExerciseBanner(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            exercise = currentExercise,
                            onClick = onExerciseSelected,
                            onDelete = {
                                selectedExercise = currentExercise
                                showDeleteDialog = true
                            }
                        )
                    }

                    item {
                        if (showAddExerciseBanner) { //Show only after data is fetched
                            AddExerciseToWorkoutBanner {

                                //Open search exercise screen...
                                workoutDetailsViewModel.navigateToSearchExcercises(
                                    workoutId = workoutDetailsState.workoutDetails.workoutId
                                )
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
    val textColor = Color.Black

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
                color = MaterialTheme.colorScheme.primaryContainer,
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
    title: String,
    subtitle: String,
    onStartWorkoutAction: () -> Unit,
    onConfigureAction: () -> Unit,
    onAddExerciseAction: () -> Unit
) {
    Column {
        StartWorkoutTitleAndSubtitle(title, subtitle)

        StartWorkoutActionRow(onConfigureAction, onAddExerciseAction)

        StartWorkoutButton(text = "Start workout!", onStartWorkoutAction = onStartWorkoutAction)
    }
}

@Composable
fun StartWorkoutActionRow(onConfigureAction: () -> Unit, onAddExerciseAction: () -> Unit) {

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
                onAction = onAddExerciseAction
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
                onAction = onAddExerciseAction
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
    Box(
        modifier = Modifier
            .background(Color.Black)
    ) {
        StartWorkoutActionRow(
            onAddExerciseAction = onAddExerciseAction,
            onConfigureAction = onConfigureAction
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
                fontWeight = FontWeight.ExtraBold
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
                fontWeight = FontWeight.Light
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
    StartWorkoutHeader(
        title = "Workout 5",
        subtitle = "Biceps, triceps and forearms.",
        onStartWorkoutAction = onStartWorkoutAction,
        onConfigureAction = onConfigureAction,
        onAddExerciseAction = onAddExerciseAction
    )
}


//TODO: options row (settings, configure, select workout,
// edit workout name, add/delete exercises, delete workout

//TODO: Image background...

//TODO: workout configuration menu


