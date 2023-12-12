package com.koleff.kare_android.ui.compose.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.koleff.kare_android.R
import com.koleff.kare_android.data.MainScreen
import com.koleff.kare_android.data.model.dto.ExerciseDetailsDto
import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.model.dto.MachineType
import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.data.model.state.ExerciseDetailsState
import com.koleff.kare_android.ui.compose.scaffolds.ExerciseDetailsScreenScaffold
import com.koleff.kare_android.ui.compose.LoadingWheel
import com.koleff.kare_android.ui.compose.YoutubeVideoPlayer
import com.koleff.kare_android.ui.compose.navigation.FloatingNavigationItem
import com.koleff.kare_android.ui.view_model.ExerciseDetailsViewModel
import com.koleff.kare_android.ui.view_model.WorkoutDetailsViewModel

@Composable
fun WorkoutDetailsScreen(
    navController: NavHostController,
    workoutId: Int = -1, //Invalid workout selected...
    isNavigationInProgress: MutableState<Boolean>,
    workoutDetailsViewModel: WorkoutDetailsViewModel
) {


    val workoutDetailsState = workoutDetailsViewModel.state.collectAsState()
}

