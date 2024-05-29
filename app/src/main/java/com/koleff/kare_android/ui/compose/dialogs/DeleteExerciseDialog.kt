package com.koleff.kare_android.ui.compose.dialogs

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import kotlin.random.Random

@Composable
fun DeleteExerciseDialog(
    onClick: () -> Unit,
    onDismiss: () -> Unit
){
    WarningDialog(
        title = "Delete Exercise",
        description = "Are you sure you want to delete this exercise? This action cannot be undone.",
        positiveButtonTitle = "Delete",
        onDismiss = onClick,
        onClick = onDismiss,
    )
}

@Preview
@Composable
private fun DeleteExerciseDialogPreview() {
    DeleteExerciseDialog(
        onClick = {},
        onDismiss = {}
    )
}