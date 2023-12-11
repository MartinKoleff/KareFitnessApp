package com.koleff.kare_android.ui.compose

import androidx.compose.foundation.shape.CornerBasedShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
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
    val options = listOf("Dumbbell", "Barbell", "Machine", "Calisthenics")

    SingleChoiceSegmentedButtonRow(modifier) {
        options.forEachIndexed { index, label ->
            SegmentedButton(
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
                            exerciseListViewModel.onEvent(OnFilterEvent.DumbbellFilter)
                        }

                        1 -> {
                            exerciseListViewModel.onEvent(OnFilterEvent.BarbellFilter)
                        }

                        2 -> {
                            exerciseListViewModel.onEvent(OnFilterEvent.MachineFilter)
                        }

                        3 -> {
                            exerciseListViewModel.onEvent(OnFilterEvent.CalisthenicsFilter)
                        }

                        -1 -> { //Disabled filter -> show all
                            exerciseListViewModel.onEvent(OnFilterEvent.NoFilter)
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
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Normal
                    ),
                )
            }
        }
    }
}