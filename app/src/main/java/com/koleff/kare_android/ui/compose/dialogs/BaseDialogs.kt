package com.koleff.kare_android.ui.compose.dialogs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties

@Composable
fun WarningDialog(
    title: String,
    description: String,
    onDismiss: () -> Unit,
    onClick: () -> Unit,
    positiveButtonTitle: String,
    negativeButtonTitle: String = "Cancel",
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
                    text = positiveButtonTitle,
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
                    text = negativeButtonTitle,
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