package com.koleff.kare_android.ui.compose.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.koleff.kare_android.common.MockupDataGenerator
import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.model.dto.ExerciseSetDto
import com.koleff.kare_android.ui.compose.screen.HorizontalLineWithText

@Composable
fun ExerciseDataSheet(exercise: ExerciseDto) { //workoutDetails: WorkoutDetailsDto
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    Column(modifier = Modifier.fillMaxSize()) {
        HorizontalLineWithText("Exercise data sheet")

        //title
        ExerciseDataSheetTitleRow()

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(screenHeight / 4)
        ) {

            items(exercise.sets.size) { setId ->
                ExerciseDataSheetRow(set = exercise.sets[setId])
            }
        }
    }
}

@Composable
fun ExerciseDataSheetTitleRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(35.dp)
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        //Set number
        Text(
            modifier = Modifier.padding(4.dp).weight(0.5f),
            text = "Set",
            style = TextStyle(
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            ),
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        //Reps
        Text(
            modifier = Modifier.padding(4.dp).weight(1.5f),
            text = "Reps",
            style = TextStyle(
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            ),
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        //Weight
        Text(
            modifier = Modifier.padding(4.dp).weight(1.5f),
            text = "Weight",
            style = TextStyle(
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            ),
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        //Is done checkbox
        Text(
            modifier = Modifier.padding(4.dp).weight(1f),
            text = "Done",
            style = TextStyle(
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            ),
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun ExerciseDataSheetRow(set: ExerciseSetDto) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(35.dp)
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        //Set number
        Text(
            modifier = Modifier.padding(4.dp).weight(0.5f),
            text = set.number.toString(),
            style = TextStyle(
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            ),
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        //Reps
        Text(
            modifier = Modifier.padding(4.dp).weight(1.5f),
            text = set.reps.toString(),
            style = TextStyle(
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            ),
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        //Weight
        Text(
            modifier = Modifier.padding(4.dp).weight(1.5f),
            text = set.weight.toString(),
            style = TextStyle(
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            ),
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        //Checkbox
        val isDoneState = remember { mutableStateOf(false) }
        Checkbox(
            modifier = Modifier.weight(1f),
            checked = isDoneState.value,
            onCheckedChange = { isDoneState.value = it }
        )
    }
}

@Preview
@Composable
fun ExerciseDataSheetRowPreview() {
    val exerciseSet = MockupDataGenerator.generateExerciseSet()
    ExerciseDataSheetRow(exerciseSet)
}

@Preview
@Composable
fun ExerciseDataSheetPreview() {
    val exercise = MockupDataGenerator.generateExercise()
    ExerciseDataSheet(exercise)
}