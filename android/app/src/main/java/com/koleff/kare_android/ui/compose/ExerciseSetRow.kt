package com.koleff.kare_android.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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

@Composable
fun ExerciseSetRow(modifier: Modifier = Modifier, set: ExerciseSet) {

    // Initialize text field states
    val setNumber = set.number
    val repsState = remember { mutableStateOf(TextFieldValue("")) }
    val weightState = remember { mutableStateOf(TextFieldValue("")) }

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
            onValueChange = { repsState.value = it },
            textStyle = TextStyle(
                color = Color.Black,
                fontWeight = FontWeight.Bold
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            maxLines = 1,
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier.border(
                        1.dp,
                        Color.Gray,
                        RoundedCornerShape(8.dp)
                    )
                ) {
                    if (repsState.value.text.isEmpty()) {
                        Text(
                            modifier = Modifier.padding(start = 4.dp),
                            text = "Reps",
                            color = Color.Black,
                            fontWeight = FontWeight.Medium
                        )
                    }
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
            onValueChange = { weightState.value = it },
            textStyle = TextStyle(
                color = Color.Black,
                fontWeight = FontWeight.Bold
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            maxLines = 1,
            decorationBox = { innerTextField ->
                Box(modifier = Modifier.border(1.dp, Color.Gray, RoundedCornerShape(8.dp))) {
                    if (weightState.value.text.isEmpty()) {
                        Text(
                            modifier = Modifier.padding(4.dp),
                            text = "Weight",
                            color = Color.Black,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    innerTextField()
                }
            }
        )
    }
}

@Composable
fun ExerciseSetRowList(modifier: Modifier, exerciseSetList: List<ExerciseSet>) {

    val totalExerciseSets = if (exerciseSetList.size < 3) {
        3
    } else {
        exerciseSetList.size
    }

    LazyColumn(modifier = modifier) {
        items(totalExerciseSets) { currentSetId ->

            //Check if set exists in the allowed boudns
            val currentSet = if (currentSetId >= 0 && currentSetId < exerciseSetList.size) {
                exerciseSetList[currentSetId]
            } else {

                //Default set
                ExerciseSet(currentSetId + 1, 12, 0f)
            }
            ExerciseSetRow(set = currentSet)
        }
    }
}


@Preview
@Composable
fun ExerciseSetRowPreview() {
    val exerciseSet = MockupDataGenerator.generateExerciseSet()
    ExerciseSetRow(set = exerciseSet)
}

@Preview
@Composable
fun ExerciseSetRowListPreview() {
    val exerciseSetList = MockupDataGenerator.generateExerciseSetsList()
    ExerciseSetRowList(modifier = Modifier.fillMaxSize(), exerciseSetList = exerciseSetList)
}