package com.koleff.kare_android.ui.compose.screen

import android.net.Uri
import androidx.annotation.OptIn
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.koleff.kare_android.R
import com.koleff.kare_android.common.navigation.Destination
import com.koleff.kare_android.common.navigation.NavigationEvent
import com.koleff.kare_android.ui.view_model.WelcomeViewModel

@Composable
fun WelcomeScreen(welcomeViewModel: WelcomeViewModel = hiltViewModel()) {
    val navigateToLogin = {
        welcomeViewModel.navigateToLogin()
    }
    val navigateToRegister = {
        welcomeViewModel.navigateToRegister()
    }
    val context = LocalContext.current

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {

        //Video player
        WelcomeVideoPlayer()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {

        //Login and sign in footer
        WelcomeFooter(onLogin = navigateToLogin, onRegister = navigateToRegister)
    }
}

@OptIn(UnstableApi::class)
@Composable
fun WelcomeVideoPlayer() {
    val configuration = LocalConfiguration.current

    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    val context = LocalContext.current
    val videoUri = "android.resource://${context.packageName}/${R.raw.login_video_no_watermark}"
    val exoPlayer = remember {

        //Prepare the player with the source.
        ExoPlayer.Builder(context).build().apply {
            val mediaItem = MediaItem.fromUri(Uri.parse(videoUri))
            setMediaItem(mediaItem)
            prepare()
            playWhenReady = true //Start playing as soon as the player is ready
            repeatMode = ExoPlayer.REPEAT_MODE_ONE //Loop the video
        }
    }

    //Clear the exo player on dispose
    DisposableEffect(exoPlayer) {
        onDispose {
            exoPlayer.release()
        }
    }

    //Video player UI
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { ctx ->
            PlayerView(ctx).apply {
                player = exoPlayer
                useController = false //Hide player controls for background playback
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
            }
        }
    )
}


@Composable
fun WelcomeFooter(onLogin: () -> Unit, onRegister: () -> Unit) {
    val configuration = LocalConfiguration.current

    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    val sizeModifier = Modifier
        .fillMaxWidth()
        .height(screenHeight / 2)

    //Background
    Box(modifier = sizeModifier) {

        //Texture background
        Image(
            painter = painterResource(id = R.drawable.metal_texture_2),
            contentDescription = "Metal texture background",
            modifier = Modifier
                .alpha(0.75f)
                .drawWithContent {

                    val colors = listOf(
                        Color.Transparent,
                        Color.Transparent,
                        Color.Transparent,
                        Color.Gray,
                        Color.Gray,
                        Color.Gray
                    )
                    drawContent()
                    drawRect(
                        brush = Brush.verticalGradient(colors),
                        blendMode = BlendMode.DstIn
                    )
                },
            contentScale = ContentScale.Crop
        )
    }

    //Footer
    Column(
        modifier = sizeModifier
            .alpha(0.75f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
    ) {

        //Logo
        Image(
            modifier = Modifier
                .size(175.dp),
            painter = painterResource(R.drawable.logo),
            contentDescription = "Logo image",
            contentScale = ContentScale.Crop
        )

        //Login button
        LoginButton(onLogin)

        //Register/ Sign up button
        RegisterButton(onRegister)
    }
}

/**
 * Currently unused
 */
@Composable
fun LogoRow() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        //Welcome to
        Text( //TODO: and cooler font...
            modifier = Modifier.padding(
                PaddingValues(8.dp)
            ),
            text = "Welcome to",
            style = TextStyle(
                color = MaterialTheme.colorScheme.primary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        //Logo
        Image(
            modifier = Modifier
                .size(175.dp),
            painter = painterResource(R.drawable.logo),
            contentDescription = "Logo image",
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun RegisterButton(onRegister: () -> Unit) {
    WideRoundButton("Register", onRegister, hasBottomPadding = true)
}

@Composable
fun LoginButton(onLogin: () -> Unit) {
    WideRoundButton("Login", onLogin, hasTopPadding = false)
}

@Composable
fun WideRoundButton(
    text: String,
    callback: () -> Unit,
    hasBottomPadding: Boolean = false,
    hasTopPadding: Boolean = true
) {
    val cornerSize = 24.dp

    val paddingValues = PaddingValues(
        start = 16.dp,
        end = 16.dp,
        top = if (hasTopPadding) 8.dp else 0.dp,
        bottom = if (hasBottomPadding) 24.dp else 8.dp
    )

    val textColor = Color.Black //MaterialTheme.colorScheme.primary

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingValues)
            .height(50.dp)
            .clip(RoundedCornerShape(cornerSize))
            .border(
                border = BorderStroke(2.dp, color = Color.Black),
                shape = RoundedCornerShape(cornerSize)
            )
            .background(
                color = Color.White,
                shape = RoundedCornerShape(cornerSize)
            )
            .clickable(onClick = callback),
        contentAlignment = Alignment.Center
    ) {

        //Texture background
        Image(
            painter = painterResource(id = R.drawable.metal_texture_3),
            contentDescription = "Metal texture background",
            modifier = Modifier.alpha(0.85f),
            contentScale = ContentScale.Crop
        )

        //Login text
        Text( //TODO: and cooler font...
            modifier = Modifier.padding(
                PaddingValues(8.dp)
            ),
            text = text,
            style = TextStyle(
                color = textColor,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}


@Preview()
@Composable
fun WelcomeFooterPreview() {
    WelcomeFooter(
        onLogin = {},
        onRegister = {}
    )
}