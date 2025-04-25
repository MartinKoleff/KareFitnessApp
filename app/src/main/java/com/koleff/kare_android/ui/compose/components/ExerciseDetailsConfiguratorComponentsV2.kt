package com.koleff.kare_android.ui.compose.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.koleff.kare_android.R
import com.koleff.kare_android.ui.theme.LocalExtendedColors
import kotlin.random.Random

@Composable
fun ConfigurationBox(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    iconResource: Int,
    total: Int,
    onNegativeAction: () -> Unit,
    onPositiveAction: () -> Unit
) {
    val titleTextColor = LocalExtendedColors.current.title
    val subtitleTextColor = LocalExtendedColors.current.subtitle

    val titleTextStyle = MaterialTheme.typography.bodyMedium.copy(
        color = titleTextColor
    )

    val subtitleTextStyle = MaterialTheme.typography.labelSmall.copy(
        color = subtitleTextColor
    )
    val iconSize = 25.dp
    val tintColor = MaterialTheme.colorScheme.primary

    val outlineColor = MaterialTheme.colorScheme.outlineVariant
    val cornerSize = 6.dp

    val paddingValues = PaddingValues(
        start = 16.dp,
        end = 16.dp,
        top = 8.dp
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(cornerSize))
            .border(
                border = BorderStroke(2.dp, color = outlineColor),
                shape = RoundedCornerShape(cornerSize)
            ),
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.padding(vertical = 4.dp),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = title,
                    style = titleTextStyle,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = subtitle,
                    style = subtitleTextStyle,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Icon(
                modifier = Modifier
                    .size(iconSize),
                painter = painterResource(iconResource),
                contentDescription = title,
                tint = tintColor
            )
        }
        Spacer(modifier = Modifier.height(12.dp))

        ConfigurationRow(
            onNegativeAction = onNegativeAction,
            onPositiveAction = onPositiveAction,
            total = total
        )
    }
}

@Composable
fun ConfigurationRow(onNegativeAction: () -> Unit, onPositiveAction: () -> Unit, total: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp, start = 16.dp, end = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        MinusButton(onClick = onNegativeAction)
        NumberDisplay(number = total)
        PlusButton(onClick = onPositiveAction)
    }
}

@Preview
@Composable
private fun ConfigurationRowPreview() {
    ConfigurationRow(
        onPositiveAction = {},
        onNegativeAction = {},
        total = Random.nextInt(1, 10)
    )
}

@Composable
fun NumberDisplay(number: Int) {
    val cornerSize = 6.dp
    val outlineColor = MaterialTheme.colorScheme.outlineVariant

    val titleTextColor = LocalExtendedColors.current.title
    val titleTextStyle = MaterialTheme.typography.titleLarge.copy(
        titleTextColor
    )
    Box(
        modifier = Modifier
            .size(75.dp)
            .clip(RoundedCornerShape(cornerSize))
            .border(
                border = BorderStroke(2.dp, color = outlineColor),
                shape = RoundedCornerShape(cornerSize)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = number.toString(),
            style = titleTextStyle,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
private fun NumberDisplayPreview() {
    NumberDisplay(Random.nextInt(1, 10))
}

@Composable
fun ConfigurationButton(imageResource: Int, onClick: () -> Unit) {
    val cornerSize = 6.dp
    val outlineColor = MaterialTheme.colorScheme.outlineVariant
    val tintColor = MaterialTheme.colorScheme.primary

    val iconSize = 30.dp
    Box(
        modifier = Modifier
            .size(45.dp)
            .clip(RoundedCornerShape(cornerSize))
            .border(
                border = BorderStroke(2.dp, color = outlineColor),
                shape = RoundedCornerShape(cornerSize)
            )
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier.size(iconSize),
            painter = painterResource(id = imageResource),
            contentDescription = "Configuration action",
            colorFilter = ColorFilter.tint(
                color = tintColor,
                blendMode = BlendMode.SrcIn
            ),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun PlusButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    ConfigurationButton(imageResource = R.drawable.plus, onClick = onClick)
}

@Preview
@Composable
private fun PlusButtonPreview() {
    PlusButton() {

    }
}

@Composable
fun MinusButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    ConfigurationButton(imageResource = R.drawable.minus, onClick = onClick)
}

@Preview
@Composable
private fun MinusButtonPreview() {
    MinusButton() {

    }
}

@Preview
@Composable
private fun NumberOfSetsBoxPreview() {
    NumberOfSetsBox(
        totalSets = Random.nextInt(1, 10),
        onDeleteSet = {},
        onAddSet = {}
    )
}

@Composable
fun NumberOfSetsBox(totalSets: Int, onDeleteSet: () -> Unit, onAddSet: () -> Unit) {
    ConfigurationBox(
        title = "Number of sets",
        subtitle = "How many sets to perform",
        iconResource = R.drawable.weight,
        total = totalSets,
        onNegativeAction = onDeleteSet,
        onPositiveAction = onAddSet
    )
}

@Composable
fun NumberOfRepsBox(totalReps: Int, onDecreaseReps: () -> Unit, onIncreaseReps: () -> Unit) {
    ConfigurationBox(
        title = "Number of reps",
        subtitle = "How many reps for each set",
        iconResource = R.drawable.timer,
        total = totalReps,
        onNegativeAction = onDecreaseReps,
        onPositiveAction = onIncreaseReps
    )
}

@Preview
@Composable
private fun NumberOfRepsBoxPreview() {
    NumberOfRepsBox(
        totalReps = Random.nextInt(1, 10),
        onDecreaseReps = {},
        onIncreaseReps = {}
    )
}

@Composable
fun WeightBox(
    modifier: Modifier = Modifier,
    weight: Float,
    onIncreaseWeight: (Int) -> Unit,
    onDecreaseWeight: (Int) -> Unit
) {
    val titleTextColor = LocalExtendedColors.current.title
    val titleTextStyle = MaterialTheme.typography.bodyMedium.copy(
        color = titleTextColor
    )

    val headlineTextStyle = MaterialTheme.typography.titleLarge.copy(
        color = titleTextColor
    )

    val subtitleTextColor = LocalExtendedColors.current.subtitle
    val subtitleTextStyle = MaterialTheme.typography.labelSmall.copy(
        color = subtitleTextColor
    )
    val iconSize = 25.dp
    val tintColor = MaterialTheme.colorScheme.primary

    val outlineColor = MaterialTheme.colorScheme.outlineVariant
    val cornerSize = 6.dp

    val paddingValues = PaddingValues(
        start = 16.dp,
        end = 16.dp,
        top = 8.dp
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(cornerSize))
            .border(
                border = BorderStroke(2.dp, color = outlineColor),
                shape = RoundedCornerShape(cornerSize)
            ),
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 4.dp),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Weight (lbs)",
                    style = titleTextStyle,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "How much weight per set",
                    style = subtitleTextStyle,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Icon(
                    modifier = Modifier
                        .size(iconSize)
                        .clickable { onDecreaseWeight(1) },
                    painter = painterResource(R.drawable.arrow_down),
                    contentDescription = "Decrease weight",
                    tint = tintColor
                )
                Text(
                    text = weight.toString(),
                    style = headlineTextStyle,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Icon(
                    modifier = Modifier
                        .size(iconSize)
                        .clickable { onIncreaseWeight(1) },
                    painter = painterResource(R.drawable.arrow_up),
                    contentDescription = "Increase weight",
                    tint = tintColor
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))

        WeightConfigurationRow(
            onIncreaseWeight = onIncreaseWeight,
            onDecreaseWeight = onDecreaseWeight
        )
    }
}

@Preview
@Composable
private fun WeightBoxPreview() {
    WeightBox(
        weight = Random.nextInt(10).toFloat(),
        onIncreaseWeight = {},
        onDecreaseWeight = {}
    )
}

@Composable
fun WeightConfigurationRow(onIncreaseWeight: (Int) -> Unit, onDecreaseWeight: (Int) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp, start = 16.dp, end = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        WeightConfigurationButton(weight = 10, isDelete = true, onClick = { onDecreaseWeight(10) })
        WeightConfigurationButton(weight = 20, isDelete = true, onClick = { onDecreaseWeight(20) })
        WeightConfigurationButton(weight = 10, isDelete = false, onClick = { onIncreaseWeight(10) })
        WeightConfigurationButton(weight = 20, isDelete = false, onClick = { onIncreaseWeight(20) })
    }
}

@Composable
fun WeightConfigurationButton(weight: Int, isDelete: Boolean, onClick: () -> Unit) {
    val cornerSize = 6.dp
    val outlineColor = MaterialTheme.colorScheme.outlineVariant

    val titleTextColor = LocalExtendedColors.current.title
    val titleTextStyle = MaterialTheme.typography.titleMedium.copy(
        titleTextColor
    )
    Box(
        modifier = Modifier
            .height(45.dp)
            .width(75.dp)
            .clip(RoundedCornerShape(cornerSize))
            .border(
                border = BorderStroke(2.dp, color = outlineColor),
                shape = RoundedCornerShape(cornerSize)
            )
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (isDelete) "- $weight" else "+ $weight",
            style = titleTextStyle,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
private fun WeightConfigurationBoxPreview() {
    WeightConfigurationButton(
        weight = Random.nextInt(10, 50),
        isDelete = false,
        onClick = {}
    )
}

@Preview
@Composable
private fun WeightConfigurationBoxPreview2() {
    WeightConfigurationButton(
        weight = Random.nextInt(10, 50),
        isDelete = true,
        onClick = {}
    )
}

@Composable
fun SaveExerciseConfigurationButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    FooterButton(modifier = modifier, text = "Save configuration", onClick = onClick)
}

@Preview
@Composable
private fun SaveExerciseConfigurationButtonPreview() {
    SaveExerciseConfigurationButton { }
}

@Composable
fun CurrentSetFooter(currentSetIndex: Int, increaseCurrentEditedSet: () -> Unit, decreaseCurrentEditedSet: () -> Unit) {
    val titleTextColor = LocalExtendedColors.current.title
    val titleTextStyle = MaterialTheme.typography.titleSmall.copy(
        color = titleTextColor
    )
    val headlineTextStyle = MaterialTheme.typography.headlineMedium.copy(
    color = titleTextColor
    )

    val iconSize = 25.dp
    val tintColor = MaterialTheme.colorScheme.primary

    val outlineColor = MaterialTheme.colorScheme.outlineVariant
    val cornerSize = 6.dp

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(horizontal = 12.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(cornerSize))
            .border(
                border = BorderStroke(2.dp, color = outlineColor),
                shape = RoundedCornerShape(cornerSize)
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Text(
            text = "Current set: ",
            style = titleTextStyle,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Icon(
            modifier = Modifier
                .size(iconSize)
                .clickable { decreaseCurrentEditedSet() },
            painter = painterResource(R.drawable.arrow_down),
            contentDescription = "Decrease weight",
            tint = tintColor
        )

        Text(
            text = currentSetIndex.toString(),
            style = headlineTextStyle,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Icon(
            modifier = Modifier
                .size(iconSize)
                .clickable { increaseCurrentEditedSet() },
            painter = painterResource(R.drawable.arrow_up),
            contentDescription = "Increase weight",
            tint = tintColor
        )
    }
}

@Preview
@Composable
private fun CurrentSetFooterPreview() {
    CurrentSetFooter(
        currentSetIndex = Random.nextInt(1, 10),
        increaseCurrentEditedSet = {},
        decreaseCurrentEditedSet = {}
    )
}