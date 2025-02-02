package com.koleff.kare_android.ui

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.koleff.kare_android.common.Constants
import com.koleff.kare_android.common.NotificationManager
import com.koleff.kare_android.common.navigation.AppNavigation
import com.koleff.kare_android.common.navigation.NavigationNotifier
import com.koleff.kare_android.ui.theme.KareTheme
import com.koleff.kare_android.ui.view_model.SplashScreenViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navigationNotifier: NavigationNotifier

    private val splashScreenViewModel by viewModels<SplashScreenViewModel>()

    @OptIn(ExperimentalComposeUiApi::class, ExperimentalAnimationApi::class, FlowPreview::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupSplashScreen()

        setContent {
            val splashScreenState = splashScreenViewModel.state.collectAsState()
            val hasOnboardedState = splashScreenViewModel.hasOnboardedState
            KareTheme {
                AppNavigation(
                    navigationNotifier,
                    hasSignedIn = splashScreenState.value.hasSignedIn,
                    hasOnboarded = hasOnboardedState
                )
            }
        }

        //Notification testing
        if (Constants.isTestingNotifications) {
            this.lifecycleScope.launch {
                delay(5000)

                NotificationManager.sendNotification(
                    this@MainActivity,
                    "Test Notification",
                    "This is a sample notification using the channel."
                )
            }
        }

        //Firebase Crashlytics testing
        if (Constants.isTestingFirebaseCrashlytics) {
            this.lifecycleScope.launch {
                delay(5000)

                throw RuntimeException("Testing Firebase Crashlytics")
            }
        }
    }

    private fun setupSplashScreen() {
        installSplashScreen().apply {

            //Duration on screen
            setKeepOnScreenCondition {
                !splashScreenViewModel.state.value.isSuccessful
            }

            //Animation
            setOnExitAnimationListener { screen ->
                val zoomX = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.SCALE_X,
                    0.4f,
                    0.0f
                )
                zoomX.interpolator = OvershootInterpolator()
                zoomX.duration = 500L
                zoomX.doOnEnd { screen.remove() }

                val zoomY = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.SCALE_Y,
                    0.4f,
                    0.0f
                )
                zoomY.interpolator = OvershootInterpolator()
                zoomY.duration = 500L
                zoomY.doOnEnd { screen.remove() }

                zoomX.start()
                zoomY.start()
            }
        }
    }
}
