package com.koleff.kare_android.ui.compose.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.koleff.kare_android.R
import com.koleff.kare_android.common.MockupDataGenerator
import com.koleff.kare_android.data.model.dto.ExerciseSetDto

@Composable
fun ExerciseSetRow(
    modifier: Modifier = Modifier,
    set: ExerciseSetDto,
    onRepsChanged: (Int) -> Unit,
    onWeightChanged: (Float) -> Unit,
    onDelete: (ExerciseSetDto) -> Unit
) {
    val setNumber = set.number

    val cornerSize = 16.dp
    val backgroundColor = Color.Black
    val textColor = Color.White
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(horizontal = 16.dp, vertical = 4.dp)
                .background(color = backgroundColor, shape = RoundedCornerShape(cornerSize))
                .border(2.dp, Color.Gray, RoundedCornerShape(cornerSize)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 8.dp),
                text = "Set $setNumber",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )

            RepsTextField(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp),
                reps = set.reps,
                onRepsChanged = onRepsChanged
            )
            Spacer(modifier = Modifier.width(8.dp))

            WeightTextField(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp),
                weight = set.weight,
                onWeightChanged = onWeightChanged
            )
        }

        //Delete footer
        ExerciseSetRowFooter(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp),
            onDelete = { onDelete(set) },
            setToDelete = set
        )
    }
}

@Composable
fun ExerciseSetRowFooter(
    modifier: Modifier = Modifier,
    onDelete: (ExerciseSetDto) -> Unit,
    setToDelete: ExerciseSetDto
) {
    val dividerColor = Color.White
    val imageSize = 30.dp
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(imageSize),
        contentAlignment = Alignment.Center
    ) {
        HorizontalDivider(thickness = 2.dp, color = dividerColor)

        Box(
            modifier = Modifier
                .size(imageSize)
                .clickable { onDelete(setToDelete) }
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_delete_2),
                contentDescription = "Delete exercise set",
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
fun WeightTextField(
    modifier: Modifier,
    weight: Float,
    onWeightChanged: (Float) -> Unit
) {
    val weightState = remember {
        mutableStateOf(weight.toString())
    }

    ExerciseSetTextField(
        modifier = modifier,
        state = weightState.value,
        onTextChange = {
            weightState.value = it
            onWeightChanged(
                it.toFloatOrNull() ?: weight
            ) //Update the parent with the new value or retain the old value if null
        },
        label = "Weight"
    )
}

@Composable
fun RepsTextField(
    modifier: Modifier,
    reps: Int,
    onRepsChanged: (Int) -> Unit
) {
    val repsState = remember {
        mutableStateOf(reps.toString())
    }

    ExerciseSetTextField(
        modifier = modifier,
        state = repsState.value,
        onTextChange = {
            repsState.value = it
            onRepsChanged(
                it.toIntOrNull() ?: reps
            ) //Update the parent with the new value or retain the old value if null
        },
        label = "Reps"
    )
}

@Composable
fun ExerciseSetTextField(
    modifier: Modifier = Modifier,
    label: String,
    state: String,
    onTextChange: (String) -> Unit
) {

    //For dark theme...
    val textColor = Color.White
    val backgroundColor = Color.DarkGray
    val outlineColor = Color.Gray
    val cornerSize = 16.dp
    BasicTextField(
        modifier = modifier,
        value = state,
        onValueChange = { text ->
            onTextChange(text)
        },
        textStyle = TextStyle(
            color = textColor,
            fontWeight = FontWeight.Bold
        ),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        maxLines = 1,
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(cornerSize))
                    .border(
                        1.dp,
                        outlineColor,
                        RoundedCornerShape(cornerSize)
                    )
                    .background(backgroundColor),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 6.dp),
                    text = label,
                    color = textColor,
                    fontWeight = FontWeight.Medium,
                )
                innerTextField()
            }
        })
}

@Preview
@Composable
fun ExerciseSetRowFooterPreview() {
    val onDelete: (ExerciseSetDto) -> Unit = {

    }
    ExerciseSetRowFooter(
        setToDelete = MockupDataGenerator.generateExerciseSet(),
        onDelete = onDelete
    )
}

@Preview
@Composable
fun ExerciseSetRepsTextFieldPreview() {
    Box(
        modifier = Modifier
            .size(500.dp)
            .background(Color.White)
    ) {
        RepsTextField(
            modifier = Modifier
                .height(50.dp)
                .width(200.dp),
            reps = 12
        ) {

        }
    }
}

@Preview
@Composable
fun ExerciseSetWeightTextFieldPreview() {
    Box(
        modifier = Modifier
            .size(500.dp)
            .background(Color.White)
    ) {
        WeightTextField(
            modifier = Modifier
                .height(50.dp)
                .width(200.dp),
            weight = 50.0f
        ) {

        }
    }
}

@Composable
fun AddNewSetFooter(onAddNewSetAction: () -> Unit) {
    val height = 50.dp
    val cornerSize = 24.dp
    val color = Color.Black
    val strokeColor = Color.White

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .padding(horizontal = 32.dp)
            .clip(RoundedCornerShape(cornerSize))
            .border(
                border = BorderStroke(1.dp, color = strokeColor),
                shape = RoundedCornerShape(cornerSize)
            )
            .background(
                color = color,
                shape = RoundedCornerShape(cornerSize)
            )
            .clickable { onAddNewSetAction() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(R.drawable.ic_vector_add_green),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(height)
                .graphicsLayer { alpha = 0.80f }
        )

        Text(
            modifier = Modifier
                .padding(horizontal = 16.dp)
            ,
            text = "Add new set",
            style = TextStyle(
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            ),
            textAlign = TextAlign.Start,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview
@Composable
fun AddNewSetFooterPreview() {
    AddNewSetFooter() {

    }
}

@Preview
@Composable
fun ExerciseSetRowPreview() {
    val exerciseSet = MockupDataGenerator.generateExerciseSet()
    ExerciseSetRow(
        set = exerciseSet,
        onWeightChanged = {},
        onRepsChanged = {},
        onDelete = {}
    )
}


//@Composable
//fun ExerciseSetRowList(
//    modifier: Modifier,
//    exerciseSetList: List<ExerciseSetDto>,
//    onRepsChanged: (Int) -> Unit, // Callback when reps are updated
//    onWeightChanged: (Float) -> Unit // Callback when weight is updated
//) {
//
//    val totalExerciseSets = if (exerciseSetList.size < 3) {
//        3
//    } else {
//        exerciseSetList.size
//    }
//
//    LazyColumn(modifier = modifier) {
//        items(totalExerciseSets) { currentSetId ->
//
//            //Check if set exists in the allowed bounds
//            val currentSet = if (currentSetId >= 0 && currentSetId < exerciseSetList.size) {
//                exerciseSetList[currentSetId]
//            } else {
//
//                //Default set
//                ExerciseSetDto(UUID.randomUUID(), currentSetId + 1, 12, 0f)
//            }
//            ExerciseSetRow(
//                set = currentSet,
//                onRepsChanged = onRepsChanged,
//                onWeightChanged = onWeightChanged,
//                onDelete = onDelete
//            )
//        }
//    }
//}

//@Preview
//@Composable
//fun ExerciseSetRowListPreview() {
//    val exerciseSetList = MockupDataGenerator.generateExerciseSetsList()
//    ExerciseSetRowList(
//        modifier = Modifier.fillMaxSize(),
//        exerciseSetList = exerciseSetList,
//        onWeightChanged = {},
//        onRepsChanged = {})
//}

