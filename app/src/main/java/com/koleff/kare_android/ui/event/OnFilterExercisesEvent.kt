package com.koleff.kare_android.ui.event

import com.koleff.kare_android.data.model.dto.ExerciseDto

sealed class OnFilterExercisesEvent{
    class DumbbellFilter(val exercises: List<ExerciseDto>) : OnFilterExercisesEvent()
    class BarbellFilter(val exercises: List<ExerciseDto>) : OnFilterExercisesEvent()
    class MachineFilter(val exercises: List<ExerciseDto>) : OnFilterExercisesEvent()
    class CalisthenicsFilter(val exercises: List<ExerciseDto>) : OnFilterExercisesEvent()
    class NoFilter(val exercises: List<ExerciseDto>) : OnFilterExercisesEvent()
}
