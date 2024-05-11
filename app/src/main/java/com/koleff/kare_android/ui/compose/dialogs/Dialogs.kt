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

    val titleTextColor = MaterialTheme.colorScheme.onPrimary
    val textColor = MaterialTheme.colorScheme.onSecondary
    val buttonColor = MaterialTheme.colorScheme.tertiary
    val onButtonColor = MaterialTheme.colorScheme.onTertiary

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
                    style = TextStyle(
                        color = titleTextColor,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    ),
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
                    style = TextStyle(
                        color = textColor,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    ),
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
                    style = TextStyle(
                        color = textColor,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    ),
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
    val titleTextColor = MaterialTheme.colorScheme.onPrimary
    val textColor = MaterialTheme.colorScheme.onSecondary
    val buttonColor = MaterialTheme.colorScheme.tertiary
    val onButtonColor = MaterialTheme.colorScheme.onTertiary

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
                    style = TextStyle(
                        color = titleTextColor,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    ),
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
                    style = TextStyle(
                        color = textColor,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal
                    ),
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
                        style = TextStyle(
                            color = textColor,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        ),
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
    val titleTextColor = MaterialTheme.colorScheme.onPrimary
    val textColor = MaterialTheme.colorScheme.onSecondary
    val buttonColor = MaterialTheme.colorScheme.tertiary
    val onButtonColor = MaterialTheme.colorScheme.onTertiary

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
                    style = TextStyle(
                        color = titleTextColor,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        },
        text = {
            Text(
                text = description,
                style = TextStyle(
                    color = textColor,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal
                ),
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
                    style = TextStyle(
                        color = textColor,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    textAlign = TextAlign.End,
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
                    style = TextStyle(
                        color = textColor,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    ),
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

    val titleTextColor = MaterialTheme.colorScheme.onPrimary
    val textColor = MaterialTheme.colorScheme.onSecondary
    val buttonColor = MaterialTheme.colorScheme.tertiary
    val onButtonColor = MaterialTheme.colorScheme.onTertiary

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
                    style = TextStyle(
                        color = titleTextColor,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    ),
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
                    style = TextStyle(
                        color = textColor,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal
                    ),
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
                    style = TextStyle(
                        color = textColor,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    ),
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
    WarningDialog(
        title = "Enable Notifications",
        description = "Notifications are important for our app. Please enable them in settings.",
        actionButtonTitle = "Open Settings",
        onClick = {},
        onDismiss = {}
    )
}

@Preview
@PreviewLightDark
@Composable
fun WarningDialogPreview() {
    WarningDialog(
        title = "Delete Workout",
        description = "Are you sure you want to delete this workout? This action cannot be undone.",
        actionButtonTitle = "Delete",
        onClick = {},
        onDismiss = {}
    )
}

@Preview
@PreviewLightDark
@Composable
fun SelectWorkoutDialogPreview() {
    val workout = MockupDataGeneratorV2.generateWorkout()
    val selectWord = if (workout.isSelected) "De-select" else "Select"

    WarningDialog(
        title = "$selectWord Workout",
        description = "Are you sure you want to ${selectWord.lowercase(Locale.getDefault())} this workout?",
        actionButtonTitle = selectWord,
        onClick = {},
        onDismiss = {}
    )
}

@Preview
@PreviewLightDark
@Composable
fun DeselectWorkoutDialogPreview() {
    val workout = MockupDataGeneratorV2.generateWorkout().copy(isSelected = true)
    val selectWord = if (workout.isSelected) "De-select" else "Select"

    WarningDialog(
        title = "$selectWord Workout",
        description = "Are you sure you want to ${selectWord.lowercase(Locale.getDefault())} this workout?",
        actionButtonTitle = selectWord,
        onClick = {},
        onDismiss = {}
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