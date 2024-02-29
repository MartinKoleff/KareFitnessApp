package com.koleff.kare_android.ui.compose.screen

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.ui.StyledPlayerView

@Composable
fun LoginScreen() {
    val onLogin = {}
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Color.Red
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
    ) {

        LoginVideoPlayer(
            context = context,
            videoUri = ""

        )

        //Footer
        LoginFooter(onLogin = onLogin)
    }
}

@Composable
fun LoginVideoPlayer(context: Context, videoUri: String) {
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            //Prepare the player with the source.
            val mediaItem = MediaItem.fromUri(Uri.parse(videoUri))
            setMediaItem(mediaItem)
            prepare()
            playWhenReady = true //Start playing as soon as the player is ready
            repeatMode = ExoPlayer.REPEAT_MODE_ONE //Loop the video
        }
    }

    //Dispose the player when it's no longer needed
    DisposableEffect(exoPlayer) {
        onDispose {
            exoPlayer.release()
        }
    }

    AndroidView(
        factory = { videoContext ->
            StyledPlayerView(videoContext).apply {
                player = exoPlayer as Player
                useController = false //Hide player controls for background playback
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
fun LoginFooter(onLogin: () -> Unit) {
    val configuration = LocalConfiguration.current

    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    val cornerSize = 24.dp
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(screenHeight / 3)
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        Color.Transparent,
                        Color.Gray,
                        Color.Gray,
                        Color.Gray
                    )
                )
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
    ) {


        //Login button
        LoginButton(onLogin)

        //Sign in text hyperlink below login button
        Text( //TODO: and cooler font...
            modifier = Modifier.padding(
                PaddingValues(8.dp)
            ),
            text = "Don't have an account? Sign in.",
            style = TextStyle(
                color = Color.Blue,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        //Margin for all components
        Spacer(Modifier.height(75.dp))
    }
}

@Composable
fun LoginButton(onLogin: () -> Unit) {
    val cornerSize = 24.dp

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp)
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
            .clickable(onClick = onLogin),
        contentAlignment = Alignment.Center
    ) {

        //Login text
        Text( //TODO: and cooler font...
            modifier = Modifier.padding(
                PaddingValues(8.dp)
            ),
            text = "Login",
            style = TextStyle(
                color = Color.Black,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}


@Preview("Login Screen")
@Composable
fun LoginScreenPreview() {
    LoginScreen()
}