package com.koleff.kare_android.ui.compose.dialogs

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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
    var cooldownHours by remember { mutableStateOf(workoutConfiguration.cooldownTime.hours) }
    var cooldownMinutes by remember { mutableStateOf(workoutConfiguration.cooldownTime.minutes) }
    var cooldownSeconds by remember { mutableStateOf(workoutConfiguration.cooldownTime.seconds) }

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
                Text(
                    "Configure Your Workout",
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(Modifier.height(16.dp))
                Text(
                    "Cooldown Time",
                    style = MaterialTheme.typography.bodyLarge
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
                        value = cooldownMinutes.toString(),
                        onValueChange = {
                            cooldownMinutes = validateUserInput(it)
                        },
                        label = { Text("Minutes") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = cooldownSeconds.toString(),
                        onValueChange = { cooldownSeconds = validateUserInput(it) },
                        label = { Text("Seconds") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(Modifier.height(16.dp))
                Button(
                    onClick = {
                        workoutConfiguration.cooldownTime =
                            ExerciseTime(cooldownHours, cooldownMinutes, cooldownSeconds)
                        onSave(workoutConfiguration)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save Configuration")
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