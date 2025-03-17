package com.koleff.kare_android.ui.event

import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.model.dto.MuscleGroup

sealed class OnFilterExerciseEvent {
    class DumbbellFilter(val exercises: List<ExerciseDto>) : OnFilterExerciseEvent()
    class BarbellFilter(val exercises: List<ExerciseDto>) : OnFilterExerciseEvent()
    class MachineFilter(val exercises: List<ExerciseDto>) : OnFilterExerciseEvent()
    class CalisthenicsFilter(val exercises: List<ExerciseDto>) : OnFilterExerciseEvent()
    class NoFilter(val exercises: List<ExerciseDto>) : OnFilterExerciseEvent()
    class MuscleGroupFilter(val exercises: List<ExerciseDto>, val muscleGroup: MuscleGroup) : OnFilterExerciseEvent()
}
