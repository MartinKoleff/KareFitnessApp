package com.koleff.kare_android.ui.compose.screen

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.koleff.kare_android.common.NotificationManager
import com.koleff.kare_android.common.PermissionManager
import com.koleff.kare_android.common.navigation.Destination
import com.koleff.kare_android.common.navigation.NavigationEvent
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.ui.compose.components.LoadingWheel
import com.koleff.kare_android.ui.compose.components.navigation_components.scaffolds.MainScreenScaffold
import com.koleff.kare_android.ui.compose.components.SettingsList
import com.koleff.kare_android.ui.compose.dialogs.EnableNotificationsDialog
import com.koleff.kare_android.ui.compose.dialogs.ErrorDialog
import com.koleff.kare_android.ui.compose.dialogs.LogoutDialog
import com.koleff.kare_android.ui.state.BaseState
import com.koleff.kare_android.ui.view_model.BaseViewModel
import com.koleff.kare_android.ui.view_model.SettingsViewModel

@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {

    val logoutState by settingsViewModel.logoutState.collectAsState()

    val context = LocalContext.current

    //Dialog visibility
    var showErrorDialog by remember { mutableStateOf(false) }
    var showLoadingDialog by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    //Used for API 33 below -> custom prompt
    var showNotificationDialog by remember { mutableStateOf(false) }


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

    //TODO: show biometrics dialog...
    val biometricsCallback: () -> Unit = {}
    var biometricsIsChecked by remember {
        mutableStateOf(PermissionManager.hasBiometricsPermission(context))
    }


    //Update switch states on Activity OnResume.
    observeLifecycleEvent { event ->
        if (event == Lifecycle.Event.ON_RESUME) {
            notificationIsChecked = PermissionManager.hasNotificationPermission(context)
        }
    }

    val onLogout = {
        settingsViewModel.logout()

        showLogoutDialog = false
    }

    //Error handling
    val onErrorDialogDismiss = {
        showErrorDialog = false
        settingsViewModel.clearError() //Enters launched effect to update showErrorDialog...
    }

    var error by remember { mutableStateOf<KareError?>(null) }
    LaunchedEffect(logoutState) {
        val states = listOf(logoutState)

        val errorState: BaseState = states.firstOrNull { it.isError } ?: BaseState()
        error = errorState.error
        showErrorDialog = errorState.isError
        Log.d("SettingsScreen", "Error detected -> $showErrorDialog")

        val loadingState: BaseState = states.firstOrNull { it.isLoading } ?: BaseState()
        showLoadingDialog = loadingState.isLoading
    }

    if (showErrorDialog) {
        error?.let {
            ErrorDialog(it, onErrorDialogDismiss)
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

    if (showLogoutDialog) {
        LogoutDialog(
            onClick = onLogout,
            onDismiss = { showLogoutDialog = false }
        )
    }

    MainScreenScaffold(
        "Settings",
        onNavigateToDashboard = { settingsViewModel.onNavigateToDashboard() },
        onNavigateToWorkouts = { settingsViewModel.onNavigateToWorkouts() },
        onNavigateBackAction = { settingsViewModel.onNavigateBack() },
        onNavigateToSettings = { settingsViewModel.onNavigateToSettings() }
    ) { innerPadding ->
        val modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()

        if (showLoadingDialog) {
            LoadingWheel(innerPadding = innerPadding, hideScreen = true)
        } else {
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
                },
                onLogout = {
                    showLogoutDialog = true
                }
            )
        }
    }
}

@Composable
fun observeLifecycleEvent(
    onEvent: (Lifecycle.Event) -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            onEvent(event)
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}