package com.koleff.kare_android.ui.event

import com.koleff.kare_android.data.model.dto.ExerciseDto

sealed class OnExerciseUpdateEvent(val exercise: ExerciseDto) {
    class OnExerciseSubmit(exercise: ExerciseDto) : OnExerciseUpdateEvent(exercise) //Same as edit
    class OnExerciseDelete(exercise: ExerciseDto) : OnExerciseUpdateEvent(exercise)
}
