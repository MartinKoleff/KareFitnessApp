package com.koleff.kare_android.ui.compose.components

import android.net.Uri
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.koleff.kare_android.R

@Composable
fun WelcomeVideoPlayer() {
    val context = LocalContext.current
    val videoUri = "android.resource://${context.packageName}/${R.raw.login_video_no_watermark}"
    DownloadedVideoPlayer(videoUri)
}

@Composable
fun WorkoutVideoPlayer(videoId: Int) {
    val context = LocalContext.current
    val videoRaw = if (videoId == -1) return
    else R.raw.login_video_no_watermark
    
    val videoUri = "android.resource://${context.packageName}/${videoRaw}"
    DownloadedVideoPlayer(videoUri)
}

@OptIn(UnstableApi::class)
@Composable
fun DownloadedVideoPlayer(videoUri: String) {
    val context = LocalContext.current

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
