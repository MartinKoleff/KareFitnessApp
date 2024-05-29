package com.koleff.kare_android.ui.compose.dialogs

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark

@Composable
fun DuplicateExercisesFoundDialog(
    onDismiss: () -> Unit,
    onSubmit: () -> Unit
) {
    WarningDialog(
        title = "Duplicate exercises found",
        description = "Some exercises are already found in your workout. Do you want to overwrite them?",
        positiveButtonTitle = "Overwrite and submit",
        onDismiss = onDismiss,
        onClick = onSubmit,
    )
}

@Preview
@PreviewLightDark
@Composable
fun DuplicateExercisesFoundDialogPreview() {
    DuplicateExercisesFoundDialog(
        onSubmit = {},
        onDismiss = {}
    )
}