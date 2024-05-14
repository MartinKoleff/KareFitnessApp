package com.koleff.kare_android.ui.event

import com.koleff.kare_android.data.model.dto.ExerciseDto

sealed class OnExerciseUpdateEvent(val exercise: ExerciseDto) {
    class OnExerciseSubmit(exercise: ExerciseDto) : OnExerciseUpdateEvent(exercise) //Same as edit
    class OnExerciseDelete(exercise: ExerciseDto) : OnExerciseUpdateEvent(exercise)
}

sealed class OnMultipleExercisesUpdateEvent(val exerciseList: List<ExerciseDto>) {
    class OnMultipleExercisesSubmit(exerciseList: List<ExerciseDto>) : OnMultipleExercisesUpdateEvent(exerciseList)
    class OnMultipleExercisesDelete(exerciseList: List<ExerciseDto>) : OnMultipleExercisesUpdateEvent(exerciseList)
}