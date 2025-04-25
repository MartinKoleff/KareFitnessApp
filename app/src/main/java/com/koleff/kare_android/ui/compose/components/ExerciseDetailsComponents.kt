package com.koleff.kare_android.ui.compose.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import com.koleff.kare_android.R
import com.koleff.kare_android.common.MockupDataGeneratorV2
import com.koleff.kare_android.data.model.dto.ExerciseDetailsDto
import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.ui.theme.LocalExtendedColors
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun DetailsInfo(
    modifier: Modifier = Modifier.height(45.dp),
    title: String,
    subtitle: String,
    imageResource: Int?
) {
    val cornerSize = 4.dp

    val outlineColor = MaterialTheme.colorScheme.outlineVariant
    val backgroundColor = MaterialTheme.colorScheme.surface
    val labelTextColor = LocalExtendedColors.current.label
    val labelTextStyle = MaterialTheme.typography.labelSmall.copy(
        color = labelTextColor
    )

    val titleTextColor = LocalExtendedColors.current.title
    val titleTextStyle = MaterialTheme.typography.bodySmall.copy(
        color = titleTextColor
    )
    val tintColor = MaterialTheme.colorScheme.primary
    val iconSize = 20.dp

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(cornerSize))
            .border(
                border = BorderStroke(2.dp, color = outlineColor),
                shape = RoundedCornerShape(cornerSize)
            )
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(cornerSize)
            )
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        imageResource?.let {
            Icon(
                modifier = Modifier
                    .padding(4.dp)
                    .size(iconSize),
                painter = painterResource(imageResource),
                contentDescription = title,
                tint = tintColor
            )
        }

        Column(
            modifier = Modifier.padding(horizontal = 4.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                modifier = Modifier.padding(
                    PaddingValues(horizontal = 4.dp)
                ),
                text = title,
                style = titleTextStyle,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                modifier = Modifier.padding(
                    PaddingValues(horizontal = 4.dp)
                ),
                text = subtitle,
                style = labelTextStyle,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun DifficultyInfo(modifier: Modifier = Modifier) {
    DetailsInfo(modifier, "Difficulty", "Intermediate", R.drawable.weight)
}

@Preview
@Composable
private fun DifficultyInfoPreview() {
    DifficultyInfo()
}

@Composable
fun DurationInfo(modifier: Modifier = Modifier) {
    DetailsInfo(modifier, "Duration", "10-15 min", R.drawable.timer)
}

@Preview
@Composable
private fun DurationInfoPreview() {
    DurationInfo()
}

@Composable
fun EquipmentNeededInfo(modifier: Modifier = Modifier) {
    DetailsInfo(modifier, "Equipment needed", "Barbell, dumbbells", null)
}

@Preview
@Composable
private fun EquipmentNeededInfoPreview() {
    EquipmentNeededInfo()
}

@Preview
@Composable
private fun InfoRowPreview() {
    Row {
        DifficultyInfo(
            modifier = Modifier
                .weight(3f)
                .padding(horizontal = 2.dp)
        )
        DurationInfo(
            modifier = Modifier
                .weight(3f)
                .padding(horizontal = 2.dp)
        )
        EquipmentNeededInfo(
            modifier = Modifier
                .weight(3f)
                .padding(horizontal = 2.dp)
        )
    }
}

@Composable
fun Gauge(
    modifier: Modifier = Modifier,
    percentage: Float,
    primaryColor: Color,
    backgroundColor: Color,
    label: String,
    title: String
) {
    val sweepAngle = (percentage / 100) * 180f
    val labelColor = LocalExtendedColors.current.label
    val labelTextStyle = MaterialTheme.typography.labelMedium.copy(
        color = labelColor
    )

    val titleTextColor = LocalExtendedColors.current.title
    val titleTextStyle = MaterialTheme.typography.titleSmall.copy(
        color = titleTextColor
    )
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = titleTextStyle,
            modifier = Modifier
                .alpha(0.3f)
                .padding(bottom = 6.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Canvas(
            modifier = modifier.size(75.dp)
        ) {
            val strokeWidth = 10.dp.toPx()
            val arcSize = size.copy(width = size.width, height = size.height) //* 4/3

            drawArc(
                size = arcSize,
                color = backgroundColor,
                startAngle = 180f,
                sweepAngle = 180f,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            drawArc(
                size = arcSize,
                color = primaryColor,
                startAngle = 180f,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            val radius = size.width / 2
            val angleInRadians = Math.toRadians((180 + sweepAngle).toDouble())
            val arrowLength = radius * 0.6f
            val arrowStartX = radius
            val arrowStartY = radius
            val arrowEndX = (radius + arrowLength * cos(angleInRadians)).toFloat()
            val arrowEndY = (radius + arrowLength * sin(angleInRadians)).toFloat()

            // Draw Arrow Line
            drawLine(
                color = primaryColor,
                start = Offset(arrowStartX, arrowStartY),
                end = Offset(arrowEndX, arrowEndY),
                strokeWidth = 3.dp.toPx(),
                cap = StrokeCap.Round
            )

            drawCircle(
                color = primaryColor,
                radius = 3.dp.toPx(),
                center = Offset(arrowStartX, arrowStartY)
            )
        }

        // Label (Primary Muscles)
        Text(
            text = label,
            style = labelTextStyle,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview
@Composable
fun GaugePreview() {
    Gauge(
        modifier = Modifier.size(75.dp),
        percentage = 33f,
        title = "Chest",
        label = "Primary muscles",
        primaryColor = MaterialTheme.colorScheme.primary,
        backgroundColor = MaterialTheme.colorScheme.inversePrimary
    )
}

@Composable
fun MuscleWorkedInfo(modifier: Modifier, percentage: Float, exercise: ExerciseDetailsDto, label: String = "Primary muscles") {
    val outlineColor = MaterialTheme.colorScheme.outlineVariant
    val cornerSize = 4.dp

    Box(
        modifier = modifier
            .size(125.dp)
            .clip(
                RoundedCornerShape(cornerSize)
            )
            .border(
                border = BorderStroke(2.dp, color = outlineColor),
                shape = RoundedCornerShape(cornerSize)
            ),
        contentAlignment = Alignment.Center
    ) {
        Gauge(
            percentage = percentage,
            title = exercise.muscleGroup.muscleGroupName,
            label = label,
            primaryColor = MaterialTheme.colorScheme.primary,
            backgroundColor = MaterialTheme.colorScheme.inversePrimary
        )
    }
}

@Preview
@Composable
private fun MuscleWorkedInfoPreview() {
    MuscleWorkedInfo(
        modifier = Modifier.size(100.dp),
        percentage = 50f,
        exercise = MockupDataGeneratorV2.generateExerciseDetails(),
        label = "Secondary muscles"
    )
}

@Preview
@Composable
private fun MusclesWorkedRowPreview() {
    val exercise = MockupDataGeneratorV2.generateExerciseDetails(muscleGroup = MuscleGroup.ARMS)

    Column {
        Text(
            modifier = Modifier.padding(vertical = 6.dp, horizontal = 2.dp),
            text = "Muscles worked",
            style = MaterialTheme.typography.labelMedium.copy(
                color = LocalExtendedColors.current.title
            ),
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Row(horizontalArrangement = Arrangement.SpaceEvenly) {
            MuscleWorkedInfo(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 2.dp),
                percentage = 33f,
                exercise = exercise,
                label = "Primary muscle"
            )
            MuscleWorkedInfo(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 2.dp),
                percentage = 50f,
                exercise = exercise,
                label = "Secondary muscle"
            )
            MuscleWorkedInfo(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 2.dp),
                percentage = 75f,
                exercise = exercise,
                label = "Secondary muscle"
            )
        }
    }
}

@Composable
fun DescriptionBox(
    modifier: Modifier = Modifier,
    title: String = "Description:",
    hasTitle: Boolean = true,
    description: String,
    hasDivider: Boolean = true
) {
    val subtitleTextColor = LocalExtendedColors.current.title
    val subtitleTextStyle = MaterialTheme.typography.titleMedium.copy(
        color = subtitleTextColor
    )

    val labelTextColor = LocalExtendedColors.current.label
    val labelTextStyle = MaterialTheme.typography.labelMedium.copy(
        color = labelTextColor
    )

    val tintColor = LocalExtendedColors.current.title

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start
    ) {
        if(hasTitle) {
            Text(
                modifier = Modifier
                    .padding(8.dp),
                text = title,
                style = subtitleTextStyle,
                textAlign = TextAlign.Start,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Text(
            modifier = Modifier
                .padding(8.dp),
            text = description,
            style = labelTextStyle,
            textAlign = TextAlign.Start,
            maxLines = 5,
            overflow = TextOverflow.Ellipsis
        )
    }

    if(hasDivider) {
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 8.dp),
            color = tintColor,
            thickness = 1.dp
        )
    }
}

@Preview
@Composable
private fun DescriptionBoxPreview() {
    DescriptionBox(description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc interdum nibh nec pharetra iaculis. Aenean ultricies egestas leo at ultricies.")
}

@Composable
fun AddToWorkoutButton(modifier: Modifier = Modifier, text: String = "+ Add to workout", onClick: () -> Unit) {
    FooterButton(
        modifier = modifier,
        text = text,
        onClick = onClick
    )
}

@Preview
@Composable
private fun AddToWorkoutButton() {
    AddToWorkoutButton {

    }
}

@Composable
fun YoutubeVideoPlayer(
    youtubeVideoId: String,
    lifecycleOwner: LifecycleOwner
) {
    val outlineColor = MaterialTheme.colorScheme.outlineVariant
    val cornerSize = 16.dp

    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(
                border = BorderStroke(5.dp, color = outlineColor),
                shape = RoundedCornerShape(cornerSize)
            )
            .clip(RoundedCornerShape(cornerSize)),
        factory = { context ->
            YouTubePlayerView(context = context).apply {
                lifecycleOwner.lifecycle.addObserver(this)

                addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                    override fun onReady(youTubePlayer: YouTubePlayer) {
                        youTubePlayer.loadVideo(youtubeVideoId, 0f)
                    }
                })
            }
        })
}

@Preview
@Composable
private fun YoutubeVideoPlayerPreview() {
    YoutubeVideoPlayer(
        youtubeVideoId = "dQw4w9WgXcQ",
        lifecycleOwner = LocalLifecycleOwner.current
    )
}