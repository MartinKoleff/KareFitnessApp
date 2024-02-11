package com.koleff.kare_android.ui.compose.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import com.koleff.kare_android.common.NotificationManager
import com.koleff.kare_android.common.PermissionManager
import com.koleff.kare_android.common.navigation.Destination
import com.koleff.kare_android.common.navigation.NavigationEvent
import com.koleff.kare_android.ui.compose.scaffolds.MainScreenScaffold
import com.koleff.kare_android.ui.compose.components.SettingsList
import com.koleff.kare_android.ui.compose.dialogs.EnableNotificationsDialog
import com.koleff.kare_android.ui.compose.lifecycle_event.observeLifecycleEvent
import com.koleff.kare_android.ui.view_model.BaseViewModel

typealias SettingsViewModel = BaseViewModel

@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {

    //Navigation Callbacks
    val onNavigateToDashboard = {
        settingsViewModel.onNavigationEvent(NavigationEvent.NavigateTo(Destination.Dashboard))
    }
    val onNavigateToWorkouts = {
        settingsViewModel.onNavigationEvent(NavigationEvent.NavigateTo(Destination.Workouts))
    }
    val onNavigateToSettings = {
        settingsViewModel.onNavigationEvent(NavigationEvent.NavigateTo(Destination.Settings))
    }
    val onNavigateBack = { settingsViewModel.onNavigationEvent(NavigationEvent.NavigateBack) }

    MainScreenScaffold(
        "Settings",
        onNavigateToDashboard = onNavigateToDashboard,
        onNavigateToWorkouts = onNavigateToWorkouts,
        onNavigateBackAction = onNavigateBack,
        onNavigateToSettings = onNavigateToSettings
    ) { innerPadding ->
        val modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()

        val context = LocalContext.current

        //Used for API 33 below -> custom prompt
        var showNotificationDialog by remember {
            mutableStateOf(false)
        }

        val notificationCallback: () -> Unit = {
            showNotificationDialog = PermissionManager.requestNotificationPermission(context)
        }

        var notificationIsChecked by remember {
            mutableStateOf(PermissionManager.hasNotificationPermission(context))
        }

        val openNotificationSettingsCallback = {
            NotificationManager.openNotificationSettings(context)

            showNotificationDialog = false
        }

        //Update switch states on Activity OnResume.
        observeLifecycleEvent { event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                notificationIsChecked = PermissionManager.hasNotificationPermission(context)
            }
        }

        if (showNotificationDialog) {
            EnableNotificationsDialog(
                onClick = openNotificationSettingsCallback,
                onDismiss = {
                    showNotificationDialog = false
                    notificationIsChecked = PermissionManager.hasNotificationPermission(context)
                }
            )
        }

        val biometricsCallback: () -> Unit = {}
        var biometricsIsChecked by remember {
            mutableStateOf(PermissionManager.hasBiometricsPermission(context))
        }

        SettingsList(
            modifier = modifier,
            notificationIsChecked = notificationIsChecked,
            biometricsIsChecked = false,
            onNotificationSwitchChange = { newState ->
                notificationIsChecked = newState

                notificationCallback()
            },
            onBiometricsSwitchChange = { newState ->
                biometricsIsChecked = newState

                biometricsCallback()
            }
        )
    }
}