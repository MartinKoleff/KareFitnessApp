package com.koleff.kare_android.ui.compose.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.model.dto.MachineType


@Composable
fun ExerciseList(
    modifier: Modifier = Modifier,
    exerciseList: List<ExerciseDto>,
    onClick: (ExerciseDto) -> Unit,
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(exerciseList.size) {
            val currentExercise = exerciseList[it]
            ExerciseBannerV2(
                exercise = currentExercise,
                showDifficulty = true,
                onClick = {
                    onClick(currentExercise)
                },
            )
        }
    }
}

@Composable
fun ExerciseList(
    modifier: Modifier = Modifier,
    exerciseList: List<ExerciseDto>,
    selectedExercisesList: List<ExerciseDto>,
    onClick: (ExerciseDto) -> Unit = {},
    onSelect: (ExerciseDto) -> Unit
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(exerciseList.size) { currentExerciseIndex ->
            val currentExercise = exerciseList[currentExerciseIndex]
            ExerciseBannerV2(
                exercise = currentExercise,
                showDifficulty = true,
                onClick = {
                    onClick(currentExercise)
                },
                onSelect = {
                    onSelect(currentExercise)
                },
                showSelectOption = true,
                isSelected = selectedExercisesList.map { it.exerciseId }.contains(currentExercise.exerciseId)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MachineFilterSegmentButton(
    modifier: Modifier = Modifier,
    selectedOptionIndex: Int = -1,
    isDisabled: Boolean,
    onFilterSelected: (MachineType) -> Unit
) {
    val labelColor = MaterialTheme.colorScheme.onSurface
    val buttonColor = MaterialTheme.colorScheme.tertiaryContainer
    val outlineColor = MaterialTheme.colorScheme.outlineVariant

    val labelTextStyle = MaterialTheme.typography.labelSmall.copy(
        color = labelColor
    )

    var selectedIndex by remember { mutableStateOf(selectedOptionIndex) }
    val options = listOf("Dumbbell", "Barbell", "Machine", "Calisthenics")
    SingleChoiceSegmentedButtonRow(modifier) {
        options.forEachIndexed { index, label ->
            SegmentedButton(
                modifier = if(index == 3) Modifier.weight(4f) else Modifier.weight(3f),
                colors = SegmentedButtonDefaults.colors(  //TODO: checkmark color to be green...
                    activeContainerColor = buttonColor,
                    disabledActiveContainerColor = buttonColor,
                    activeBorderColor = outlineColor,
                    disabledInactiveBorderColor = outlineColor,
                    inactiveBorderColor = outlineColor,
                    activeContentColor = labelColor,
                    inactiveContentColor = MaterialTheme.colorScheme.onSurface,
                    inactiveContainerColor = MaterialTheme.colorScheme.surfaceContainer
                ),
                shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                onClick = {
                    selectedIndex = if (selectedIndex == index) {
                        -1 //Deselect filters -> no filter
                    } else {
                        index
                    }

                    //Filter
                    when (selectedIndex) {
                        0 -> {
                            onFilterSelected(MachineType.DUMBBELL)
                        }

                        1 -> {
                            onFilterSelected(MachineType.BARBELL)
                        }

                        2 -> {
                            onFilterSelected(MachineType.MACHINE)
                        }

                        3 -> {
                            onFilterSelected(MachineType.CALISTHENICS)
                        }

                        -1 -> { //Disabled filter -> show all
                            onFilterSelected(MachineType.NONE)
                        }
                    }
                },
                selected = index == selectedIndex,
                enabled = !isDisabled
            ) {
                Text(
                    text = label,
                    style = labelTextStyle,
                )
            }
        }
    }
}

@Preview
@Preview(name = "NEXUS_5", device = Devices.NEXUS_5)
@Composable
private fun MachineFilterSegmentEnabledPreview() {
    MachineFilterSegmentButton(
        modifier = Modifier,
        selectedOptionIndex = 1, //Barbell
        isDisabled = false,
        onFilterSelected = {

        }
    )
}

@Preview
@Composable
private fun MachineFilterSegmentDisabledPreview() {
    MachineFilterSegmentButton(
        modifier = Modifier,
        selectedOptionIndex = 1, //Barbell
        isDisabled = true,
        onFilterSelected = {

        }
    )
}

@Preview
@Composable
private fun MachineFilterSegmentEnabled2Preview() {
    MachineFilterSegmentButton(
        modifier = Modifier,
        selectedOptionIndex = 1, //Barbell
        isDisabled = false,
        onFilterSelected = {

        }
    )
}

@Preview
@Composable
private fun MachineFilterSegmentDisabled2Preview() {
    MachineFilterSegmentButton(
        modifier = Modifier,
        selectedOptionIndex = 0, //Dumbbell
        isDisabled = true,
        onFilterSelected = {

        }
    )
}