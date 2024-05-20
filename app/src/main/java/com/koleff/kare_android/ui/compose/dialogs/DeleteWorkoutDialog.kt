package com.koleff.kare_android.ui.compose.dialogs

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark

@Composable
fun DeleteWorkoutDialog(
    onClick: () -> Unit,
    onDismiss: () -> Unit
) {
    WarningDialog(
        title = "Delete Workout",
        description = "Are you sure you want to delete this workout? This action cannot be undone.",
        positiveButtonTitle = "Delete",
        onClick = onClick,
        onDismiss = onDismiss
    )
}

@Preview
@PreviewLightDark
@Composable
fun DeleteWorkoutDialogPreview() {
    DeleteWorkoutDialog(
        onClick = {

        },
        onDismiss = {

        }
    )
}