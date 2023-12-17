package com.koleff.kare_android.ui.event

import com.koleff.kare_android.data.model.dto.ExerciseDto

sealed class OnWorkoutDetailsEvent(val exercise: ExerciseDto) {
    class OnExerciseSubmit(exercise: ExerciseDto) : OnWorkoutDetailsEvent(exercise) //Same as edit
    class OnExerciseDelete(exercise: ExerciseDto) : OnWorkoutDetailsEvent(exercise)
}
