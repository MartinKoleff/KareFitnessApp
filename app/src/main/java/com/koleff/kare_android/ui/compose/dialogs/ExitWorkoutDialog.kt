package com.koleff.kare_android.ui.compose.dialogs

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark

@Composable
fun ExitWorkoutDialog(
    workoutName: String,
    onClick: () -> Unit,
    onDismiss: () -> Unit,
) {
    SuccessDialog(
        title = "Exit workout",
        description = "Do you want to exit $workoutName? The progress you have made won't be saved.",
        onDismiss = onDismiss,
        onClick = onClick
    )
}
@Preview
@PreviewLightDark
@Composable
fun ExitWorkoutDialogPreview(
) {
    ExitWorkoutDialog(
        workoutName = "Blow your arms workout",
        onClick = {},
        onDismiss = {}
    )
}