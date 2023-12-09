package com.koleff.kare_android.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.compose.rememberNavController
import com.koleff.kare_android.ui.compose.navigation.SetupNavGraph
import com.koleff.kare_android.ui.theme.KareTheme
import com.koleff.kare_android.ui.view_model.ExerciseDetailsViewModel
import com.koleff.kare_android.ui.view_model.ExerciseViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var exerciseViewModelFactory: ExerciseViewModel.Factory

    @Inject
    lateinit var exerciseDetailsViewModelFactory: ExerciseDetailsViewModel.Factory

    @OptIn(ExperimentalComposeUiApi::class, ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KareTheme {
                val navController = rememberNavController()
                SetupNavGraph(
                    navController = navController,
                    exerciseViewModelFactory = exerciseViewModelFactory,
                    exerciseDetailsViewModelFactory = exerciseDetailsViewModelFactory
                )
            }
        }
    }
}
