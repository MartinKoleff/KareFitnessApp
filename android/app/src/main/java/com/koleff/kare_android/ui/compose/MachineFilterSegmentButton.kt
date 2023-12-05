package com.koleff.kare_android.ui.compose

import android.util.Log
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.koleff.kare_android.data.MainScreen
import com.koleff.kare_android.ui.compose.navigation.blockNavigationButtons


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MachineFilterSegmentButton(
    modifier: Modifier = Modifier,
    selectedOptionIndex: Int = -1
) {
    var selectedIndex by remember { mutableStateOf(selectedOptionIndex) }
    val options = listOf("Dumbbell", "Barbell", "Machine")

    SingleChoiceSegmentedButtonRow(modifier) {
        options.forEachIndexed { index, label ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                onClick = {
                    //Deselect filters -> no filter
                    if (selectedIndex == index) {
                        selectedIndex = -1
                        //TODO: load all exercises...
                        return@SegmentedButton
                    }
                    selectedIndex = index

                    //Filter
                    when (selectedIndex) {
                        0 -> {

                        }

                        1 -> {

                        }

                        2 -> {

                        }
                    }
                },
                selected = index == selectedIndex
            ) {
                Text(label)
            }
        }
    }
}