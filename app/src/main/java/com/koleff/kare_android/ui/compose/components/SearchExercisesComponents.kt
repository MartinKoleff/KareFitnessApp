package com.koleff.kare_android.ui.compose.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.koleff.kare_android.data.model.dto.MuscleGroup

@Composable
fun MuscleGroupFilterGrid(
    modifier: Modifier = Modifier,
    muscleGroupList: List<MuscleGroup>,
    onMuscleGroupSelected: (MuscleGroup) -> Unit
) {
    val selectedMuscleGroup by remember { mutableStateOf<MuscleGroup?>(null) }

    LazyVerticalGrid(
        modifier = modifier.padding(8.dp),
        columns = GridCells.Fixed(3),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        items(muscleGroupList.size) { currentMuscleGroupId ->
            val currentMuscleGroup = muscleGroupList[currentMuscleGroupId]
            val backgroundColor =
                if (selectedMuscleGroup == currentMuscleGroup) Color.Gray else Color.LightGray

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable { onMuscleGroupSelected(currentMuscleGroup) },
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(16.dp)) {
                    Text(currentMuscleGroup.muscleGroupName, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Preview
@Composable
private fun MuscleGroupFilterGridPreview() {
    MuscleGroupFilterGrid(
        muscleGroupList = MuscleGroup.getSupportedMuscleGroups(),
        onMuscleGroupSelected = {
        }
    )
}


@Composable
fun MuscleGroupFilterButtonGrid(
    muscleGroups: List<MuscleGroup>,
    onClick: (MuscleGroup) -> Unit
) {
    var selectedFilter by rememberSaveable { mutableStateOf(MuscleGroup.ALL) }

    val labelColor = MaterialTheme.colorScheme.onSurface
    val labelTextStyle = MaterialTheme.typography.bodySmall.copy(
        color = labelColor
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        muscleGroups.chunked(3).forEach { rowFilters ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                rowFilters.forEach { filter ->
                    FilterChip(
                        selected = selectedFilter == filter,
                        onClick = {
                            if (selectedFilter == filter)
                                selectedFilter = MuscleGroup.ALL
                            else
                                selectedFilter = filter

                            onClick(selectedFilter)
                        },
                        label = {
                            Text(
                                text = filter.muscleGroupName,
                                style = labelTextStyle,
                            )
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun MuscleGroupFilterButtonGridPreview() {
    MuscleGroupFilterButtonGrid(
        muscleGroups = MuscleGroup.getSupportedMuscleGroups(),
        onClick = {}
    )
}

//@Composable
//fun MuscleGroupFilterGrid2(
//    modifier: Modifier = Modifier,
//    muscleGroupList: List<MuscleGroup>,
//    onMuscleGroupSelected: (Int) -> Unit
//) {
//    var selectedMuscle by remember { mutableStateOf<MuscleGroup?>(null) }
//
//    LazyVerticalStaggeredGrid(
//        modifier = modifier,
//        columns = StaggeredGridCells.Fixed(3),
//        verticalItemSpacing = 2.dp,
//        horizontalArrangement = Arrangement.spacedBy(2.dp),
//    ) {
//        items(muscleGroupList.size) { currentMuscleGroupId ->
//            val currentMuscleGroup = muscleGroupList[currentMuscleGroupId]
//            val backgroundColor =
//                if (selectedMuscle == currentMuscleGroup) Color.Gray else Color.LightGray
//
//            val padding = if(currentMuscleGroupId % 4 == 0)
//                PaddingValues(start = 80.dp, top = 8.dp, end = 8.dp, bottom = 8.dp)
//            else PaddingValues(all = 8.dp)
//
//            Card(
//                modifier = Modifier
//                    .padding(8.dp)
//                    .clickable { onMuscleGroupSelected(currentMuscleGroupId) },
//                shape = RoundedCornerShape(8.dp),
//                colors = CardDefaults.cardColors(
//                    containerColor = backgroundColor
//                )
//            ) {
//                Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(16.dp)) {
//                    Text(currentMuscleGroup.muscleGroupName, fontWeight = FontWeight.Bold)
//                }
//            }
//        }
//    }
//}
//
//@Preview
//@Composable
//private fun MuscleGroupFilterGridPreview2() {
//    MuscleGroupFilterGrid2(
//        muscleGroupList = MuscleGroup.getSupportedMuscleGroups().plus(MuscleGroup.ARMS),
//        onMuscleGroupSelected = {
//
//        }
//    )
//}