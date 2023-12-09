package com.koleff.kare_android.ui.compose

import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.koleff.kare_android.data.model.event.OnFilterEvent
import com.koleff.kare_android.ui.view_model.ExerciseViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MachineFilterSegmentButton(
    modifier: Modifier = Modifier,
    selectedOptionIndex: Int = -1,
    isDisabled: Boolean,
    exerciseListViewModel: ExerciseViewModel
) {
    var selectedIndex by remember { mutableStateOf(selectedOptionIndex) }
    val options = listOf("Dumbbell", "Barbell", "Machine")

    SingleChoiceSegmentedButtonRow(modifier) {
        options.forEachIndexed { index, label ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                onClick = {
                    //Deselect filters -> no filter
                    if (selectedIndex == index) { //TODO: load all exercises...
                        selectedIndex = -1
                        return@SegmentedButton
                    }
                    selectedIndex = index

                    //Filter
                    when (selectedIndex) {
                        0 -> {
                            exerciseListViewModel.onEvent(OnFilterEvent.DumbbellFilter)
                        }

                        1 -> {
                            exerciseListViewModel.onEvent(OnFilterEvent.BarbellFilter)
                        }

                        2 -> {
                            exerciseListViewModel.onEvent(OnFilterEvent.MachineFilter)
                        }

                        -1 -> { //Disabled filter -> show all
                            exerciseListViewModel.onEvent(OnFilterEvent.NoFilter)
                        }
                    }
                },
                selected = index == selectedIndex,
                enabled = !isDisabled
            ) {
                Text(label)
            }
        }
    }
}