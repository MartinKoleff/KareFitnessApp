package com.koleff.kare_android.ui.compose.dialogs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties

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