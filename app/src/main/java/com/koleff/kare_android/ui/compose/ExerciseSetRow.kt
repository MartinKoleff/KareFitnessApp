package com.koleff.kare_android.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.koleff.kare_android.common.MockupDataGenerator
import com.koleff.kare_android.data.model.dto.ExerciseSet
import java.util.UUID

@Composable
fun ExerciseSetRow(
    modifier: Modifier = Modifier,
    set: ExerciseSet,
    onRepsChanged: (Int) -> Unit, // Callback when reps are updated
    onWeightChanged: (Float) -> Unit // Callback when weight is updated
) {

    // Initialize text field states
    val setNumber = set.number

    var reps = set.reps
    var weight = set.weight
    val repsState = remember { mutableStateOf(reps.toString()) }
    val weightState = remember { mutableStateOf(weight.toString()) }

    Row(
        modifier = Modifier
            .padding(16.dp)
            .background(color = Color.White, shape = RoundedCornerShape(8.dp))
            .border(2.dp, Color.Gray, RoundedCornerShape(8.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.padding(end = 8.dp),
            text = "Set $setNumber",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        BasicTextField(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 4.dp),
            value = repsState.value,
            onValueChange = {
                repsState.value = it
                onRepsChanged(it.toIntOrNull() ?: set.reps) // Update the parent with the new value or retain the old value if null
            },
            textStyle = TextStyle(
                color = Color.Black,
                fontWeight = FontWeight.Bold
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            maxLines = 1,
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier.border(
                        1.dp,
                        Color.Gray,
                        RoundedCornerShape(8.dp)
                    ),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 6.dp),
                        text = "Reps",
                        color = Color.Black,
                        fontWeight = FontWeight.Medium
                    )

//                    Text(
//                        modifier = Modifier.padding(end = 8.dp),
//                        text = set.reps.toString(),
//                        color = Color.Black,
//                        fontWeight = FontWeight.Medium,
//                        maxLines = 1
//                    )

//                    reps = if (repsState.value.isEmpty()) {
//                        repsState.value.toInt()
//                    } else set.reps

                    reps = set.reps

                    innerTextField()
                }
            }
        )

        Spacer(modifier = Modifier.width(8.dp))

        BasicTextField(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 4.dp),
            value = weightState.value,
            onValueChange = {
                weightState.value = it
                onWeightChanged(it.toFloatOrNull() ?: set.weight) // Update the parent with the new value or retain the old value if null
            },
            textStyle = TextStyle(
                color = Color.Black,
                fontWeight = FontWeight.Bold
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            maxLines = 1,
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier.border(
                        1.dp,
                        Color.Gray,
                        RoundedCornerShape(8.dp)
                    ),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 6.dp),
                        text = "Weight",
                        color = Color.Black,
                        fontWeight = FontWeight.Medium,
                    )

//                    Text(
//                        modifier = Modifier.padding(end = 8.dp),
//                        text = set.weight.toString(),
//                        color = Color.Black,
//                        fontWeight = FontWeight.Medium,
//                        maxLines = 1
//                    )


//                    weight = if (weightState.value.isEmpty()) {
//                        weightState.value.toFloat()
//                    } else set.weight

                    weight = set.weight

                    innerTextField()
                }
            }
        )
    }
}

@Composable
fun ExerciseSetRowList(
    modifier: Modifier,
    exerciseSetList: List<ExerciseSet>,
    onRepsChanged: (Int) -> Unit, // Callback when reps are updated
    onWeightChanged: (Float) -> Unit // Callback when weight is updated
) {

    val totalExerciseSets = if (exerciseSetList.size < 3) {
        3
    } else {
        exerciseSetList.size
    }

    LazyColumn(modifier = modifier) {
        items(totalExerciseSets) { currentSetId ->

            //Check if set exists in the allowed bounds
            val currentSet = if (currentSetId >= 0 && currentSetId < exerciseSetList.size) {
                exerciseSetList[currentSetId]
            } else {

                //Default set
                ExerciseSet(UUID.randomUUID(), currentSetId + 1, 12, 0f)
            }
            ExerciseSetRow(
                set = currentSet,
                onRepsChanged = onRepsChanged,
                onWeightChanged = onWeightChanged
            )
        }
    }
}


@Preview
@Composable
fun ExerciseSetRowPreview() {
    val exerciseSet = MockupDataGenerator.generateExerciseSet()
    ExerciseSetRow(
        set = exerciseSet,
        onWeightChanged = {},
        onRepsChanged = {}
    )
}

@Preview
@Composable
fun ExerciseSetRowListPreview() {
    val exerciseSetList = MockupDataGenerator.generateExerciseSetsList()
    ExerciseSetRowList(
        modifier = Modifier.fillMaxSize(),
        exerciseSetList = exerciseSetList,
        onWeightChanged = {},
        onRepsChanged = {})
}