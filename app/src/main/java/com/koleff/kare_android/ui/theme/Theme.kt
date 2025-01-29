package com.koleff.kare_android.ui.theme

import android.app.Activity
import android.content.Context
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.koleff.kare_android.R
import com.koleff.kare_android.common.ColorManager.colorFromResource

@Composable
fun lightColorScheme(context: Context) = lightColorScheme(
    primary = colorFromResource(context, R.color.primary),
    secondary = colorFromResource(context, R.color.secondary),
    tertiary = colorFromResource(context, R.color.tertiary),
    background = colorFromResource(context, R.color.surface_light),
    surface = colorFromResource(context, R.color.surface_light),
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = colorFromResource(context, R.color.title_light),
    onSurface = colorFromResource(context, R.color.subtitle_light)
)

@Composable
fun darkColorScheme(context: Context) = darkColorScheme(
    primary = colorFromResource(context, R.color.primary),
    secondary = colorFromResource(context, R.color.secondary),
    tertiary = colorFromResource(context, R.color.tertiary),
    background = colorFromResource(context, R.color.surface_dark),
    surface = colorFromResource(context, R.color.surface_dark),
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = colorFromResource(context, R.color.title_dark),
    onSurface = colorFromResource(context, R.color.subtitle_dark),
)


@Composable
fun KareTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,  //Dynamic color is available on Android 12+
    content: @Composable() () -> Unit
) {
    val context = LocalContext.current

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> darkColorScheme(context)
        else -> lightColorScheme(context)
    }

    val extendedColors = TextColorScheme(
        title = colorFromResource(
            context,
            if (darkTheme) R.color.title_dark else R.color.title_light
        ),
        subtitle = colorFromResource(
            context,
            if (darkTheme) R.color.subtitle_dark else R.color.subtitle_light
        ),
        label = colorFromResource(
            context,
            if (darkTheme) R.color.label_dark else R.color.label_light
        )
    )

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
    ) {
        CompositionLocalProvider(LocalExtendedColors provides extendedColors) {
            content()
        }
    }
}


//    //Custom colors
//    val extendedColorScheme = ExtendedColorScheme(
//        doWorkoutColors = DoWorkoutColors(),
//        workoutBannerColors = WorkoutBannerColors(
//            deleteButtonColor = MaterialTheme.colorScheme.tertiary,
//            selectButtonColor = Color.Blue,
//            editButtonColor = Color.Green
//        ),
//        detailsScreenBackgroundGradient = if (darkTheme) {
//            listOf(
//                MaterialTheme.colorScheme.inversePrimary,
//                MaterialTheme.colorScheme.inversePrimary,
//                MaterialTheme.colorScheme.inversePrimary,
//                MaterialTheme.colorScheme.tertiary,
//                MaterialTheme.colorScheme.tertiary,
//                MaterialTheme.colorScheme.tertiary,
//            )
//        } else {
//            listOf(
//                MaterialTheme.colorScheme.primary,
//                MaterialTheme.colorScheme.primary,
//                MaterialTheme.colorScheme.primary,
//                MaterialTheme.colorScheme.tertiary,
//                MaterialTheme.colorScheme.tertiary,
//                MaterialTheme.colorScheme.tertiary,
//            )
//        },
//        authenticationScreenBackgroundGradient = listOf(
//            Color.Red,
//            Color.Red,
//            Color.Black,
//            Color.Blue
//        ),
//        detailsToolbarColor = if (darkTheme) {
//            MaterialTheme.colorScheme.inversePrimary
//        } else {
//            MaterialTheme.colorScheme.primary
//        }
//    )
//
//    MaterialTheme(
//        colorScheme = colorScheme,
//        typography = Typography,
//    ) {
//        CompositionLocalProvider(LocalExtendedColorScheme provides extendedColorScheme) {
//            content()
//        }
//    }

//@Immutable
//data class ColorFamily(
//    val color: Color,
//    val onColor: Color,
//    val colorContainer: Color,
//    val onColorContainer: Color
//)
//
//val unspecified_scheme = ColorFamily(
//    Color.Unspecified, Color.Unspecified, Color.Unspecified, Color.Unspecified
//)