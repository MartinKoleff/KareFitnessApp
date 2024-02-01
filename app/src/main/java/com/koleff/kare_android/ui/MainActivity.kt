package com.koleff.kare_android.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.ExperimentalComposeUiApi
import com.koleff.kare_android.common.Constants
import com.koleff.kare_android.common.NotificationManager
import com.koleff.kare_android.ui.compose.navigation.SetupNavGraph
import com.koleff.kare_android.ui.theme.KareTheme
import com.koleff.kare_android.ui.view_model.ExerciseDetailsConfiguratorViewModel
import com.koleff.kare_android.ui.view_model.ExerciseListViewModel
import com.koleff.kare_android.ui.view_model.ExerciseViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var exerciseViewModelFactory: ExerciseViewModel.Factory

    @Inject
    lateinit var exerciseDetailsConfiguratorViewModelFactory: ExerciseDetailsConfiguratorViewModel.Factory

    @OptIn(ExperimentalComposeUiApi::class, ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KareTheme {
                SetupNavGraph(
                    exerciseViewModelFactory = exerciseViewModelFactory,
                    exerciseDetailsConfiguratorViewModelFactory = exerciseDetailsConfiguratorViewModelFactory
                )
            }
        }

        if (Constants.isTestingNotifications) {
            GlobalScope.launch {
                delay(5000)

                NotificationManager.sendNotification(this@MainActivity, "Test Notification", "This is a sample notification using the channel.")
            }
        }
    }
}
