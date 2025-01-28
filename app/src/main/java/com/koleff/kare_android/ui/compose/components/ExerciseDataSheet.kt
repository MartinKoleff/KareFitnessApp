package com.koleff.kare_android.ui.compose.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.koleff.kare_android.common.MockupDataGeneratorV2
import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.model.dto.ExerciseProgressDto
import com.koleff.kare_android.data.model.dto.ExerciseSetDto
import com.koleff.kare_android.data.model.dto.ExerciseSetProgressDto
import kotlin.random.Random

@Composable
fun ExerciseDataSheet(
    exercise: ExerciseDto,
    onExerciseDataChange: (ExerciseProgressDto) -> Unit
) {
    var sets by remember {
        mutableStateOf(
            exercise.sets.map {
                ExerciseSetProgressDto(
                    baseSet = it,
                    isDone = false
                )
            }
        )
    }

    //When a set is changed, update the list of sets.
    fun handleSetChange(updatedSet: ExerciseSetProgressDto) {

        //Updates
        sets = sets.map {
            if (it.baseSet.setId == updatedSet.baseSet.setId) updatedSet else it
        }

        //Callback
        onExerciseDataChange(
            ExerciseProgressDto(
                exerciseId = exercise.exerciseId,
                workoutId = exercise.workoutId,
                name = exercise.name,
                muscleGroup = exercise.muscleGroup,
                machineType = exercise.machineType,
                snapshot = exercise.snapshot,
                sets = sets,
            )
        )
    }

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    val cornerSize = 24.dp
    val outlineColor = MaterialTheme.colorScheme.outlineVariant
    val backgroundColor = MaterialTheme.colorScheme.secondary
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(screenHeight / 3)
            .clip(RoundedCornerShape(cornerSize))
            .border(
                border = BorderStroke(2.dp, color = outlineColor),
                shape = RoundedCornerShape(cornerSize)
            )
            .background(backgroundColor)
    ) {
        HorizontalLineWithText("Exercise data sheet")

        ExerciseDataSheetTitleRow()

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(screenHeight / 4)
        ) {

            //TODO: add functionality when last set has data typed in to add new row...
            items(exercise.sets.size) { setId ->
                ExerciseDataSheetRow(
                    set = exercise.sets[setId],
                    onSetChange = ::handleSetChange
                )
            }
        }
    }
}

@Composable
fun ExerciseDataSheetTitleRow() {
    val textColor = MaterialTheme.colorScheme.onSurface
    val textStyle = MaterialTheme.typography.titleMedium.copy(
        color = textColor
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(35.dp)

            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        //Set number
        Text(
            modifier = Modifier
                .padding(4.dp)
                .weight(0.5f),
            text = "Set",
            style = textStyle,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        //Reps
        Text(
            modifier = Modifier
                .padding(4.dp)
                .weight(1.5f),
            text = "Reps",
            style = textStyle,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        //Weight
        Text(
            modifier = Modifier
                .padding(4.dp)
                .weight(1.5f),
            text = "Weight",
            style = textStyle,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        //Is done checkbox
        Text(
            modifier = Modifier
                .padding(4.dp)
                .weight(1f),
            text = "Done",
            style = textStyle,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun ExerciseDataSheetRow(
    set: ExerciseSetDto,
    onSetChange: (ExerciseSetProgressDto) -> Unit
) {
    val repsFocusRequester = remember { FocusRequester() }
    val weightFocusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val textColor = MaterialTheme.colorScheme.onSurface
    val textStyle = MaterialTheme.typography.titleMedium.copy(
        color = textColor
    )

    val checkboxSelectedColor = Color.Green
    val checkboxBorderColor = MaterialTheme.colorScheme.outlineVariant

    //Forcing Re-composition on set change -> new exercise in ExerciseDataSheet
    key(set.setId) {
        var reps by remember { mutableStateOf(set.reps.toString()) }
        var weight by remember { mutableStateOf(set.weight.toString()) }
        var isDone by remember { mutableStateOf(false) }

        //On data change -> callback called and updates up the ladder via state hoisting
        LaunchedEffect(reps, weight, isDone) {
            onSetChange(
                ExerciseSetProgressDto(
                    baseSet = set.copy(
                        reps = reps.toIntOrNull() ?: set.reps,
                        weight = weight.toFloatOrNull() ?: set.weight
                    ),
                    isDone = isDone
                )
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            //Set number
            Text(
                modifier = Modifier
                    .padding(4.dp)
                    .weight(0.5f),
                text = set.number.toString(),
                style = textStyle,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            //Reps
            ExerciseDataSheetTextField(
                modifier = Modifier
                    .padding(4.dp)
                    .weight(1.5f)
                    .focusRequester(repsFocusRequester),
                text = reps,
                onValueChange = {
                    reps = it
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        weightFocusRequester.requestFocus()
                    }
                )
            )

            //Weight
            ExerciseDataSheetTextField(
                modifier = Modifier
                    .padding(4.dp)
                    .weight(1.5f)
                    .focusRequester(weightFocusRequester),
                text = weight,
                onValueChange = {
                    weight = it
                },
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

            //Checkbox
            Checkbox(
                modifier = Modifier
                    .weight(1f),
                colors = CheckboxDefaults.colors(
                    checkedColor = checkboxSelectedColor,
                    uncheckedColor = checkboxBorderColor //Checkbox border
                ),
                checked = isDone,
                onCheckedChange = {
                    isDone = it
                }
            )
        }
    }
}

@Composable
fun ExerciseDataSheetTextField(
    modifier: Modifier = Modifier,
    text: String,
    onValueChange: (String) -> Unit,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    val cornerSize = 16.dp
    val textColor = MaterialTheme.colorScheme.onSurface
    val outlineColor = MaterialTheme.colorScheme.outlineVariant
    val backgroundColor = MaterialTheme.colorScheme.secondary

    val textStyle = MaterialTheme.typography.titleSmall.copy(
        color = textColor
    )

    TextField(
        modifier = modifier
            .clip(RoundedCornerShape(cornerSize))
            .border(
                border = BorderStroke(2.dp, color = outlineColor),
                shape = RoundedCornerShape(cornerSize)
            )
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(cornerSize)
            ),
        value = text,
        textStyle = textStyle,
        onValueChange = onValueChange,
        keyboardActions = keyboardActions,
        keyboardOptions = keyboardOptions
    )
}

@Preview
@Composable
fun ExerciseDataSheetRowPreview() {
    val exerciseSet = MockupDataGeneratorV2.generateExerciseSet(
        workoutId = Random.nextInt(1, 100),
        exerciseId = Random.nextInt(1, 100)
    )
    val onSetChanged: (ExerciseSetProgressDto) -> Unit = { set ->

    }
    ExerciseDataSheetRow(exerciseSet, onSetChanged)
}

@Preview
@PreviewLightDark
@Composable
fun ExerciseDataSheetPreview() {
    val exercise = MockupDataGeneratorV2.generateExercise()
    val onExerciseDataChange: (ExerciseProgressDto) -> Unit = {

    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        ExerciseDataSheet(exercise, onExerciseDataChange)
    }
}

@Preview
@PreviewLightDark
@Composable
fun ExerciseDataSheetTextFieldPreview() {
    ExerciseDataSheetTextField(text = "50.0", onValueChange = {})
}