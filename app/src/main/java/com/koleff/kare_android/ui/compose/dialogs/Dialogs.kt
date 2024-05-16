package com.koleff.kare_android.ui.compose.dialogs

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.koleff.kare_android.R
import com.koleff.kare_android.common.MockupDataGeneratorV2
import com.koleff.kare_android.data.model.response.base_response.KareError
import java.util.Locale

@Composable
fun EditWorkoutDialog(
    title: String = "Edit workout name",
    currentName: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var text by remember { mutableStateOf(currentName) }

    val buttonColor = MaterialTheme.colorScheme.tertiary
    val onButtonColor = MaterialTheme.colorScheme.onTertiary

    val titleTextColor = MaterialTheme.colorScheme.onSurface
    val titleTextStyle = MaterialTheme.typography.headlineMedium.copy(
        color = titleTextColor
    )

    val textColor = MaterialTheme.colorScheme.onSurface
    val textStyle = MaterialTheme.typography.titleMedium.copy(
        color = textColor
    )

    AlertDialog(
        title = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = title,
                    style = titleTextStyle,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        },
        text = {
            TextField(
                value = text,
                onValueChange = { text = it },
                singleLine = true
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(text)
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = buttonColor,
                    contentColor = textColor
                )
            ) {
                Text(
                    text = "Confirm",
                    style = textStyle,
                    textAlign = TextAlign.End,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        },
        dismissButton = {
            Button(
                onClick = { onDismiss() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = buttonColor,
                    contentColor = onButtonColor
                )
            ) {
                Text(
                    text = "Cancel",
                    style = textStyle,
                    textAlign = TextAlign.End,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        },
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    )
}

@Composable
fun SuccessDialog(
    title: String,
    description: String = "",
    onDismiss: () -> Unit,
    onClick: () -> Unit,
) {
    val buttonColor = MaterialTheme.colorScheme.tertiary
    val onButtonColor = MaterialTheme.colorScheme.onTertiary

    val titleTextColor = MaterialTheme.colorScheme.onSurface
    val titleTextStyle = MaterialTheme.typography.headlineMedium.copy(
        color = titleTextColor
    )

    val textColor = MaterialTheme.colorScheme.onSurface
    val textStyle = MaterialTheme.typography.titleMedium.copy(
        color = textColor
    )

    val buttonTextStyle = MaterialTheme.typography.titleSmall.copy(
        color = textColor
    )

    AlertDialog(
        title = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = title,
                    style = titleTextStyle,
                    textAlign = TextAlign.Center,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
        },
        text = {
            if (description.isNotEmpty()) {
                Text(
                    text = description,
                    style = textStyle,
                    textAlign = TextAlign.Center,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
        },
        confirmButton = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = {
                        onClick()
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = buttonColor,
                        contentColor = onButtonColor
                    )
                ) {
                    Text(
                        text = "OK",
                        style = buttonTextStyle,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        },
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    )
}

@Composable
fun WarningDialog(
    title: String,
    description: String,
    onDismiss: () -> Unit,
    onClick: () -> Unit,
    actionButtonTitle: String,
    callDismissOnConfirm: Boolean = true
) {
    val buttonColor = MaterialTheme.colorScheme.tertiary
    val onButtonColor = MaterialTheme.colorScheme.onTertiary

    val titleTextColor = MaterialTheme.colorScheme.onSurface
    val titleTextStyle = MaterialTheme.typography.headlineMedium.copy(
        color = titleTextColor
    )

    val textColor = MaterialTheme.colorScheme.onSurface
    val textStyle = MaterialTheme.typography.titleMedium.copy(
        color = textColor
    )

    val buttonTextStyle = MaterialTheme.typography.titleSmall.copy(
        color = textColor
    )

    AlertDialog(
        title = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = title,
                    style = titleTextStyle,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        },
        text = {
            Text(
                text = description,
                style = textStyle,
                textAlign = TextAlign.Center,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    onClick()
                    if (callDismissOnConfirm) onDismiss()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = buttonColor,
                    contentColor = onButtonColor
                )
            ) {
                Text(
                    text = actionButtonTitle,
                    style = buttonTextStyle,
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = buttonColor,
                    contentColor = onButtonColor
                )
            ) {
                Text(
                    text = "Cancel",
                    style = buttonTextStyle,
                    textAlign = TextAlign.End,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        },
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    )
}

@Composable
fun ErrorDialog(error: KareError, onDismiss: () -> Unit) {
    val context = LocalContext.current
    val errorMessage =
        context.resources.getString(error.errorMessageResourceId) + " " + error.extraMessage
    val title = "Error"

    val buttonColor = MaterialTheme.colorScheme.tertiary
    val onButtonColor = MaterialTheme.colorScheme.onTertiary

    val titleTextColor = MaterialTheme.colorScheme.onSurface
    val titleTextStyle = MaterialTheme.typography.headlineMedium.copy(
        color = titleTextColor
    )

    val textColor = MaterialTheme.colorScheme.onSurface
    val textStyle = MaterialTheme.typography.titleMedium.copy(
        color = textColor
    )

    val buttonTextStyle = MaterialTheme.typography.titleSmall.copy(
        color = textColor
    )

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp), //, vertical = 4.dp
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = title,
                    style = titleTextStyle,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        },
        text = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = errorMessage,
                    style = textStyle,
                    textAlign = TextAlign.Center,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onDismiss() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = buttonColor,
                    contentColor = onButtonColor
                )
            ) {
                Text(
                    text = "OK",
                    style = buttonTextStyle,
                    textAlign = TextAlign.End,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        },
        icon = {
            Image(
                modifier = Modifier.size(50.dp),
                painter = painterResource(id = R.drawable.ic_vector_event_warning),
                contentScale = ContentScale.Crop,
                contentDescription = "Warning icon"
            )
        }
    )
}

@Composable
fun EnableNotificationsDialog(
    onClick: () -> Unit,
    onDismiss: () -> Unit
) {
    WarningDialog(
        title = "Enable Notifications",
        description = "Notifications are important for our app. Please enable them in settings.",
        actionButtonTitle = "Open Settings",
        onClick = onClick,
        onDismiss = onDismiss,
        callDismissOnConfirm = false
    )
}

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


@Composable
fun DuplicateExercisesFoundDialog(
    onDismiss: () -> Unit,
    onSubmit: () -> Unit
) {
    WarningDialog(
        title = "Duplicate exercises found",
        description = "Some exercises are already found in your workout. Do you want to overwrite them?",
        actionButtonTitle = "Overwrite and submit",
        onDismiss = onDismiss,
        onClick = onSubmit,
    )
}


@Composable
fun FavoriteWorkoutDialog(
    actionTitle: String,
    onClick: () -> Unit,
    onDismiss: () -> Unit
) {
    WarningDialog(
        title = "$actionTitle Workout",
        description = "Are you sure you want to ${actionTitle.lowercase(Locale.getDefault())} this workout?",
        actionButtonTitle = actionTitle,
        onClick = onClick,
        onDismiss = onDismiss
    )
}

@Composable
fun DeleteWorkoutDialog(
    onClick: () -> Unit,
    onDismiss: () -> Unit
) {
    WarningDialog(
        title = "Delete Workout",
        description = "Are you sure you want to delete this workout? This action cannot be undone.",
        actionButtonTitle = "Delete",
        onClick = onClick,
        onDismiss = onDismiss
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

@Preview
@PreviewLightDark
@Composable
fun EnableNotificationsDialogPreview() {
    EnableNotificationsDialog(onClick = {}, onDismiss = {})
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

@Preview
@PreviewLightDark
@Composable
fun SuccessDialogPreview() {
    SuccessDialog(
        title = "Workout added successfully",
        onDismiss = {},
        onClick = {}
    )
}

@Preview
@PreviewLightDark
@Composable
fun EditWorkoutNameDialogPreview() {
    val onConfirm: (String) -> Unit = {}

    EditWorkoutDialog(
        onDismiss = {},
        onConfirm = onConfirm,
        currentName = "Workout 1"
    )
}

@Preview
@PreviewLightDark
@Composable
fun ErrorDialogPreview() {
    val error = KareError.INVALID_CREDENTIALS.apply {
        extraMessage = "Username must be at least 4 characters long."
    }

    ErrorDialog(
        error = error,
        onDismiss = {}
    )
}