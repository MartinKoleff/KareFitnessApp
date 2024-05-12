package com.koleff.kare_android.ui.compose.components

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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.sp
import com.koleff.kare_android.data.model.dto.MachineType
import com.koleff.kare_android.ui.view_model.ExerciseListViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MachineFilterSegmentButton(
    modifier: Modifier = Modifier,
    selectedOptionIndex: Int = -1,
    isDisabled: Boolean,
    onFilterSelected: (MachineType) -> Unit
) {
    val labelColor = MaterialTheme.colorScheme.onSecondary
    val buttonColor = MaterialTheme.colorScheme.tertiaryContainer
    val outlineColor = MaterialTheme.colorScheme.outlineVariant

    var selectedIndex by remember { mutableStateOf(selectedOptionIndex) }
    val options = listOf("Dumbbell", "Barbell", "Machine", "Calisthenics")
    SingleChoiceSegmentedButtonRow(modifier) {
        options.forEachIndexed { index, label ->
            SegmentedButton(
                colors = SegmentedButtonDefaults.colors(  //TODO: checkmark color to be green...
                    activeContainerColor = buttonColor,
                    disabledActiveContainerColor = buttonColor,
                    activeBorderColor = outlineColor,
                    disabledInactiveBorderColor = outlineColor,
                    inactiveBorderColor = outlineColor
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
                    style = TextStyle(
                        fontSize = 12.sp,
                        color = labelColor,
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Normal
                    ),
                )
            }
        }
    }
}

@Preview
@PreviewLightDark
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
@PreviewLightDark
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
@PreviewLightDark
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
@PreviewLightDark
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