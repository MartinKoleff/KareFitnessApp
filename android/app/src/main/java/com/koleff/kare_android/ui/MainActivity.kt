package com.koleff.kare_android.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.ExperimentalComposeUiApi
import com.koleff.kare_android.ui.compose.navigation.SetupNavGraph
import com.koleff.kare_android.ui.theme.KareTheme
import com.koleff.kare_android.ui.view_model.ExerciseDetailsConfiguratorViewModel
import com.koleff.kare_android.ui.view_model.ExerciseDetailsViewModel
import com.koleff.kare_android.ui.view_model.ExerciseListViewModel
import com.koleff.kare_android.ui.view_model.ExerciseViewModel
import com.koleff.kare_android.ui.view_model.WorkoutDetailsViewModel
import com.koleff.kare_android.ui.view_model.WorkoutViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var exerciseListViewModelFactory: ExerciseListViewModel.Factory

    @Inject
    lateinit var exerciseViewModelFactory: ExerciseViewModel.Factory

    @Inject
    lateinit var exerciseDetailsViewModelFactory: ExerciseDetailsViewModel.Factory

    @Inject
    lateinit var workoutDetailsViewModelFactory: WorkoutDetailsViewModel.Factory

    @Inject
    lateinit var exerciseDetailsConfiguratorViewModelFactory: ExerciseDetailsConfiguratorViewModel.Factory

    @OptIn(ExperimentalComposeUiApi::class, ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KareTheme {
                SetupNavGraph(
                    exerciseListViewModelFactory = exerciseListViewModelFactory,
                    exerciseViewModelFactory = exerciseViewModelFactory,
                    exerciseDetailsViewModelFactory = exerciseDetailsViewModelFactory,
                    workoutDetailsViewModelFactory = workoutDetailsViewModelFactory,
                    exerciseDetailsConfiguratorViewModelFactory = exerciseDetailsConfiguratorViewModelFactory
                )
            }
        }
    }
}
