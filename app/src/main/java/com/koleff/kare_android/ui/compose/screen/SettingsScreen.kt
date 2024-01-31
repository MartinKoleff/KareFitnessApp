package com.koleff.kare_android.ui.compose.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.koleff.kare_android.common.PermissionManager
import com.koleff.kare_android.ui.compose.scaffolds.MainScreenScaffold
import com.koleff.kare_android.ui.compose.SettingsList

@Composable
fun SettingsScreen(
    navController: NavHostController,
    isNavigationInProgress: MutableState<Boolean>
) {
    MainScreenScaffold("Settings", navController, isNavigationInProgress) { innerPadding ->
        val modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()

        //TODO: send broadcast?
        val context = LocalContext.current
        val notificationCallback: () -> Unit = { PermissionManager.requestNotificationPermission(context) } //TODO: update to boolean flag...
        val biometricsCallback: () -> Unit = {}
        val notificationIsChecked = remember {
            mutableStateOf(PermissionManager.hasNotificationPermission(context))
        }
//        val biometricsIsChecked = PermissionManager.hasBiometricsPermission(context)

        SettingsList(
            modifier = modifier,
            notificationCallback = notificationCallback,
            biometricsCallback = biometricsCallback,
            notificationIsChecked = notificationIsChecked.value,
            biometricsIsChecked = false
        )
    }
}