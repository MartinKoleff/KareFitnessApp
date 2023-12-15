package com.koleff.kare_android.ui.compose.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.koleff.kare_android.common.MockupDataGenerator
import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.model.event.OnSearchEvent
import com.koleff.kare_android.data.model.event.OnWorkoutDetailsEvent
import com.koleff.kare_android.ui.compose.LoadingWheel
import com.koleff.kare_android.ui.compose.SearchExercisesList
import com.koleff.kare_android.ui.compose.scaffolds.SearchListScaffold
import com.koleff.kare_android.ui.view_model.ExerciseListViewModel
import com.koleff.kare_android.ui.view_model.WorkoutDetailsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchExercisesScreen(
    navController: NavHostController,
    isNavigationInProgress: MutableState<Boolean>,
    workoutId: Int,
    exercisesListViewModel: ExerciseListViewModel
) {
    SearchListScaffold("Select exercise", navController, isNavigationInProgress) { innerPadding ->
        val modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()

        val exercisesState by exercisesListViewModel.state.collectAsState()
        val allExercises = exercisesState.exerciseList

        val searchState by exercisesListViewModel.searchState.collectAsState()
        val isSearching = searchState.isSearching
        val searchText = searchState.searchText

        //All exercises
        if (exercisesState.isLoading) {
            LoadingWheel()
        } else {
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
            ) {

//                //Material3 Search bar
//                SearchBar(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(16.dp),
//                    query = searchText,
//                    onQueryChange = exercisesListViewModel.onSearchEvent(OnSearchEvent.OnToggleSearch()),
//                    onSearch = exercisesListViewModel.onSearchEvent(OnSearchEvent.OnSearchTextChange(searchText)),
//                    active = isSearching,
//                    onActiveChange = { exercisesListViewModel.onSearchEvent(OnSearchEvent.OnToggleSearch()) }
//                ) { }
                    SearchExercisesList(
                        modifier =  Modifier
                            .fillMaxSize(),
                        exerciseList = allExercises,
                        workoutId = workoutId,
                        navController = navController
                    )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun SearchExercisesScreenPreview() {
    val navController = rememberNavController()
    SearchListScaffold("Select exercise", navController, mutableStateOf(false)) { innerPadding ->
        val modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()


        val allExercises = MockupDataGenerator.generateExerciseList()

        Column(modifier = modifier) {
            SearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                query = "",
                onQueryChange = {},
                onSearch = {},
                active = false,
                onActiveChange = { }
            ) {}

            SearchExercisesList(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 5.dp),
                exerciseList = allExercises,
                workoutId = 1,
                navController = navController
            )
        }
    }
}

