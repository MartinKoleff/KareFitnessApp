package com.koleff.kare_android.ui.compose.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonColors
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.koleff.kare_android.ui.event.OnWorkoutScreenSwitchEvent
import com.koleff.kare_android.ui.view_model.WorkoutViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutSegmentButton(
    modifier: Modifier = Modifier,
    selectedOptionIndex: Int,
    isDisabled: Boolean,
    onWorkoutFilter: (OnWorkoutScreenSwitchEvent) -> Unit
) {
    val labelColor = MaterialTheme.colorScheme.onSecondary
    val buttonColor = MaterialTheme.colorScheme.tertiaryContainer
    val outlineColor = MaterialTheme.colorScheme.outlineVariant

    var selectedIndex by remember { mutableStateOf(selectedOptionIndex) }
    val options = listOf("MyWorkout", "Workouts")

    SingleChoiceSegmentedButtonRow(modifier) {
        options.forEachIndexed { index, label ->
            SegmentedButton( //TODO: checkmark color to be green...
                colors = SegmentedButtonDefaults.colors(
                    activeContainerColor = buttonColor,
                    disabledActiveContainerColor = buttonColor,
                    activeBorderColor = outlineColor,
                    disabledInactiveBorderColor = outlineColor,
                    inactiveBorderColor = outlineColor
                ),
                shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                onClick = {

                    //If the same option is selected
                    selectedIndex = if (selectedIndex == index) {
                        return@SegmentedButton
                    } else {
                        index
                    }

                    //Navigation
                    when (selectedIndex) {
                        0 -> {

                            //MyWorkout screen
                            onWorkoutFilter(OnWorkoutScreenSwitchEvent.SelectedWorkout)
                        }

                        1 -> {

                            //Workouts Screen
                            onWorkoutFilter(OnWorkoutScreenSwitchEvent.AllWorkouts)
                        }
                    }
                },
                selected = index == selectedIndex,
                enabled = !isDisabled
            ) {
                Text(text = label, color = labelColor)
            }
        }
    }
}

@Preview
@PreviewLightDark
@Composable
private fun WorkoutSegmentButtonEnabledPreview() {
    WorkoutSegmentButton(
        modifier = Modifier,
        selectedOptionIndex = 1, //Workouts screen
        isDisabled = false,
        onWorkoutFilter = {

        }
    )
}

@Preview
@PreviewLightDark
@Composable
private fun WorkoutSegmentButtonDisabledPreview() {
    WorkoutSegmentButton(
        modifier = Modifier,
        selectedOptionIndex = 1, //MyWorkout screen
        isDisabled = true,
        onWorkoutFilter = {

        }
    )
}

@Preview
@PreviewLightDark
@Composable
private fun WorkoutSegmentButtonEnabled2Preview() {
    WorkoutSegmentButton(
        modifier = Modifier,
        selectedOptionIndex = 0, //Workouts screen
        isDisabled = false,
        onWorkoutFilter = {

        }
    )
}

@Preview
@PreviewLightDark
@Composable
private fun WorkoutSegmentButtonDisabled2Preview() {
    WorkoutSegmentButton(
        modifier = Modifier,
        selectedOptionIndex = 0, //MyWorkout screen
        isDisabled = true,
        onWorkoutFilter = {

        }
    )
}