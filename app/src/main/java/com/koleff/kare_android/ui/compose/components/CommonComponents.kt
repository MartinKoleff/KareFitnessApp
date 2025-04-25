package com.koleff.kare_android.ui.compose.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.koleff.kare_android.R
import com.koleff.kare_android.common.MockupDataGeneratorV2
import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.data.model.dto.WorkoutDto
import com.koleff.kare_android.ui.theme.LocalExtendedColors

@Composable
fun <T> BannerV2(
    modifier: Modifier,
    onClick: (T) -> Unit,
    data: T,
    title: String,
    imageResource: Int,
    showPlayerIcon: Boolean = false,
    showDifficulty: Boolean = true,
    showMachineType: Boolean = false,
    showTimeAndDateInfo: Boolean = true,
    machineTypeText: String? = null,
    showSelectOption: Boolean = false,
    onSelect: (Boolean) -> Unit = {},
    isSelected: Boolean = false
) {
    val titleTextColor = LocalExtendedColors.current.title
    val titleTextStyle = MaterialTheme.typography.titleMedium.copy(
        color = titleTextColor,
    )

    val subtitleTextColor = LocalExtendedColors.current.label
    val subtitleTextStyle = MaterialTheme.typography.bodyMedium.copy(
        color = subtitleTextColor,
    )

    val tintColor = LocalExtendedColors.current.title
    val bannerHeight = 140.dp

    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(bannerHeight)
                .padding(bottom = 6.dp)
                .clickable {
                    onClick(data)
//                    if (showSelectOption) isSelected = !isSelected
                },
            verticalAlignment = Alignment.CenterVertically
        ) {

            BannerImage(
                modifier = Modifier
                    .padding(6.dp)
                    .size(100.dp),
                imageResource = imageResource,
                showPlayerIcon = showPlayerIcon
            )

            //Data
            Column(
                modifier = Modifier
                    .width(210.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Bottom
            ) {

                //Workout title
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(65.dp),
                    contentAlignment = Alignment.TopStart
                ) {
                    Text(
                        modifier = Modifier
                            .padding(
                            PaddingValues(6.dp)
                        ),
                        text = title,
                        style = titleTextStyle,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                //Time estimation and date created
                if (showTimeAndDateInfo) {
                    Row(
                        modifier = Modifier
                            .height(30.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        DateCreatedInfo("yesterday")
                        TimeEstimationInfo("30 min")
                    }
                } else if (showMachineType) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            modifier = Modifier.padding(
                                PaddingValues(horizontal = 6.dp)
                            ),
                            text = machineTypeText ?: "",
                            style = subtitleTextStyle,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                //Workout difficulty
                if (showDifficulty) {
                    WorkoutDifficultyInfo(
                        difficulty = "Advanced"
                    )

                    Spacer(modifier = Modifier.height(6.dp))
                }
            }

            if (showSelectOption) {
                CircleCheckBox(
                    isChecked = isSelected,
                    onChecked = {
//                        isSelected = !isSelected
                        onSelect(isSelected)
                    }
                )
            }
        }

        HorizontalDivider(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .align(Alignment.BottomCenter),
            color = tintColor,
            thickness = 1.dp
        )
    }
}

@Composable
fun FavoriteStar(isFavorite: Boolean) {
    val imageResource =
        if (isFavorite) R.drawable.ic_vector_favorited_item else R.drawable.ic_vector_favorite_empty

    val iconSize = 20.dp
    val tintColor = MaterialTheme.colorScheme.primary

    Image(
        painter = painterResource(imageResource),
        contentDescription = "Play icon",
        modifier = Modifier
            .padding(6.dp)
            .size(iconSize),
        contentScale = ContentScale.Crop,
        colorFilter = ColorFilter.tint(
            color = tintColor,
            blendMode = BlendMode.SrcIn
        )
    )
}


@Composable
fun BannerImage(modifier: Modifier = Modifier, imageResource: Int, showPlayerIcon: Boolean) {
    val cornerSize = 32.dp
    val iconSize = 20.dp
    val outlineColor = MaterialTheme.colorScheme.outlineVariant

    Box(
        modifier
            .border(
                border = BorderStroke(2.dp, color = outlineColor),
                shape = RoundedCornerShape(cornerSize)
            )
    ) {
        Image(
            painter = painterResource(imageResource),
            contentDescription = "Banner image",
            modifier = Modifier
                .clip(RoundedCornerShape(cornerSize)),
            contentScale = ContentScale.Crop
        )

        if (showPlayerIcon) {
            Image(
                painter = painterResource(R.drawable.player), //player icon
                contentDescription = "Play icon",
                modifier = Modifier
                    .size(iconSize)
                    .align(Alignment.Center),
                contentScale = ContentScale.Crop
            )
        }
    }
}

//@Preview
//@Composable
//private fun WorkoutImagePreview() {
//    WorkoutImage(workout = MockupDataGeneratorV2.generateWorkout())
//}

@Composable
fun WorkoutDifficultyInfo(modifier: Modifier = Modifier, difficulty: String) {
    val cornerSize = 32.dp

    val outlineColor = MaterialTheme.colorScheme.outlineVariant
    val backgroundColor = MaterialTheme.colorScheme.surface
    val labelTextColor = LocalExtendedColors.current.label
    val labelTextStyle = MaterialTheme.typography.labelMedium.copy(
        color = labelTextColor
    )
    val tintColor = MaterialTheme.colorScheme.primary
    val iconSize = 20.dp

    Row(
        modifier = modifier
            .height(30.dp)
            .clip(RoundedCornerShape(cornerSize))
            .border(
                border = BorderStroke(2.dp, color = outlineColor),
                shape = RoundedCornerShape(cornerSize)
            )
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(cornerSize)
            )
            .padding(horizontal = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Icon(
            modifier = Modifier
                .size(iconSize),
            painter = painterResource(R.drawable.weight),
            contentDescription = "Workout difficulty",
            tint = tintColor
        )

        Text(
            modifier = Modifier.padding(
                PaddingValues(horizontal = 6.dp)
            ),
            text = difficulty,
            style = labelTextStyle,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview
@Composable
private fun WorkoutDifficultyInfoPreview() {
    WorkoutDifficultyInfo(difficulty = "Advanced")
}

@Composable
fun DateCreatedInfo(dateCreated: String) {  //TODO: Change to last performed time...
    MiniRow(dateCreated, R.drawable.timer)
}

@Composable
fun MiniRow(text: String, imageResource: Int) {
    val labelTextColor = LocalExtendedColors.current.label
    val labelTextStyle = MaterialTheme.typography.labelMedium.copy(
        color = labelTextColor,
    )
    val tintColor = MaterialTheme.colorScheme.primary
    val iconSize = 20.dp

    Row(
        modifier = Modifier
            .height(30.dp)
            .padding(horizontal = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier
                .size(iconSize),
            painter = painterResource(imageResource),
            contentDescription = "",
            tint = tintColor,
        )

        Text(
            modifier = Modifier.padding(horizontal = 6.dp),
            text = text,
            style = labelTextStyle,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview
@Composable
private fun DateCreatedInfoPreview() {
    DateCreatedInfo("yesterday")
}

@Composable
fun TimeEstimationInfo(time: String) {
    MiniRow(time, R.drawable.diagram)
}

@Preview
@Composable
private fun TimeEstimationInfoPreview() {
    TimeEstimationInfo("30 min")
}

@Composable
fun WorkoutBannerV2(
    modifier: Modifier = Modifier,
    workout: WorkoutDto,
    onClick: (WorkoutDto) -> Unit
) {
    BannerV2(
        modifier = modifier,
        data = workout,
        onClick = { onClick(workout) },
        title = workout.name,
        imageResource = MuscleGroup.getImage(workout.muscleGroup),
        showDifficulty = true,
        showMachineType = false,
        showPlayerIcon = false,
    )
}

@Preview
@Composable
private fun WorkoutBannerV2Preview() {
    val onClick: (WorkoutDto) -> Unit = {

    }
    val workout = WorkoutDto(name = "Arnold Workout")

    WorkoutBannerV2(
        modifier = Modifier,
        workout = workout,
        onClick = onClick
    )
}

@Preview
@Composable
private fun WorkoutBannerV2Preview2() {
    val onClick: (WorkoutDto) -> Unit = {

    }
    val workout = MockupDataGeneratorV2.generateWorkout(name = "Arnold Workout Workout Workout")

    WorkoutBannerV2(
        modifier = Modifier,
        workout = workout,
        onClick = onClick
    )
}

@Composable
fun ExerciseBannerV2(
    modifier: Modifier = Modifier,
    exercise: ExerciseDto,
    showSelectOption: Boolean = false,
    showDifficulty: Boolean,
    onClick: (ExerciseDto) -> Unit,
    onSelect: (ExerciseDto) -> Unit = {},
    isSelected: Boolean = false
) {
    BannerV2(
        modifier = modifier,
        onClick = onClick,
        title = exercise.name,
        data = exercise,
        imageResource = MuscleGroup.getImage(exercise.muscleGroup),
        showDifficulty = showDifficulty,
        showMachineType = true,
        showPlayerIcon = true,
        showTimeAndDateInfo = false,
        machineTypeText = exercise.machineType.name,
        showSelectOption = showSelectOption,
        onSelect = {
            onSelect(exercise)
        },
        isSelected = isSelected
    )
}

@Preview
@Composable
private fun ExerciseBannerV2Preview() {
    val onClick: (ExerciseDto) -> Unit = {

    }
    val exercise = MockupDataGeneratorV2.generateExercise()
    ExerciseBannerV2(exercise = exercise, onClick = onClick, showDifficulty = false, isSelected = false)
}


@Preview
@Composable
private fun ExerciseBannerV2Preview2() {
    val onClick: (ExerciseDto) -> Unit = {

    }
    val exercise =
        MockupDataGeneratorV2.generateExercise().copy(name = "Exercise name Exercise name")
    ExerciseBannerV2(
        exercise = exercise,
        onClick = onClick,
        showSelectOption = true,
        showDifficulty = true,
        isSelected = false
    )
}

@Composable
fun CircleCheckBox(
    isChecked: Boolean,
    onChecked: () -> Unit = {},
) {
    val tintColor = MaterialTheme.colorScheme.primary
    val iconSize = 30.dp

    Icon(
        modifier = Modifier
            .size(iconSize)
            .clickable {
                onChecked()
            },
        imageVector = if (isChecked) Icons.Default.CheckCircle else Icons.Outlined.Circle,
        contentDescription = "Check circle button",
        tint = tintColor
    )
}

@Preview
@Composable
private fun CicleCheckBoxPreview() {
    var isChecked by remember {
        mutableStateOf(true)
    }
    CircleCheckBox(
        isChecked = isChecked,
        onChecked = { isChecked = !isChecked }
    )
}

@Composable
fun FooterButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit
) {
    val cornerSize = 24.dp

    val buttonColor = MaterialTheme.colorScheme.primary
    val textColor = LocalExtendedColors.current.title
    val outlineColor = MaterialTheme.colorScheme.outlineVariant

    val textStyle = MaterialTheme.typography.titleLarge.copy(
        color = textColor
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(horizontal = 64.dp)
            .clip(RoundedCornerShape(cornerSize))
            .border(
                border = BorderStroke(2.dp, color = outlineColor),
                shape = RoundedCornerShape(cornerSize)
            )
            .background(
                color = buttonColor,
                shape = RoundedCornerShape(cornerSize)
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {

        Text(
            modifier = Modifier.padding(
                PaddingValues(8.dp)
            ),
            text = text,
            style = textStyle,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview
@Composable
private fun FooterButtonPreview() {
    FooterButton(
        text = "Button"
    ) { }
}

@Composable
fun SearchBar(
    searchText: String,
    modifier: Modifier = Modifier,
    onToggleSearch: () -> Unit,
    onSearchTextChange: (String) -> Unit,
    onSearch: (String) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    val textColor = MaterialTheme.colorScheme.onSurface
    val tintColor = MaterialTheme.colorScheme.onSurface

    val textStyle = MaterialTheme.typography.bodyLarge.copy(
        color = textColor
    )

    Row(modifier = modifier) {
        TextField(
            value = searchText,
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent, //Hide the bottom line indicator
                unfocusedIndicatorColor = Color.Transparent,
            ),
            onValueChange = {
                if ((searchText.isNotEmpty() && it.isEmpty()) ||
                    (searchText.isEmpty() && it.isNotEmpty())
                ){
                    onToggleSearch()

                    keyboardController?.hide()
                    focusManager.clearFocus()
                }

                onSearchTextChange(it) // Update external state

                // Trigger search
                onSearch(it)
            },
            label = { Text(text = "Search", style = textStyle) },
            leadingIcon = {
                Icon(
                    Icons.Filled.Search,
                    tint = tintColor,
                    contentDescription = "Search icon"
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearch(searchText)

                    keyboardController?.hide()
                    focusManager.clearFocus()
                },
                onDone = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                }
            ),
            shape = RoundedCornerShape(50.dp)
        )
    }
}

@Preview
@PreviewLightDark
@Composable
fun SearchBarPreview() {
    SearchBar(
        searchText = "Test",
        onSearchTextChange = {},
        onToggleSearch = {},
        onSearch = {}
    )
}


@Composable
fun LoadingWheel(
    modifier: Modifier = Modifier
        .fillMaxSize(),
    innerPadding: PaddingValues = PaddingValues(8.dp),
    hideScreen: Boolean = false,
    customBackground: Color = Color.Transparent
) {
    val background = if (hideScreen) {
        MaterialTheme.colorScheme.surface
    } else customBackground

    Box(
        modifier = modifier
            .padding(innerPadding)
            .background(background),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(50.dp),
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Preview
@Composable
private fun LoadingWheelPreview() {
    LoadingWheel()
}