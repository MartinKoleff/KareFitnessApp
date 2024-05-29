package com.koleff.kare_android.ui.compose.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun LoadingDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
) {
    val loadingWheelSize = 100.dp

    val backgroundColor = MaterialTheme.colorScheme.surface

    val cornerSize = 16.dp

    Dialog(
        onDismissRequest = onDismiss,
        DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    ) {
        Box(
            contentAlignment= Alignment.Center,
            modifier = Modifier
                .size(loadingWheelSize)
                .background(
                    color = backgroundColor,
                    shape = RoundedCornerShape(cornerSize)
                )
        ) {
            CircularProgressIndicator()
        }
    }
}

@Preview
@Composable
private fun LoadingDialogPreview() {
    LoadingDialog(
        onDismiss = {}
    )
}