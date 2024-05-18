package com.koleff.kare_android.ui.compose.dialogs

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark

@Composable
fun EnableNotificationsDialog(
    onClick: () -> Unit,
    onDismiss: () -> Unit
) {
    WarningDialog(
        title = "Enable Notifications",
        description = "Notifications are important for our app. Please enable them in settings.",
        positiveButtonTitle = "Open Settings",
        onClick = onClick,
        onDismiss = onDismiss,
        callDismissOnConfirm = false
    )
}

@Preview
@PreviewLightDark
@Composable
fun EnableNotificationsDialogPreview() {
    EnableNotificationsDialog(onClick = {}, onDismiss = {})
}