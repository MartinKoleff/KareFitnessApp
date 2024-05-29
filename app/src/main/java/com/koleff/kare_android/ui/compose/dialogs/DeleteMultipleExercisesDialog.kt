package com.koleff.kare_android.ui.compose.dialogs

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import kotlin.random.Random

@Composable
fun DeleteMultipleExercisesDialog(
    totalExercises: Int,
    onClick: () -> Unit,
    onDismiss: () -> Unit
) {
    WarningDialog(
        title = "Delete $totalExercises exercises",
        description = "Are you sure you want to delete these exercises? This action cannot be undone.",
        positiveButtonTitle = "Delete",
        onDismiss = onClick,
        onClick = onDismiss,
    )
}

@Preview
@Composable
private fun DeleteMultipleExercisesDialogPreview() {
    DeleteMultipleExercisesDialog(
        totalExercises = Random.nextInt(1, 100),
        onClick = {},
        onDismiss = {}
    )
}
