import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.koleff.kare_android.R
import com.koleff.kare_android.ui.compose.navigation.Toolbar


@Composable
fun ExerciseDetailsToolbar(
    modifier: Modifier,
    navController: NavHostController,
    isNavigationInProgress: MutableState<Boolean>,
    exerciseImageId: Int
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val primaryContainerColor = MaterialTheme.colorScheme.primaryContainer
    val configuration = LocalConfiguration.current

    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp
    Column(
        modifier = modifier
    ) {
        Toolbar(
            navController = navController,
            hasTitle = false,
            isNavigationInProgress = isNavigationInProgress
        )

        Image(
            painter = painterResource(id = exerciseImageId),
            contentDescription = "Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(screenHeight / 2.5f)
                .graphicsLayer { alpha = 0.80f } //TODO: add opacity / 0.66f alpha works
                .drawWithContent {

                    val colors = listOf(
                        primaryColor,
                        primaryColor,
                        primaryContainerColor,
                        Color.Transparent,
                        Color.Transparent,
                        Color.Transparent
                    )
                    drawContent()
                    drawRect(
                        brush = Brush.verticalGradient(colors),
                        blendMode = BlendMode.Overlay
                    )
                }
                .clip(RoundedToolbarShape())
        )
    }
}

class RoundedToolbarShape : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Generic(
            // Draw your custom path here
            path = drawRoundedToolbarSquare(size)
        )
    }

    private fun drawRoundedToolbarSquare(size: Size): Path {
        val screenWidth = size.width
        val screenHeight = size.height
        val cornerRadius = screenWidth / 3

        val rect = Rect(
            left = screenWidth - 2 * cornerRadius,
            top = screenHeight - 2 * cornerRadius,
            right = screenWidth,
            bottom = screenHeight
        )

        val path = Path().apply {
            moveTo(0f, 0f)
            lineTo(screenWidth, 0f)
            lineTo(screenWidth, screenHeight - cornerRadius)
            arcTo(
                rect = rect,
                startAngleDegrees = 0f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )
            lineTo(cornerRadius, screenHeight)
            arcTo(
                rect = Rect(
                    left = 0f,
                    top = screenHeight - 2 * cornerRadius,
                    right = cornerRadius * 2,
                    bottom = screenHeight
                ),
                startAngleDegrees = 90f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )
            close()
        }

        return path
    }
}


@Preview
@Composable
fun PreviewRoundedSquare() {
    val navController = rememberNavController()

    ExerciseDetailsToolbar(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primaryContainer,
                        MaterialTheme.colorScheme.primaryContainer,
                        MaterialTheme.colorScheme.primaryContainer,
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.primary
                    )
                )
            ),
        isNavigationInProgress = mutableStateOf(false),
        navController = navController,
        exerciseImageId = R.drawable.ic_chest
    )
}




