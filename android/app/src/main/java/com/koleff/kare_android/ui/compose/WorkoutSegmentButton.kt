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
fun WorkoutSegmentButton(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    selectedOptionIndex: Int,
    isBlocked: MutableState<Boolean>
) {
    var selectedIndex by remember { mutableStateOf(selectedOptionIndex) }
    val options = listOf("MyWorkout", "Workouts")

    LaunchedEffect(key1 = isBlocked.value) {
        Log.d(
            "Navigation LaunchedEffect",
            "Is navigation in progress: ${isBlocked.value}"
        )

        if (isBlocked.value) {
            blockNavigationButtons(isBlocked)
        }
    }

    SingleChoiceSegmentedButtonRow(modifier) {
        options.forEachIndexed { index, label ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                onClick = {
                    if (isBlocked.value) return@SegmentedButton

                    selectedIndex = index

                    //Navigation
                    when (selectedIndex) {
                        0 -> {
                            navController.navigate(MainScreen.MyWorkout.route).also {
                                Log.d(
                                    "Navigation",
                                    "Updated isBlocked to true"
                                )

                                isBlocked.value = true
                            }
                        }

                        1 -> {
                            navController.navigate(MainScreen.Workouts.route).also {
                                Log.d(
                                    "Navigation",
                                    "Updated isBlocked to true"
                                )

                                isBlocked.value = true
                            }
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