import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.koleff.kare_android.R
import com.koleff.kare_android.common.MockupDataGeneratorV2
import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.data.model.dto.WorkoutDetailsDto
import com.koleff.kare_android.ui.compose.components.BannerImage
import com.koleff.kare_android.ui.theme.LocalExtendedColors

@Composable
fun WorkoutConfigurationHeader(
    workout: WorkoutDetailsDto,
    onConfigure: () -> Unit,
    onDeleteWorkout: () -> Unit,
    onEditWorkoutName: () -> Unit,
    onFavoriteWorkout: () -> Unit,
    onUnfavoriteWorkout: () -> Unit,
    onAddExercise: () -> Unit,
    onStartWorkout: () -> Unit,
    showImage: Boolean = true
) {
    val titleTextColor = LocalExtendedColors.current.title
    val titleTextStyle = MaterialTheme.typography.titleMedium.copy(
        color = titleTextColor
    )

    val subtitleTextColor = LocalExtendedColors.current.subtitle
    val subtitleTextStyle = MaterialTheme.typography.bodyMedium.copy(
        color = subtitleTextColor
    )

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, end = 12.dp, bottom = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            //Image
            if(showImage) {
                BannerImage(
                    modifier = Modifier
                        .size(100.dp),
                    imageResource = MuscleGroup.getImage(workout.muscleGroup),
                    showPlayerIcon = false
                )
            }

            Column(
                modifier = Modifier.padding(start = 6.dp, end = 6.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = workout.name,
                    style = titleTextStyle,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "Configure your workout here.",
                    style = subtitleTextStyle,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                WorkoutConfigurationRow(
                    isWorkoutFavorited = workout.isFavorite,
                    onConfigure = onConfigure,
                    onDeleteWorkout = onDeleteWorkout,
                    onEditWorkoutName = onEditWorkoutName,
                    onFavoriteWorkout = onFavoriteWorkout,
                    onUnfavoriteWorkout = onUnfavoriteWorkout,
                    onAddExercise = onAddExercise,
                    onStartWorkout = onStartWorkout
                )
            }
        }

        HorizontalDivider(
            modifier = Modifier
                .padding(horizontal = 12.dp),
            thickness = 1.dp
        )
    }
}

@Preview
@Composable
private fun WorkoutConfigurationHeaderPreview() {
    WorkoutConfigurationHeader(
        workout = MockupDataGeneratorV2.generateWorkoutDetails(), //name = "Tren twins special -> Destroy your arms workout"
        onConfigure = {},
        onDeleteWorkout = {},
        onEditWorkoutName = {},
        onFavoriteWorkout = {},
        onUnfavoriteWorkout = {},
        onAddExercise = {},
        onStartWorkout = {}
    )
}

@Composable
fun WorkoutConfigurationRow(
    isWorkoutFavorited: Boolean,
    onConfigure: () -> Unit,
    onDeleteWorkout: () -> Unit,
    onEditWorkoutName: () -> Unit,
    onFavoriteWorkout: () -> Unit,
    onUnfavoriteWorkout: () -> Unit,
    onAddExercise: () -> Unit,
    onStartWorkout: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        WorkoutConfigurationOption(
            imageResource = R.drawable.edit,
            onClick = onEditWorkoutName
        )

        WorkoutConfigurationOption(
            imageResource = if (isWorkoutFavorited) R.drawable.ic_heart_full else R.drawable.heart,
            onClick = if (isWorkoutFavorited) onUnfavoriteWorkout else onFavoriteWorkout
        )

        WorkoutConfigurationOption(
            imageResource = R.drawable.setting,
            onClick = onConfigure
        )

        WorkoutConfigurationOption(
            imageResource = R.drawable.trash,
            onClick = onDeleteWorkout
        )

        WorkoutConfigurationOption(
            imageResource = R.drawable.ic_vector_add,
            onClick = onAddExercise
        )

        WorkoutConfigurationOption(
            imageResource = R.drawable.ic_player,
            onClick = onStartWorkout
        )
    }
}

@Preview
@Composable
private fun WorkoutConfigurationRowPreview() {
    WorkoutConfigurationRow(
        isWorkoutFavorited = false,
        onConfigure = {},
        onDeleteWorkout = {},
        onEditWorkoutName = {},
        onFavoriteWorkout = {},
        onUnfavoriteWorkout = {},
        onAddExercise = {},
        onStartWorkout = {}
    )
}


//TODO: move to common and rename to ConfigurationOptionButton...
@Composable
fun WorkoutConfigurationOption(imageResource: Int, onClick: () -> Unit) {
    val outlineColor = MaterialTheme.colorScheme.outlineVariant
    val iconSize = 20.dp

    val tintColor = MaterialTheme.colorScheme.primary

    Box(
        modifier = Modifier
            .padding(4.dp)
            .size(35.dp)
            .clip(CircleShape)
            .border(
                border = BorderStroke(2.dp, color = outlineColor),
                shape = CircleShape
            )
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier.size(iconSize),
            painter = painterResource(imageResource),
            contentDescription = "Workout configuration option",
        )
    }
}

@Preview
@Composable
private fun WorkoutConfigurationOptionPreview() {
    WorkoutConfigurationOption(imageResource = R.drawable.weight) { }
}
