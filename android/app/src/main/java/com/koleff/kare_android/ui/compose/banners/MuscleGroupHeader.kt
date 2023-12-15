package com.koleff.kare_android.ui.compose.banners

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.koleff.kare_android.R
import com.koleff.kare_android.data.model.dto.MuscleGroup

@Composable
fun MuscleGroupHeader(currentMuscleGroup: MuscleGroup) {
    val primaryContainerColor = MaterialTheme.colorScheme.primaryContainer

    HorizontalDivider(thickness = 5.dp)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(35.dp),
        contentAlignment = Alignment.Center
    ) {
        //Parallax effect overflowing into exercise snapshot
        Image(
            painter = painterResource(R.drawable.ic_search_header_background_effect),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.TopStart)
                .graphicsLayer { alpha = 0.75f },
            colorFilter = ColorFilter.tint(primaryContainerColor, BlendMode.Darken)
        )

        Text(
            text = currentMuscleGroup.name,
            style = TextStyle(
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }

    HorizontalDivider(thickness = 5.dp)
}

@Preview
@Composable
fun MuscleGroupHeaderPreview() {
    val muscleGroup = MuscleGroup.BACK

    MuscleGroupHeader(currentMuscleGroup = muscleGroup)
}