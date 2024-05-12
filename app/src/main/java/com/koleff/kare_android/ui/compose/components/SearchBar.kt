package com.koleff.kare_android.ui.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    onToggleSearch: () -> Unit,
    onSearch: (String) -> Unit
) {
    var text by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    val textColor = MaterialTheme.colorScheme.onSurface
    val tintColor = MaterialTheme.colorScheme.onSurface

    Row(modifier = modifier) {
        TextField(
            value = text,
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent, //Hide the bottom line indicator
                unfocusedIndicatorColor = Color.Transparent,
            ),
            onValueChange = {
                if ((text.isNotEmpty() && it.isEmpty()) ||
                    (text.isEmpty() && it.isNotEmpty())
                ) onToggleSearch()

                text = it

                //Search every time text value changes
                onSearch(text)
            },
            label = { Text(text = "Search", color = textColor) },
            leadingIcon = {
                Icon(
                    Icons.Filled.Search,
                    tint = tintColor,
                    contentDescription = "Search icon"
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearch(text)

                    keyboardController?.hide()
                    focusManager.clearFocus()
                },
                onDone = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                }
            ),
            shape = RoundedCornerShape(50.dp)
        )
    }
}

@Preview
@Composable
fun SearchBarPreview() {
    SearchBar(
        onToggleSearch = {},
        onSearch = {}
    )
}