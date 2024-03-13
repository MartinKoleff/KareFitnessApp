package com.koleff.kare_android.ui.compose.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.koleff.kare_android.common.MockupDataGenerator
import com.koleff.kare_android.data.model.dto.ExerciseSetDto

@Composable
fun WorkoutDataSheet() {
    //title

    //rows (by default 3)
    //if u fill 3rd row add 4th one unfilled

}

@Composable
fun WorkoutDataSheetRow(set: ExerciseSetDto) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        //Set number
        Text(
            modifier = Modifier.padding(4.dp),
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
            modifier = Modifier.padding(4.dp),
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
            modifier = Modifier.padding(4.dp),
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
        val isDoneState = remember { mutableStateOf(true) }
        Checkbox(
            checked = isDoneState.value,
            onCheckedChange = { isDoneState.value = it }
        )
    }
}

@Preview
@Composable
fun WorkoutDataSheetRowPreview() {
    val exerciseSet = MockupDataGenerator.generateExerciseSet()
    WorkoutDataSheetRow(exerciseSet)
}

@Preview
@Composable
fun WorkoutDataSheetPreview() {
    WorkoutDataSheet()
}