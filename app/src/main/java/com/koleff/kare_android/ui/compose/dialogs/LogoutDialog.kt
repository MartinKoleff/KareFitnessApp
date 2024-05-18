package com.koleff.kare_android.ui.compose.dialogs

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark

@Composable
fun LogoutDialog(
    onClick: () -> Unit,
    onDismiss: () -> Unit
) {
    WarningDialog(
        title = "Logout",
        description = "Are you sure you want to logout?",
        positiveButtonTitle = "Yes",
        negativeButtonTitle = "No",
        onClick = onClick,
        onDismiss = onDismiss
    )
}

@Preview
@PreviewLightDark
@Composable
fun LogoutDialogPreview(
) {
    LogoutDialog(
        onClick = {},
        onDismiss = {}
    )
}
