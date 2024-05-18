package com.koleff.kare_android.ui.compose.dialogs

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.koleff.kare_android.common.MockupDataGeneratorV2
import java.util.Locale

@Composable
fun FavoriteWorkoutDialog(
    actionTitle: String,
    onClick: () -> Unit,
    onDismiss: () -> Unit
) {
    WarningDialog(
        title = "$actionTitle Workout",
        description = "Are you sure you want to ${actionTitle.lowercase(Locale.getDefault())} this workout?",
        positiveButtonTitle = actionTitle,
        onClick = onClick,
        onDismiss = onDismiss
    )
}

@Preview
@PreviewLightDark
@Composable
fun FavoriteWorkoutDialogPreview() {
    val workout = MockupDataGeneratorV2.generateWorkout().copy(isFavorite = false)
    val selectWord = if (workout.isFavorite) "Unfavorite" else "Favorite"

    FavoriteWorkoutDialog(
        actionTitle = selectWord,
        onClick = {

        },
        onDismiss = {

        }
    )
}

@Preview
@PreviewLightDark
@Composable
fun UnfavoriteWorkoutDialogPreview() {
    val workout = MockupDataGeneratorV2.generateWorkout().copy(isFavorite = true)
    val selectWord = if (workout.isFavorite) "Unfavorite" else "Favorite"

    FavoriteWorkoutDialog(
        actionTitle = selectWord,
        onClick = {

        },
        onDismiss = {

        }
    )
}