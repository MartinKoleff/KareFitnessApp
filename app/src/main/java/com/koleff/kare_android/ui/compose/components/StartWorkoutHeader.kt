package com.koleff.kare_android.ui.compose.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.koleff.kare_android.R
import com.koleff.kare_android.ui.compose.components.navigation_components.NavigationItem
import kotlin.random.Random


@Composable
fun StartWorkoutButton(
    text: String,
    onStartWorkoutAction: () -> Unit,
) {
    val cornerSize = 24.dp
    val paddingValues = PaddingValues(
        horizontal = 32.dp,
        vertical = 8.dp
    )

    val backgroundColor = MaterialTheme.colorScheme.tertiary
    val textColor = MaterialTheme.colorScheme.onSurface
    val outlineColor = MaterialTheme.colorScheme.outlineVariant

    val textStyle = MaterialTheme.typography.headlineSmall.copy(
        color = textColor
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(75.dp)
            .padding(paddingValues)
            .clip(RoundedCornerShape(cornerSize))
            .border(
                border = BorderStroke(2.dp, color = outlineColor),
                shape = RoundedCornerShape(cornerSize)
            )
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(cornerSize)
            )
            .clickable(onClick = { onStartWorkoutAction() }),
        contentAlignment = Alignment.Center
    ) {

        //Sign in text
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

@Composable
fun StartWorkoutHeader(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    isWorkoutFavorited: Boolean,
    onStartWorkoutAction: () -> Unit,
    onConfigureAction: () -> Unit,
    onAddExerciseAction: () -> Unit,
    onDeleteWorkoutAction: () -> Unit,
    onEditWorkoutNameAction: () -> Unit,
    onFavoriteWorkoutAction: () -> Unit,
    onUnfavoriteWorkoutAction: () -> Unit,
    onNavigateBackAction: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    Box(modifier = modifier) {
//        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
//            StartWorkoutToolbar(onNavigateBackAction, onNavigateToSettings)
//        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            StartWorkoutTitleAndSubtitle(title, subtitle)

            StartWorkoutActionRow(
                isWorkoutFavorited = isWorkoutFavorited,
                onConfigureAction = onConfigureAction,
                onAddExerciseAction = onAddExerciseAction,
                onDeleteWorkoutAction = onDeleteWorkoutAction,
                onEditWorkoutNameAction = onEditWorkoutNameAction,
                onFavoriteWorkoutAction = onFavoriteWorkoutAction,
                onUnfavoriteWorkoutAction = onUnfavoriteWorkoutAction
            )

            StartWorkoutButton(text = "Start workout!", onStartWorkoutAction = onStartWorkoutAction)
        }
    }
}

@Composable
fun StartWorkoutToolbar(
    onNavigateBackAction: () -> Unit,
    onNavigateToSettings: () -> Unit,
    toolbarHeight: Dp = 65.dp
) {
    val cornerSize = 24.dp
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    val circleOutlineColor = MaterialTheme.colorScheme.outlineVariant
    val tintColor = MaterialTheme.colorScheme.onSurface

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(toolbarHeight),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        NavigationItem(
            modifier = Modifier
                .drawBehind {
                    drawCircle(
                        color = circleOutlineColor,
                        radius = this.size.maxDimension / 3
                    )
                },
            icon = Icons.AutoMirrored.Filled.ArrowBackIos,
            label = "Go back",
            onNavigateAction = onNavigateBackAction,
            tint = tintColor
        )

        NavigationItem(
            modifier = Modifier
                .drawBehind {
                    drawCircle(
                        color = circleOutlineColor,
                        radius = this.size.maxDimension / 3
                    )
                },
            icon = Icons.Filled.Settings,
            label = "Settings",
            onNavigateAction = onNavigateToSettings,
            tint = tintColor
        )
    }
}

@Preview
@Composable
fun StartWorkoutToolbarPreview() {
    StartWorkoutToolbar(
        onNavigateBackAction = {},
        onNavigateToSettings = {}
    )
}

@Composable
fun StartWorkoutActionRow(
    isWorkoutFavorited: Boolean,
    onConfigureAction: () -> Unit,
    onAddExerciseAction: () -> Unit,
    onDeleteWorkoutAction: () -> Unit,
    onEditWorkoutNameAction: () -> Unit,
    onFavoriteWorkoutAction: () -> Unit,
    onUnfavoriteWorkoutAction: () -> Unit,
) {

    //Same as StartWorkoutButton
    val paddingValues = PaddingValues(
        horizontal = 32.dp
    )
    val actionColumnHeight = 150.dp

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(actionColumnHeight)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(actionColumnHeight / 2)
                .padding(paddingValues),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            StartWorkoutActionButton(
                modifier = Modifier.weight(1.2f),
                text = "Edit workout name",
                iconResourceId = R.drawable.ic_edit,
                onAction = onEditWorkoutNameAction
            )

            StartWorkoutActionButton(
                modifier = Modifier.weight(1.1f),
                text = "Configure workout",
                iconResourceId = R.drawable.ic_vector_settings,
                onAction = onConfigureAction
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(actionColumnHeight / 2)
                .padding(paddingValues),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            StartWorkoutActionButton(
                modifier = Modifier.weight(1f),
                text = "Add exercise",
                iconResourceId = R.drawable.ic_vector_add,
                onAction = onAddExerciseAction
            )


            StartWorkoutDynamicActionButton(
                modifier = Modifier.weight(1f),
                isWorkoutFavorited = isWorkoutFavorited,
                initialText = "Favorite",
                changedText = "Favorited",
                initialIconResourceId = R.drawable.ic_heart_outline,
                changedIconResourceId = R.drawable.ic_heart_full,
                onFavoriteWorkoutAction = onFavoriteWorkoutAction,
                onUnfavoriteWorkoutAction = onUnfavoriteWorkoutAction
            )

            StartWorkoutActionButton(
                modifier = Modifier.weight(1f),
                text = "Delete workout",
                iconResourceId = R.drawable.ic_delete,
                onAction = onDeleteWorkoutAction
            )
        }
    }
}

@Composable
fun StartWorkoutActionButton(
    modifier: Modifier = Modifier,
    text: String,
    iconResourceId: Int,
    onAction: () -> Unit
) {
    val textColor = MaterialTheme.colorScheme.onSurface
    val tintColor = MaterialTheme.colorScheme.onSurface
    val iconSize = 30.dp
    val paddingValues = PaddingValues(2.dp)

    val textStyle = MaterialTheme.typography.labelLarge.copy(
        color = textColor
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        //Image
        IconButton(
            modifier = Modifier
                .size(iconSize)
                .padding(paddingValues),
            onClick = {
                onAction()
            }) {

            Icon(
                painter = painterResource(id = iconResourceId),
                contentDescription = "Start workout action button",
                tint = tintColor
            )
        }
        //Text
        Text(
            modifier = Modifier.padding(paddingValues),
            text = text,
            style = textStyle,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun StartWorkoutDynamicActionButton(
    modifier: Modifier = Modifier,
    isWorkoutFavorited: Boolean,
    initialText: String,
    changedText: String,
    initialIconResourceId: Int,
    changedIconResourceId: Int,
    onFavoriteWorkoutAction: () -> Unit = {},
    onUnfavoriteWorkoutAction: () -> Unit = {},
) {
    var isWorkoutFavorited by remember {
        mutableStateOf(isWorkoutFavorited)
    }

    val textColor = MaterialTheme.colorScheme.onSurface
    val tintColor = MaterialTheme.colorScheme.onSurface

    val textStyle = MaterialTheme.typography.bodyMedium.copy(
        color = textColor
    )

    val iconSize = 30.dp
    val paddingValues = 2.dp

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        //Image
        IconButton(
            modifier = Modifier.size(iconSize),
            onClick = {
//                isSaved = !isSaved

                if(isWorkoutFavorited) onUnfavoriteWorkoutAction() else onFavoriteWorkoutAction()
            }) {
            Icon(
                painter = painterResource(id = if (isWorkoutFavorited) changedIconResourceId else initialIconResourceId),
                contentDescription = "Start workout action button",
                tint = tintColor
            )
        }

        //Text
        Text(
            modifier = Modifier.padding(
                paddingValues
            ),
            text = if (isWorkoutFavorited) changedText else initialText,
            style = textStyle,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview
@Composable
fun StartWorkoutActionButtonPreview() {
    StartWorkoutActionButton(text = "Save", iconResourceId = R.drawable.ic_heart_outline) {

    }
}

@Preview
@Composable
fun StartWorkoutActionRowPreview() {
    val onAddExerciseAction = {}
    val onConfigureAction = {}
    val onDeleteWorkoutAction = {}
    val onEditWorkoutNameAction = {}
    val onFavoriteWorkoutAction = {}
    val onUnfavoriteWorkoutAction = {}
    val isWorkoutFavorited = Random.nextBoolean()

    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
    ) {
        StartWorkoutActionRow(
            isWorkoutFavorited = isWorkoutFavorited,
            onAddExerciseAction = onAddExerciseAction,
            onConfigureAction = onConfigureAction,
            onDeleteWorkoutAction = onDeleteWorkoutAction,
            onEditWorkoutNameAction = onEditWorkoutNameAction,
            onFavoriteWorkoutAction = onFavoriteWorkoutAction,
            onUnfavoriteWorkoutAction = onUnfavoriteWorkoutAction
        )
    }

}

@Composable
fun StartWorkoutTitleAndSubtitle(
    title: String,
    subtitle: String
) {
    val titleTextColor = MaterialTheme.colorScheme.onSurface
    val titleTextStyle = MaterialTheme.typography.displayMedium.copy(
        color = titleTextColor
    )

    val subtitleTextColor = MaterialTheme.colorScheme.onSurface
    val subtitleTextStyle = MaterialTheme.typography.headlineSmall.copy(
        color = subtitleTextColor
    )

    val titlePadding =
        PaddingValues(
            top = 8.dp,
            bottom = 0.dp,
            start = 32.dp,
            end = 32.dp
        )

    val subtitlePadding =
        PaddingValues(
            top = 2.dp,
            bottom = 64.dp,
            start = 32.dp,
            end = 32.dp
        )

    //Title
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.padding(
                titlePadding
            ),
            text = title,
            style = titleTextStyle,
            maxLines = 4,
            overflow = TextOverflow.Ellipsis
        )
    }

    //Subtitle
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.padding(
                subtitlePadding
            ),
            text = subtitle,
            style = subtitleTextStyle,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview
@Composable
fun StartWorkoutHeaderPreview() {
    val onStartWorkoutAction = {}
    val onConfigureAction = {}
    val onAddExerciseAction = {}
    val onEditWorkoutNameAction = {}
    val onDeleteWorkoutAction = {}
    val onNavigateBackAction = {}
    val onFavoriteWorkoutAction = {}
    val onUnfavoriteWorkoutAction = {}
    val onNavigateToSettings = {}
    val isWorkoutFavorited = Random.nextBoolean()

    StartWorkoutHeader(
        modifier = Modifier.fillMaxSize(),
        title = "Arnold destroy back workout",
        subtitle = "Biceps, triceps and forearms.",
        isWorkoutFavorited = isWorkoutFavorited,
        onStartWorkoutAction = onStartWorkoutAction,
        onConfigureAction = onConfigureAction,
        onAddExerciseAction = onAddExerciseAction,
        onEditWorkoutNameAction = onEditWorkoutNameAction,
        onDeleteWorkoutAction = onDeleteWorkoutAction,
        onFavoriteWorkoutAction = onFavoriteWorkoutAction,
        onUnfavoriteWorkoutAction = onUnfavoriteWorkoutAction,
        onNavigateBackAction = onNavigateBackAction,
        onNavigateToSettings = onNavigateToSettings
    )
}

//TODO: Image/Video background...
