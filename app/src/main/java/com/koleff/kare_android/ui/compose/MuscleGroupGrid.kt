package com.koleff.kare_android.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.koleff.kare_android.ui.MainScreen
import com.koleff.kare_android.data.model.dto.MuscleGroupUI

@Composable
fun ImageCard(
    painter: Painter,
    contentDescription: String,
    title: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(15.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        )
    ) {
        //Image first, then gradient over it, then text over the gradient...
        Box(modifier = Modifier
            .height(200.dp)
            .clickable { onClick.invoke() }
        ) {
            Image(
                painter = painter,
                contentDescription = contentDescription,
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(
                        brush = Brush.verticalGradient(
                            listOf(Color.Transparent, Color.Black)
                        )
                    )
            )

            //Have the text in a box positioned at bottom start
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(12.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                Text(
                    text = title,
                    style = TextStyle(
                        color = Color.White,
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
    navController: NavHostController,
    muscleGroupList: List<MuscleGroupUI>
) {
    LazyVerticalStaggeredGrid(
        modifier = modifier,
        columns = StaggeredGridCells.Fixed(2),
        verticalItemSpacing = 2.dp,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        items(muscleGroupList.size) { currentMuscleGroupId ->
            val currentMuscleGroup = muscleGroupList[currentMuscleGroupId]

            ImageCard(
                painter = painterResource(id = currentMuscleGroup.imageId),
                contentDescription = currentMuscleGroup.description,
                title = currentMuscleGroup.name,
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
                openMuscleGroupScreen(currentMuscleGroupId, navController)
            }
        }
    }
}

fun openMuscleGroupScreen(currentMuscleGroupId: Int, navController: NavHostController) {
    navController.navigate(MainScreen.MuscleGroupExercisesList.createRoute(currentMuscleGroupId))
}