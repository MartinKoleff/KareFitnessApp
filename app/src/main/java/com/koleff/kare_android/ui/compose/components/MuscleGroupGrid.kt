package com.koleff.kare_android.ui.compose.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.koleff.kare_android.data.model.dto.MuscleGroup

@Composable
fun MuscleGroupImageCard(
    painter: Painter,
    contentDescription: String,
    title: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val tintColor = MaterialTheme.colorScheme.scrim
    val textColor = MaterialTheme.colorScheme.onSurface
    val backgroundColor = MaterialTheme.colorScheme.tertiary

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(15.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        )
    ) {
        //Image first, then gradient over it, then text over the gradient...
        Column(modifier = Modifier
            .height(200.dp)
            .clickable { onClick.invoke() }
            .drawBehind {

                //Fixes white rectangle due to smaller images
                drawRect(
                    color = backgroundColor, //Adds background behind text box with contrast
                    size = this.size
                )
            }
        ) {
            Image(
                modifier = Modifier
                    .weight(5f)
                    .drawWithContent {
                        drawContent()

                        //Tint
                        drawRect(
                            brush = Brush.verticalGradient(
                                listOf(Color.Transparent, tintColor)
                            )
                        )
                    },
                painter = painter,
                contentDescription = contentDescription,
                contentScale = ContentScale.Crop
            )

            //Muscle group text positioned at the bottom
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(6.dp),
                contentAlignment = Alignment.BottomCenter
            ) {

                Text(
                    text = title,
                    style = TextStyle(
                        color = textColor,
                        fontSize = 16.sp,
                        fontStyle = FontStyle.Italic,
                    ),
                )
            }
        }
    }
}

@Composable
fun MuscleGroupGrid(
    modifier: Modifier = Modifier,
    muscleGroupList: List<MuscleGroup>,
    onMuscleGroupSelected: (Int) -> Unit
) {
    LazyVerticalStaggeredGrid(
        modifier = modifier,
        columns = StaggeredGridCells.Fixed(2),
        verticalItemSpacing = 2.dp,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        items(muscleGroupList.size) { currentMuscleGroupId ->
            val currentMuscleGroup = muscleGroupList[currentMuscleGroupId]

            MuscleGroupImageCard(
                painter = painterResource(id = currentMuscleGroup.imageId),
                contentDescription = currentMuscleGroup.description,
                title = currentMuscleGroup.muscleGroupName,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        if (currentMuscleGroupId == 1) PaddingValues(
                            top = 80.dp,
                            bottom = 8.dp,
                            start = 8.dp,
                            end = 8.dp
                        ) else PaddingValues(
                            all = 8.dp
                        )
                    )
            ) {
                onMuscleGroupSelected(currentMuscleGroupId)
            }
        }
    }
}

@Preview
@PreviewLightDark
@Composable
private fun MuscleGroupGridPreview() {
    MuscleGroupGrid(
        muscleGroupList = MuscleGroup.getSupportedMuscleGroups(),
        onMuscleGroupSelected = {

        }
    )
}