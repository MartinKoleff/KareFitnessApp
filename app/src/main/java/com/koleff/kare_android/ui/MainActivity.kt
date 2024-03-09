package com.koleff.kare_android.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.lifecycle.lifecycleScope
import com.koleff.kare_android.common.Constants
import com.koleff.kare_android.common.NotificationManager
import com.koleff.kare_android.common.navigation.AppNavigation
import com.koleff.kare_android.common.navigation.NavigationNotifier
import com.koleff.kare_android.common.preferences.Preferences
import com.koleff.kare_android.ui.theme.KareTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.RuntimeException
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navigationNotifier: NavigationNotifier


    @OptIn(ExperimentalComposeUiApi::class, ExperimentalAnimationApi::class, FlowPreview::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KareTheme {
            }
        }

        //Notification testing
        if (Constants.isTestingNotifications) {
            this.lifecycleScope.launch {
                delay(5000)

                NotificationManager.sendNotification(this@MainActivity, "Test Notification", "This is a sample notification using the channel.")
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
}
