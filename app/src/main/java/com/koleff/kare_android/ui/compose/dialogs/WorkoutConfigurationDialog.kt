@file:OptIn(ExperimentalMaterial3Api::class)

package com.koleff.kare_android.ui.compose.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.koleff.kare_android.data.model.dto.ExerciseTime
import com.koleff.kare_android.data.model.dto.WorkoutConfigurationDto

@Composable
fun WorkoutConfigurationDialog(
    workoutConfiguration: WorkoutConfigurationDto,
    onSave: (WorkoutConfigurationDto) -> Unit,
    onDismiss: () -> Unit,
) {

    //Keyboard
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    var cooldownHours by remember { mutableStateOf(workoutConfiguration.cooldownTime.hours) }
    var cooldownMinutes by remember { mutableStateOf(workoutConfiguration.cooldownTime.minutes) }
    var cooldownSeconds by remember { mutableStateOf(workoutConfiguration.cooldownTime.seconds) }

    val outlineColor = MaterialTheme.colorScheme.outlineVariant
    val buttonColor = MaterialTheme.colorScheme.tertiary
    val onButtonColor = MaterialTheme.colorScheme.onTertiary

    val titleTextColor = MaterialTheme.colorScheme.onSurface
    val titleTextStyle = MaterialTheme.typography.headlineMedium.copy(
        color = titleTextColor
    )

    val subtitleTextStyle = MaterialTheme.typography.titleMedium.copy(
        color = titleTextColor
    )

    val textColor = MaterialTheme.colorScheme.onSurface
    val textStyle = MaterialTheme.typography.bodyLarge.copy(
        color = textColor
    )

    val buttonTextStyle = MaterialTheme.typography.titleSmall.copy(
        color = textColor
    )

    Dialog(
        onDismissRequest = { onDismiss() }
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                //Title
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Configure Your Workout",
                        color = titleTextColor,
                        style = titleTextStyle
                    )
                }

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "Cooldown Time",
                    color = textColor,
                    style = subtitleTextStyle
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
//                    OutlinedTextField(
//                        value = cooldownHours.toString(),
//                        onValueChange = { cooldownHours = it.toIntOrNull() ?: 0 },
//                        label = { Text("Hours") },
//                        singleLine = true,
//                        modifier = Modifier.weight(1f)
//                    )
                    OutlinedTextField(
                        modifier = Modifier.weight(1f),
                        value = cooldownMinutes.toString(),
                        onValueChange = {
                            cooldownMinutes = validateUserInput(it)
                        },
                        label = {
                            Text(
                                text = "Minutes",
                                style = textStyle
                            )
                        },
                        textStyle = textStyle,
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = outlineColor,
                            unfocusedBorderColor = outlineColor
                        )
                    )
                    OutlinedTextField(
                        modifier = Modifier.weight(1f),
                        value = cooldownSeconds.toString(),
                        onValueChange = { cooldownSeconds = validateUserInput(it) },
                        label = {
                            Text(
                                text = "Seconds",
                                style = textStyle
                            )
                        },
                        textStyle = textStyle,
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = outlineColor,
                            unfocusedBorderColor = outlineColor
                        ),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Send
                        ),
                        keyboardActions = KeyboardActions(
                            onSend = {
                                keyboardController?.hide()
                                focusManager.clearFocus()

                                onSave(workoutConfiguration)
                            }
                        )
                    )
                }

                Spacer(Modifier.height(16.dp))
                Button(
                    onClick = {
                        workoutConfiguration.cooldownTime =
                            ExerciseTime(cooldownHours, cooldownMinutes, cooldownSeconds)
                        onSave(workoutConfiguration)
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = buttonColor,
                        contentColor = onButtonColor
                    )
                ) {
                    Text(text = "Save Configuration", style = buttonTextStyle)
                }
            }
        }
    }
}

fun validateUserInput(userInput: String): Int {
    val data = userInput.toIntOrNull() ?: 0

    return if (data > 60) 60 else data
}

@Preview
@PreviewLightDark
@Composable
private fun WorkoutConfigurationDialogPreview() {
    val workoutConfiguration = WorkoutConfigurationDto()
    val onSave: (WorkoutConfigurationDto) -> Unit = {}
    val onCancel = {}
    WorkoutConfigurationDialog(
        workoutConfiguration = workoutConfiguration,
        onSave = onSave,
        onDismiss = onCancel
    )
}