//package com.koleff.kare_android.ui.compose.components
//
//import android.util.Log
//import androidx.compose.foundation.BorderStroke
//import androidx.compose.foundation.ExperimentalFoundationApi
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.layout.wrapContentWidth
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.rememberLazyListState
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.text.BasicTextField
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.material3.HorizontalDivider
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.draw.drawBehind
//import androidx.compose.ui.geometry.CornerRadius
//import androidx.compose.ui.geometry.Offset
//import androidx.compose.ui.geometry.Size
//import androidx.compose.ui.graphics.BlendMode
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.ColorFilter
//import androidx.compose.ui.graphics.graphicsLayer
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.platform.LocalDensity
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.text.style.TextOverflow
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.tooling.preview.PreviewLightDark
//import androidx.compose.ui.unit.dp
//import com.koleff.kare_android.R
//import com.koleff.kare_android.common.manager.data.MockupDataGeneratorV2
//import com.koleff.kare_android.data.model.dto.ExerciseSetDto
//import com.koleff.kare_android.ui.state.CircularTimerStyle
//import kotlin.random.Random
//
//@Composable
//fun ExerciseSetRow(
//    modifier: Modifier = Modifier,
//    set: ExerciseSetDto,
//    onRepsChanged: (Int) -> Unit,
//    onWeightChanged: (Float) -> Unit,
//    onDelete: (ExerciseSetDto) -> Unit
//) {
//    val setNumber = set.number
//
//    val cornerSize = 16.dp
//    val backgroundColor = MaterialTheme.colorScheme.tertiary
//    val textColor = MaterialTheme.colorScheme.onSurface
//    val outlineColor = MaterialTheme.colorScheme.outlineVariant
//
//    val textStyle = MaterialTheme.typography.titleMedium.copy(
//        color = textColor
//    )
//    Column(modifier = modifier) {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(50.dp)
//                .padding(horizontal = 16.dp, vertical = 4.dp)
//                .background(
//                    color = backgroundColor,
//                    shape = RoundedCornerShape(cornerSize)
//                )
//                .border(
//                    border = BorderStroke(1.5.dp, outlineColor),
//                    shape = RoundedCornerShape(cornerSize)
//                ),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Text(
//                modifier = Modifier.padding(horizontal = 8.dp),
//                text = "Set $setNumber",
//                style = textStyle,
//                maxLines = 1,
//                overflow = TextOverflow.Ellipsis
//            )
//
//            RepsTextField(
//                modifier = Modifier
//                    .weight(1f)
//                    .padding(horizontal = 4.dp),
//                reps = set.reps,
//                onRepsChanged = onRepsChanged
//            )
//            Spacer(modifier = Modifier.width(8.dp))
//
//            WeightTextField(
//                modifier = Modifier
//                    .weight(1f)
//                    .padding(horizontal = 4.dp),
//                weight = set.weight,
//                onWeightChanged = onWeightChanged
//            )
//        }
//
//        //Delete footer
//        ExerciseSetRowFooter(
//            modifier = Modifier
//                .padding(horizontal = 16.dp, vertical = 8.dp),
//            onDelete = { onDelete(set) },
//            setToDelete = set
//        )
//    }
//}
//
//@Composable
//fun ExerciseSetRowFooter(
//    modifier: Modifier = Modifier,
//    onDelete: (ExerciseSetDto) -> Unit,
//    setToDelete: ExerciseSetDto
//) {
//    val dividerColor = MaterialTheme.colorScheme.outlineVariant
//    val imageSize = 30.dp
//    Box(
//        modifier = modifier
//            .fillMaxWidth()
//            .height(imageSize),
//        contentAlignment = Alignment.Center
//    ) {
//        HorizontalDivider(thickness = 3.dp, color = dividerColor)
//
//        Box(
//            modifier = Modifier
//                .size(imageSize)
//                .clickable { onDelete(setToDelete) }
//        ) {
//            Image(
//                painter = painterResource(id = R.drawable.ic_delete_2),
//                contentDescription = "Delete exercise set",
//                contentScale = ContentScale.Crop
//            )
//        }
//    }
//}
//
//@Composable
//fun WeightTextField(
//    modifier: Modifier,
//    weight: Float,
//    onWeightChanged: (Float) -> Unit
//) {
//    val weightState = remember {
//        mutableStateOf(weight.toString())
//    }
//
//    ExerciseSetTextField(
//        modifier = modifier,
//        state = weightState.value,
//        onTextChange = {
//            weightState.value = it
//            onWeightChanged(
//                it.toFloatOrNull() ?: weight
//            ) //Update the parent with the new value or retain the old value if null
//        },
//        label = "Weight"
//    )
//}
//
//@Composable
//fun RepsTextField(
//    modifier: Modifier,
//    reps: Int,
//    onRepsChanged: (Int) -> Unit
//) {
//    val repsState = remember {
//        mutableStateOf(reps.toString())
//    }
//
//    ExerciseSetTextField(
//        modifier = modifier,
//        state = repsState.value,
//        onTextChange = {
//            repsState.value = it
//            onRepsChanged(
//                it.toIntOrNull() ?: reps
//            ) //Update the parent with the new value or retain the old value if null
//        },
//        label = "Reps"
//    )
//}
//
//@Composable
//fun ExerciseSetTextField(
//    modifier: Modifier = Modifier,
//    label: String,
//    state: String,
//    onTextChange: (String) -> Unit
//) {
//
//    //For dark theme...
//    val textColor = MaterialTheme.colorScheme.onSurface
//    val backgroundColor = MaterialTheme.colorScheme.tertiary
//    val outlineColor = MaterialTheme.colorScheme.outlineVariant
//
//    val textStyle = MaterialTheme.typography.bodyLarge.copy(
//        color = textColor
//    )
//    val cornerSize = 16.dp
//    BasicTextField(
//        modifier = modifier,
//        value = state,
//        onValueChange = { text ->
//            onTextChange(text)
//        },
//        textStyle = textStyle,
//        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
//        maxLines = 1,
//        decorationBox = { innerTextField ->
//            Row(
//                modifier = Modifier
//                    .clip(shape = RoundedCornerShape(cornerSize))
//                    .border(
//                        border = BorderStroke(1.dp, outlineColor),
//                        shape = RoundedCornerShape(cornerSize)
//                    )
//                    .background(backgroundColor),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Text(
//                    modifier = Modifier.padding(horizontal = 6.dp),
//                    text = label,
//                    style = textStyle
//                )
//                innerTextField()
//            }
//        })
//}
//
//@Preview
//@Composable
//fun ExerciseSetRowFooterPreview() {
//    val onDelete: (ExerciseSetDto) -> Unit = {
//
//    }
//    val exerciseSet = MockupDataGeneratorV2.generateExerciseSet(
//        workoutId = Random.nextInt(1, 100),
//        exerciseId = Random.nextInt(1, 100)
//    )
//    ExerciseSetRowFooter(
//        setToDelete = exerciseSet,
//        onDelete = onDelete
//    )
//}
//
//@Preview
//@Composable
//fun ExerciseSetRepsTextFieldPreview() {
//    Box(
//        modifier = Modifier
//            .size(500.dp)
//            .background(MaterialTheme.colorScheme.surface)
//    ) {
//        RepsTextField(
//            modifier = Modifier
//                .height(50.dp)
//                .width(200.dp),
//            reps = 12
//        ) {
//
//        }
//    }
//}
//
//@Preview
//@Composable
//fun ExerciseSetWeightTextFieldPreview() {
//    Box(
//        modifier = Modifier
//            .size(500.dp)
//            .background(MaterialTheme.colorScheme.surface)
//    ) {
//        WeightTextField(
//            modifier = Modifier
//                .height(50.dp)
//                .width(200.dp),
//            weight = 50.0f
//        ) {
//
//        }
//    }
//}
//
//@Composable
//fun AddNewSetFooter(onAddNewSetAction: () -> Unit) {
//    val height = 50.dp
//    val cornerSize = 24.dp
//    val backgroundColor = MaterialTheme.colorScheme.tertiary
//    val textColor = MaterialTheme.colorScheme.onSurface
//    val outlineColor = MaterialTheme.colorScheme.outlineVariant
//
//    val textStyle = MaterialTheme.typography.titleLarge.copy(
//        color = textColor
//    )
//
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(height)
//            .padding(horizontal = 32.dp)
//            .clip(RoundedCornerShape(cornerSize))
//            .border(
//                border = BorderStroke(2.dp, color = outlineColor),
//                shape = RoundedCornerShape(cornerSize)
//            )
//            .background(
//                color = backgroundColor,
//                shape = RoundedCornerShape(cornerSize)
//            )
//            .clickable { onAddNewSetAction() },
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.Center
//    ) {
//        Image(
//            painter = painterResource(R.drawable.ic_vector_add_green),
//            contentDescription = null,
//            contentScale = ContentScale.Crop,
//            modifier = Modifier
//                .size(height)
//                .graphicsLayer { alpha = 0.80f }
//        )
//
//        Text(
//            modifier = Modifier
//                .padding(horizontal = 16.dp),
//            text = "Add new set",
//            style = textStyle,
//            textAlign = TextAlign.Start,
//            maxLines = 1,
//            overflow = TextOverflow.Ellipsis
//        )
//    }
//}
//
//@Preview
//@Composable
//fun AddNewSetFooterPreview() {
//    AddNewSetFooter() {
//
//    }
//}
//
//@Preview
//@Composable
//fun ExerciseSetRowPreview() {
//    val exerciseSet = MockupDataGeneratorV2.generateExerciseSet(
//        workoutId = Random.nextInt(1, 100),
//        exerciseId = Random.nextInt(1, 100)
//    )
//    ExerciseSetRow(
//        set = exerciseSet,
//        onWeightChanged = {},
//        onRepsChanged = {},
//        onDelete = {}
//    )
//}
//
//
////@Composable
////fun ExerciseSetRowList(
////    modifier: Modifier,
////    exerciseSetList: List<ExerciseSetDto>,
////    onRepsChanged: (Int) -> Unit, // Callback when reps are updated
////    onWeightChanged: (Float) -> Unit // Callback when weight is updated
////) {
////
////    val totalExerciseSets = if (exerciseSetList.size < 3) {
////        3
////    } else {
////        exerciseSetList.size
////    }
////
////    LazyColumn(modifier = modifier) {
////        items(totalExerciseSets) { currentSetId ->
////
////            //Check if set exists in the allowed bounds
////            val currentSet = if (currentSetId >= 0 && currentSetId < exerciseSetList.size) {
////                exerciseSetList[currentSetId]
////            } else {
////
////                //Default set
////                ExerciseSetDto(UUID.randomUUID(), currentSetId + 1, 12, 0f)
////            }
////            ExerciseSetRow(
////                set = currentSet,
////                onRepsChanged = onRepsChanged,
////                onWeightChanged = onWeightChanged,
////                onDelete = onDelete
////            )
////        }
////    }
////}
//
////@Preview
////@Composable
////fun ExerciseSetRowListPreview() {
////    val exerciseSetList = MockupDataGenerator.generateExerciseSetsList()
////    ExerciseSetRowList(
////        modifier = Modifier.fillMaxSize(),
////        exerciseSetList = exerciseSetList,
////        onWeightChanged = {},
////        onRepsChanged = {})
//
////}
//
//enum class TimeType(val maxTime: Int) {
//    Hours(13), //Start from 0 until n - 1
//    Minutes(61),
//    Seconds(61)
//}
//
//@OptIn(ExperimentalFoundationApi::class)
//@Composable
//fun CircularTimePicker(
//    timeType: TimeType,
//    initialTime: Int,
//    circularTimerStyle: CircularTimerStyle = CircularTimerStyle()
//) {
//    val expandedSize = timeType.maxTime * 10_000_000
//    val initialListPoint = expandedSize / 2
//    val targetIndex = initialListPoint + initialTime - 1 //Middle time is the selected one -> - 1
//
//    val scrollState = rememberLazyListState(initialFirstVisibleItemIndex = targetIndex)
//    val flingBehavior = rememberSnapFlingBehavior(scrollState)
//
//    val textColor = MaterialTheme.colorScheme.onSurface
//    val outlineColor = MaterialTheme.colorScheme.outlineVariant
//
//    val textStyle = MaterialTheme.typography.titleLarge.copy(
//        color = textColor
//    )
//
//    LaunchedEffect(scrollState.isScrollInProgress) {
//        if (!scrollState.isScrollInProgress) {
//            val currentTime =
//                if (scrollState.firstVisibleItemIndex / 10_000_000 == timeType.maxTime) timeType.maxTime //Max time reached...
//                else ((scrollState.firstVisibleItemIndex + 1) % timeType.maxTime) //Middle time is the selected one -> + 1
//
//            Log.e("Time", "$timeType: $currentTime")
//        }
//    }
//
//    Box(
//        modifier = Modifier
//            .height(circularTimerStyle.size)
//            .wrapContentWidth()
//    ) {
//        LazyColumn(
//            modifier = Modifier.wrapContentWidth(),
//            state = scrollState,
//            flingBehavior = flingBehavior
//        ) {
//            items(expandedSize) { index ->
//                val num = (index % timeType.maxTime)
//                Box(
//                    modifier = Modifier.size(circularTimerStyle.cellSize),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Text(
//                        text = String.format("%02d", num),
//                        style = textStyle,
//                        fontSize = LocalDensity.current.run { circularTimerStyle.cellTextSize.toSp() }
//                    )
//                }
//            }
//        }
//    }
//}
//
//@Preview(showBackground = true)
//@PreviewLightDark
//@Composable
//fun CircularTimePickerPreview() {
//    val size = CircularTimerStyle().size
//    val backgroundColor = MaterialTheme.colorScheme.scrim
//
//    Row(
//        modifier = Modifier
//            .size(size)
//            .background(backgroundColor)
//    ) {
//        CircularTimePicker(TimeType.Hours, initialTime = 5)
//        CircularTimePicker(TimeType.Minutes, initialTime = 15)
//        CircularTimePicker(TimeType.Seconds, initialTime = 30)
//    }
//}
//
//@Composable
//fun CircularTimerFooter(
//    modifier: Modifier = Modifier,
//    circularTimerStyle: CircularTimerStyle = CircularTimerStyle()
//) {
//    val textColor = MaterialTheme.colorScheme.onSurface
//
//    val textStyle = MaterialTheme.typography.titleLarge.copy(
//        color = textColor
//    )
//
//    val cornerSize = 20.dp
//    Row(
//        modifier = modifier
//            .height(circularTimerStyle.size),
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//
//        //Text
//        Text(
//            modifier = Modifier
//                .padding(4.dp),
//            text = "Time:",
//            style = textStyle,
//            textAlign = TextAlign.Center,
//            maxLines = 1,
//            overflow = TextOverflow.Ellipsis
//        )
//
//        //Timer
//        Row(
//            modifier = Modifier
//                .width(300.dp)
//                .height(circularTimerStyle.size)
//                .padding(horizontal = 16.dp)
//                .drawBehind {
//
//                    //Selector
//                    drawRoundRect(
//                        color = Color.Gray,
//                        topLeft = Offset(x = 0.dp.toPx(), y = circularTimerStyle.cellSize.toPx()),
//                        size = Size(
//                            width = this.size.width,
//                            height = circularTimerStyle.cellSize.toPx()
//                        ),
//                        cornerRadius = CornerRadius(x = cornerSize.toPx(), y = cornerSize.toPx()),
//                        colorFilter = ColorFilter.tint(Color.Gray, BlendMode.Screen)
//                    )
//                }
//        ) {
//
//
//            Box(modifier = Modifier.weight(1f)) {
//                CircularTimePicker(TimeType.Minutes, initialTime = 0)
//
//                //Text
//                Box(
//                    modifier = Modifier.fillMaxSize(),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Text(
//                        modifier = Modifier
//                            .padding(4.dp),
//                        text = "min.",
//                        style = textStyle,
//                        textAlign = TextAlign.Center,
//                        maxLines = 1,
//                        overflow = TextOverflow.Ellipsis
//                    )
//                }
//            }
//            Box(modifier = Modifier.weight(1f)) {
//                CircularTimePicker(TimeType.Seconds, initialTime = 0)
//
//                //Text
//                Box(
//                    modifier = Modifier.fillMaxSize(),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Text(
//                        modifier = Modifier
//                            .padding(4.dp),
//                        text = "sec.",
//                        style = textStyle,
//                        textAlign = TextAlign.Center,
//                        maxLines = 1,
//                        overflow = TextOverflow.Ellipsis
//                    )
//                }
//            }
//        }
//    }
//}
//
//@Preview(showBackground = true)
//@PreviewLightDark
//@Composable
//fun CircularTimerFooterPreview() {
//    val backgroundColor = MaterialTheme.colorScheme.scrim
//
//    CircularTimerFooter(modifier = Modifier.background(backgroundColor))
//}
//
//@Composable
//fun RestBetweenSetsFooter(
//    modifier: Modifier = Modifier,
//    onCheckedChange: (Boolean) -> Unit,
//    isChecked: Boolean = false
//) {
//    val textColor = MaterialTheme.colorScheme.onSurface
//
//    val textStyle = MaterialTheme.typography.titleLarge.copy(
//        color = textColor
//    )
//
//    Column(
//        modifier = modifier.padding(horizontal = 16.dp, vertical = 4.dp)
//    ) {
//
//        //Toggle switch header
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
//
//            //Text
//            Text(
//                modifier = Modifier
//                    .padding(4.dp),
//                text = "Rest between sets:",
//                style = textStyle,
//                textAlign = TextAlign.Center,
//                maxLines = 1,
//                overflow = TextOverflow.Ellipsis
//            )
//
//            //Switch
//            SwitchButton(isChecked = isChecked, onCheckedChange = onCheckedChange)
//        }
//
//        if (isChecked) {
//            CircularTimerFooter()
//        }
//    }
//}
//
//@Preview
//@PreviewLightDark
//@Composable
//fun RestBetweenSetsFooterPreview() {
//    val backgroundColor = MaterialTheme.colorScheme.scrim
//
//    RestBetweenSetsFooter(
//        modifier = Modifier.background(backgroundColor),
//        onCheckedChange = {},
//        isChecked = true
//    )
//}
//
//@Preview
//@PreviewLightDark
//@Composable
//fun RestBetweenSetsFooterPreview2() {
//    val backgroundColor = MaterialTheme.colorScheme.scrim
//
//    RestBetweenSetsFooter(
//        modifier = Modifier.background(backgroundColor),
//        onCheckedChange = {},
//        isChecked = false
//    )
//}
