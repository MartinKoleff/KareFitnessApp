package com.koleff.kare_android.ui.compose.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.koleff.kare_android.data.model.dto.ExerciseDto


@Composable
fun SelectedExercisesRow(selectedExercises: List<ExerciseDto>) {
    val backgroundColor = MaterialTheme.colorScheme.tertiary
    val outlineColor = MaterialTheme.colorScheme.outlineVariant

    val textColor = MaterialTheme.colorScheme.onSurface
    val textStyle = MaterialTheme.typography.titleMedium.copy(
        color = textColor
    )

    val cornerSize = 16.dp

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(cornerSize))
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(cornerSize)
            )
            .border(
                border = BorderStroke(2.dp, color = outlineColor),
                shape = RoundedCornerShape(cornerSize)
            ),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "${selectedExercises.size} exercises selected.",
            style = textStyle,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun UpdateExercisesRow(
    modifier: Modifier = Modifier,
    text: String,
    backgroundColor: Color,
    onAction: () -> Unit
) {
    val outlineColor = MaterialTheme.colorScheme.outlineVariant

    val textColor = MaterialTheme.colorScheme.onSurface
    val textStyle = MaterialTheme.typography.titleMedium.copy(
        color = textColor
    )

    val cornerSize = 16.dp

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .clip(RoundedCornerShape(cornerSize))
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(cornerSize)
            )
            .border(
                border = BorderStroke(2.dp, color = outlineColor),
                shape = RoundedCornerShape(cornerSize)
            )
            .clickable { onAction() },
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = textStyle,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun SubmitExercisesRow(modifier: Modifier = Modifier, onSubmit: () -> Unit) {
    UpdateExercisesRow(
        modifier = modifier,
        text = "Add exercises",
        backgroundColor = MaterialTheme.colorScheme.tertiary,
        onAction = onSubmit
    )
}

@Composable
fun DeleteExercisesRow(modifier: Modifier = Modifier, onDelete: () -> Unit) {
    UpdateExercisesRow(
        modifier = modifier,
        text = "Delete exercises",
        backgroundColor = MaterialTheme.colorScheme.tertiary,
        onAction = onDelete
    )
}

@Preview
@PreviewLightDark
@Composable
private fun SelectedExercisesRowPreview() {
    val selectedExercises = listOf<ExerciseDto>()

    SelectedExercisesRow(selectedExercises)
}

@Preview
@PreviewLightDark
@Composable
private fun DeleteExercisesRowPreview() {
    DeleteExercisesRow() {

    }
}

@Preview
@PreviewLightDark
@Composable
private fun SubmitExercisesRowPreview() {
    SubmitExercisesRow() {

    }
}
