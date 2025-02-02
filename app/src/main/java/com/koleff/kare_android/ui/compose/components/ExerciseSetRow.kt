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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.koleff.kare_android.R
import com.koleff.kare_android.common.MockupDataGeneratorV2
import com.koleff.kare_android.data.model.dto.ExerciseSetDto
import kotlin.random.Random

@Composable
fun ExerciseSetRow(
    modifier: Modifier = Modifier,
    set: ExerciseSetDto,
    onRepsChanged: (Int) -> Unit,
    onWeightChanged: (Float) -> Unit,
    onDelete: (ExerciseSetDto) -> Unit
) {
    //Keyboard
    val repsFocusRequester = remember { FocusRequester() }
    val weightFocusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    val setNumber = set.number

    val cornerSize = 16.dp
    val backgroundColor = MaterialTheme.colorScheme.tertiary
    val textColor = MaterialTheme.colorScheme.onSurface
    val outlineColor = MaterialTheme.colorScheme.outlineVariant

    val textStyle = MaterialTheme.typography.titleMedium.copy(
        color = textColor
    )
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(horizontal = 16.dp, vertical = 4.dp)
                .background(
                    color = backgroundColor,
                    shape = RoundedCornerShape(cornerSize)
                )
                .border(
                    border = BorderStroke(1.5.dp, outlineColor),
                    shape = RoundedCornerShape(cornerSize)
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 8.dp),
                text = "Set $setNumber",
                style = textStyle,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            RepsTextField(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp)
                    .focusRequester(repsFocusRequester),
                reps = set.reps,
                onRepsChanged = onRepsChanged,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        weightFocusRequester.requestFocus()
                    }
                ),
            )
            Spacer(modifier = Modifier.width(8.dp))

            WeightTextField(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp)
                    .focusRequester(weightFocusRequester),
                weight = set.weight,
                onWeightChanged = onWeightChanged,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    }
                )
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
    val dividerColor = MaterialTheme.colorScheme.outlineVariant
    val imageSize = 30.dp
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(imageSize),
        contentAlignment = Alignment.Center
    ) {
        HorizontalDivider(thickness = 3.dp, color = dividerColor)

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
    onWeightChanged: (Float) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
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
    onRepsChanged: (Int) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
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
        label = "Reps",
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions
    )
}

@Composable
fun ExerciseSetTextField(
    modifier: Modifier = Modifier,
    label: String,
    state: String,
    onTextChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {

    //For dark theme...
    val textColor = MaterialTheme.colorScheme.onSurface
    val backgroundColor = MaterialTheme.colorScheme.tertiary
    val outlineColor = MaterialTheme.colorScheme.outlineVariant

    val textStyle = MaterialTheme.typography.bodyLarge.copy(
        color = textColor
    )
    val cornerSize = 16.dp
    BasicTextField(
        modifier = modifier,
        value = state,
        onValueChange = { text ->
            onTextChange(text)
        },
        textStyle = textStyle,
        maxLines = 1,
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(cornerSize))
                    .border(
                        border = BorderStroke(1.dp, outlineColor),
                        shape = RoundedCornerShape(cornerSize)
                    )
                    .background(backgroundColor),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 6.dp),
                    text = label,
                    style = textStyle
                )
                innerTextField()
            }
        },
        keyboardOptions = keyboardOptions.copy(keyboardType = KeyboardType.Decimal),
        keyboardActions = keyboardActions
    )
}

@Preview
@Composable
fun ExerciseSetRowFooterPreview() {
    val onDelete: (ExerciseSetDto) -> Unit = {

    }
    val exerciseSet = MockupDataGeneratorV2.generateExerciseSet(
        workoutId = Random.nextInt(1, 100),
        exerciseId = Random.nextInt(1, 100)
    )
    ExerciseSetRowFooter(
        setToDelete = exerciseSet,
        onDelete = onDelete
    )
}

@Preview
@Composable
fun ExerciseSetRepsTextFieldPreview() {
    Box(
        modifier = Modifier
            .size(500.dp)
            .background(MaterialTheme.colorScheme.surface)
    ) {
        RepsTextField(
            modifier = Modifier
                .height(50.dp)
                .width(200.dp),
            reps = 12,
            onRepsChanged = {}
        )
    }
}

@Preview
@Composable
fun ExerciseSetWeightTextFieldPreview() {
    Box(
        modifier = Modifier
            .size(500.dp)
            .background(MaterialTheme.colorScheme.surface)
    ) {
        WeightTextField(
            modifier = Modifier
                .height(50.dp)
                .width(200.dp),
            weight = 50.0f,
            onWeightChanged = {}
        )
    }
}

@Composable
fun AddNewSetFooter(onAddNewSetAction: () -> Unit) {
    val height = 50.dp
    val cornerSize = 24.dp
    val backgroundColor = MaterialTheme.colorScheme.tertiary
    val textColor = MaterialTheme.colorScheme.onSurface
    val outlineColor = MaterialTheme.colorScheme.outlineVariant

    val textStyle = MaterialTheme.typography.titleLarge.copy(
        color = textColor
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .padding(horizontal = 32.dp)
            .clip(RoundedCornerShape(cornerSize))
            .border(
                border = BorderStroke(2.dp, color = outlineColor),
                shape = RoundedCornerShape(cornerSize)
            )
            .background(
                color = backgroundColor,
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
                .padding(horizontal = 16.dp),
            text = "Add new set",
            style = textStyle,
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
    val exerciseSet = MockupDataGeneratorV2.generateExerciseSet(
        workoutId = Random.nextInt(1, 100),
        exerciseId = Random.nextInt(1, 100)
    )
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

