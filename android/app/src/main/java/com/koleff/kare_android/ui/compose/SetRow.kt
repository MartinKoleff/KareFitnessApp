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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.koleff.kare_android.common.MockupDataGenerator
import com.koleff.kare_android.data.model.dto.ExerciseSet

@Composable
fun SetRow(modifier: Modifier = Modifier, set: ExerciseSet) {

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
            text = "Set Number: $setNumber",
            fontSize = 16.sp,
            modifier = Modifier.padding(end = 8.dp)
        )

        BasicTextField(
            value = repsState.value,
            onValueChange = { repsState.value = it },
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 4.dp),
            decorationBox = { innerTextField ->
                Box(modifier = Modifier.border(1.dp, Color.Gray, RoundedCornerShape(8.dp))) {
                    if (repsState.value.text.isEmpty()) {
                        Text("Reps", color = Color.Gray)
                    }
                    innerTextField()
                }
            }
        )

        Spacer(modifier = Modifier.width(8.dp))

        BasicTextField(
            value = weightState.value,
            onValueChange = { weightState.value = it },
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 4.dp),
            decorationBox = { innerTextField ->
                Box(modifier = Modifier.border(1.dp, Color.Gray, RoundedCornerShape(8.dp))) {
                    if (weightState.value.text.isEmpty()) {
                        Text("Weight", color = Color.Gray)
                    }
                    innerTextField()
                }
            }
        )
    }
}

@Composable
fun SetRowList(modifier: Modifier, exerciseSetList: List<ExerciseSet>) {
    LazyColumn(modifier = modifier) {
        items(exerciseSetList.size){ currentSetId ->
            val currentSet = exerciseSetList[currentSetId]
            SetRow(set = currentSet)
        }
    }
}


@Preview
@Composable
fun SetRowPreview() {
    val exerciseSet = MockupDataGenerator.generateExerciseSet()
    SetRow(set = exerciseSet)
}

@Preview
@Composable
fun SetRowListPreview() {
    val exerciseSetList = MockupDataGenerator.generateExerciseSetsList()
    SetRowList(modifier = Modifier.fillMaxSize(), exerciseSetList = exerciseSetList)
}