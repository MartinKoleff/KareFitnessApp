package com.koleff.kare_android.ui.compose.dialogs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.koleff.kare_android.common.MockupDataGenerator
import java.util.Locale

@Composable
fun EditWorkoutDialog(
    title: String = "Edit workout name",
    currentName: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var text by remember { mutableStateOf(currentName) }

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
                        color = Color.Black,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Normal
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
                }
            ) {
                Text(
                    text = "Confirm",
                    style = TextStyle(
                        color = Color.White,
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
            Button(onClick = { onDismiss() }) {
                Text(
                    text = "Cancel",
                    style = TextStyle(
                        color = Color.White,
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
    AlertDialog(
        title = {
            Text(
                text = title,
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Normal
                ),
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        },
        text = {
            if (description.isNotEmpty()) {
                Text(
                    text = description,
                    style = TextStyle(
                        color = Color.Black,
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
                Button(onClick = {
                    onClick()
                    onDismiss()
                }) {
                    Text(
                        text = "OK",
                        style = TextStyle(
                            color = Color.White,
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
    actionButtonTitle: String
) {
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
                        color = Color.Black,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Normal
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
                    color = Color.Black,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal
                ),
                textAlign = TextAlign.Center,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        },
        confirmButton = {
            Button(onClick = {
                onClick()
                onDismiss()
            }) {
                Text(
                    text = actionButtonTitle,
                    style = TextStyle(
                        color = Color.White,
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
            Button(onClick = onDismiss) {
                Text(
                    text = "Cancel",
                    style = TextStyle(
                        color = Color.White,
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

@Preview
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
@Composable
fun SelectWorkoutDialogPreview() {
    val workout = MockupDataGenerator.generateWorkout()
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
@Composable
fun DeselectWorkoutDialogPreview() {
    val workout = MockupDataGenerator.generateWorkout().copy(isSelected = true)
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
@Composable
fun SuccessDialogPreview() {
    SuccessDialog(
        title = "Workout added successfully",
        onDismiss = {},
        onClick = {}
    )
}

@Preview
@Composable
fun EditWorkoutNameDialogPreview() {
    val onConfirm: (String) -> Unit = {}

    EditWorkoutDialog(
        onDismiss = {},
        onConfirm = onConfirm,
        currentName = "Workout 1"
    )
}