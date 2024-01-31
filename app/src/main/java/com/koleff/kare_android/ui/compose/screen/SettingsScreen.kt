package com.koleff.kare_android.ui.compose.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import com.koleff.kare_android.common.NotificationManager
import com.koleff.kare_android.common.PermissionManager
import com.koleff.kare_android.ui.compose.scaffolds.MainScreenScaffold
import com.koleff.kare_android.ui.compose.SettingsList
import com.koleff.kare_android.ui.compose.dialogs.EnableNotificationsDialog
import com.koleff.kare_android.ui.compose.lifecycle_event.observeLifecycleEvent

@Composable
fun SettingsScreen(
    navController: NavHostController,
    isNavigationInProgress: MutableState<Boolean>
) {
    MainScreenScaffold("Settings", navController, isNavigationInProgress) { innerPadding ->
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