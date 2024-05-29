package com.koleff.kare_android.ui.compose.dialogs

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark

@Composable
fun WorkoutCompletedDialog(
    workoutName: String,
    onClick: () -> Unit,
) {
    SuccessDialog(
        title = "$workoutName completed successfully!",
        onDismiss = onClick,
        onClick = onClick
    )
}
@Preview
@PreviewLightDark
@Composable
fun WorkoutCompletedDialogPreview(
) {
    WorkoutCompletedDialog(
        workoutName = "Blow your arms workout",
        onClick = {}
    )
}
