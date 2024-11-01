package com.koleff.kare_android.ui.compose.dialogs

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.koleff.kare_android.data.model.dto.KareLanguage

@Composable
fun ChangeLanguageDialog(
    selectedLanguage: KareLanguage,
    onClick: () -> Unit,
    onDismiss: () -> Unit
) {
    WarningDialog(
        title = "Change Language",
        description = "Are you sure you want the language to ${selectedLanguage.name}",
        positiveButtonTitle = "Confirm",
        onClick = onClick,
        onDismiss = onDismiss
    )
}

@Preview
@PreviewLightDark
@Composable
fun ChangeLanguageDialogPreview() {
    ChangeLanguageDialog(
        KareLanguage("Bulgarian"),
        onClick = {

        },
        onDismiss = {

        }
    )
}