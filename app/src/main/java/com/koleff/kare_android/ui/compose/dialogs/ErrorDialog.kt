package com.koleff.kare_android.ui.compose.dialogs

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.koleff.kare_android.R
import com.koleff.kare_android.data.model.response.base_response.KareError

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