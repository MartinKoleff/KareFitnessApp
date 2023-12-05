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
import androidx.navigation.NavHostController
import com.koleff.kare_android.data.MainScreen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutSegmentButton(modifier: Modifier = Modifier, navController: NavHostController, selectedOptionIndex: Int) {
    var selectedIndex by remember { mutableStateOf(selectedOptionIndex) }
    val options = listOf("MyWorkout", "Workouts")

    SingleChoiceSegmentedButtonRow(modifier) {
        options.forEachIndexed { index, label ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                onClick = {
                    selectedIndex = index

                    //Navigation
                    when(selectedIndex){
                        0 -> {
                            navController.navigate(MainScreen.MyWorkout.route)
                        }
                        1 -> {
                            navController.navigate(MainScreen.Workouts.route)
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