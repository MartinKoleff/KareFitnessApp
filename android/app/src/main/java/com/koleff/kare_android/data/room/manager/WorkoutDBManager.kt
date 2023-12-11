package com.koleff.kare_android.data.room.manager

import com.koleff.kare_android.common.preferences.Preferences
import com.koleff.kare_android.data.model.dto.MachineType
import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.data.room.dao.WorkoutDao
import com.koleff.kare_android.data.room.dao.WorkoutDetailsDao
import com.koleff.kare_android.data.room.entity.Exercise
import com.koleff.kare_android.data.room.entity.Workout
import com.koleff.kare_android.data.room.entity.WorkoutDetails
import com.koleff.kare_android.data.room.entity.relations.WorkoutDetailsExerciseCrossRef
import com.koleff.kare_android.data.room.entity.relations.WorkoutDetailsWithExercises
import com.koleff.kare_android.data.room.entity.relations.WorkoutDetailsWorkoutCrossRef
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WorkoutDBManager @Inject constructor(
    private val preferences: Preferences
) {
    val hasInitializedWorkoutTableRoomDB = preferences.hasInitializedWorkoutTableRoomDB()

    suspend fun initializeWorkoutTableRoomDB(
        workoutDao: WorkoutDao,
        workoutDetailsDao: WorkoutDetailsDao
    ) = withContext(Dispatchers.IO) {
        val workouts = getAllWorkouts()
        val workoutDetails = getAllWorkoutDetails()
        val exerciseCrossRefs = getAllExerciseCrossRefs()
        val workoutCrossRefs = getAllWorkoutCrossRefs()

        workoutDao.insertAll(workouts)
        workoutDetailsDao.insertAllDetails(workoutDetails.map(WorkoutDetailsWithExercises::workoutDetails))

        workoutDetailsDao.insertAllWorkoutDetailsExerciseCrossRef(exerciseCrossRefs)
        workoutDao.insertAllWorkoutDetailsWorkoutCrossRef(workoutCrossRefs)

        preferences.initializeWorkoutTableRoomDB()
    }

    private fun getAllWorkoutCrossRefs(): List<WorkoutDetailsWorkoutCrossRef> {
        return listOf(
            WorkoutDetailsWorkoutCrossRef(workoutDetailsId = 1, workoutId = 1),
            WorkoutDetailsWorkoutCrossRef(workoutDetailsId = 2, workoutId = 2),
            WorkoutDetailsWorkoutCrossRef(workoutDetailsId = 3, workoutId = 3)
        )
    }

    private fun getAllExerciseCrossRefs(): List<WorkoutDetailsExerciseCrossRef> {
        return listOf(
            WorkoutDetailsExerciseCrossRef(exerciseId = 3, workoutDetailsId = 1),
            WorkoutDetailsExerciseCrossRef(exerciseId = 4, workoutDetailsId = 1),
            WorkoutDetailsExerciseCrossRef(exerciseId = 5, workoutDetailsId = 1),
            WorkoutDetailsExerciseCrossRef(exerciseId = 6, workoutDetailsId = 1),
            WorkoutDetailsExerciseCrossRef(exerciseId = 7, workoutDetailsId = 1),
            WorkoutDetailsExerciseCrossRef(exerciseId = 11, workoutDetailsId = 2),
            WorkoutDetailsExerciseCrossRef(exerciseId = 12, workoutDetailsId = 2),
            WorkoutDetailsExerciseCrossRef(exerciseId = 13, workoutDetailsId = 2),
            WorkoutDetailsExerciseCrossRef(exerciseId = 14, workoutDetailsId = 2),
            WorkoutDetailsExerciseCrossRef(exerciseId = 32, workoutDetailsId = 3),
            WorkoutDetailsExerciseCrossRef(exerciseId = 33, workoutDetailsId = 3),
            WorkoutDetailsExerciseCrossRef(exerciseId = 34, workoutDetailsId = 3),
            WorkoutDetailsExerciseCrossRef(exerciseId = 35, workoutDetailsId = 3),
            WorkoutDetailsExerciseCrossRef(exerciseId = 36, workoutDetailsId = 3),
            WorkoutDetailsExerciseCrossRef(exerciseId = 37, workoutDetailsId = 3),
        )
    }

    private fun getAllWorkoutDetails(): List<WorkoutDetailsWithExercises> {
        return listOf(
            WorkoutDetailsWithExercises(
                workoutDetails = WorkoutDetails(
                    workoutDetailsId = 1,
                    name = "Arnold chest workout",
                    description = "Blow your chest",
                    muscleGroup = MuscleGroup.CHEST,
                    isSelected = false
                ),
                exercises = listOf(
                    Exercise(
                        3,
                        "Incline barbell bench press",
                        MuscleGroup.CHEST,
                        MachineType.BARBELL,
                        ""
                    ),
                    Exercise(
                        4,
                        "Incline dumbbell bench press",
                        MuscleGroup.CHEST,
                        MachineType.DUMBBELL,
                        ""
                    ),
                    Exercise(
                        5,
                        "Flat dumbbell bench press",
                        MuscleGroup.CHEST,
                        MachineType.DUMBBELL,
                        ""
                    ),
                    Exercise(
                        6,
                        "Pec deck fly",
                        MuscleGroup.CHEST,
                        MachineType.MACHINE,
                        ""
                    ),
                    Exercise(
                        7,
                        "Cable chest fly",
                        MuscleGroup.CHEST,
                        MachineType.MACHINE,
                        ""
                    )
                )
            ),
            WorkoutDetailsWithExercises(
                workoutDetails = WorkoutDetails(
                    workoutDetailsId = 2,
                    name = "Chavdo destroy back workout",
                    description = "Blow your back with me4ka",
                    muscleGroup = MuscleGroup.BACK,
                    isSelected = false
                ),
                exercises = listOf(
                    Exercise(
                        11,
                        "Seated cable rows",
                        MuscleGroup.BACK,
                        MachineType.MACHINE,
                        ""
                    ),
                    Exercise(
                        12,
                        "Lat pulldown (Wide grip)",
                        MuscleGroup.BACK,
                        MachineType.MACHINE,
                        ""
                    ),
                    Exercise(
                        13,
                        "Pull ups",
                        MuscleGroup.BACK,
                        MachineType.CALISTHENICS,
                        ""
                    ),
                    Exercise(
                        14,
                        "Bent over barbell row",
                        MuscleGroup.BACK,
                        MachineType.BARBELL,
                        ""
                    )
                )
            ),
            WorkoutDetailsWithExercises(
                workoutDetails = WorkoutDetails(
                    workoutDetailsId = 3,
                    name = "Blow your arms workout",
                    description = "Blow your arms with curls",
                    muscleGroup = MuscleGroup.ARMS,
                    isSelected = true
                ), exercises = listOf(
                    Exercise(
                        32,
                        "Standing dumbbell biceps curl",
                        MuscleGroup.BICEPS,
                        MachineType.DUMBBELL,
                        ""
                    ),
                    Exercise(
                        33,
                        "Sitting dumbbell biceps curl",
                        MuscleGroup.BICEPS,
                        MachineType.DUMBBELL,
                        ""
                    ),
                    Exercise(
                        34,
                        "Barbell biceps curl",
                        MuscleGroup.BICEPS,
                        MachineType.BARBELL,
                        ""
                    ),
                    Exercise(
                        35,
                        "Dumbbell concentrated curl",
                        MuscleGroup.BICEPS,
                        MachineType.DUMBBELL,
                        ""
                    ),
                    Exercise(
                        36,
                        "Dumbbell hammer curl",
                        MuscleGroup.BICEPS,
                        MachineType.DUMBBELL,
                        ""
                    ),  Exercise(
                        37,
                        "Dumbbell hammer curl",
                        MuscleGroup.BICEPS,
                        MachineType.DUMBBELL,
                        ""
                    )
                )
            )
        )
    }

    private fun getAllWorkouts(): List<Workout> {
        return listOf(
            Workout(
                workoutId = 1,
                name = "Arnold chest workout",
                muscleGroup = MuscleGroup.CHEST,
                snapshot = "",
                totalExercises = 5,
                isSelected = false
            ),
            Workout(
                workoutId = 2,
                name = "Chavdo destroy back workout",
                muscleGroup = MuscleGroup.BACK,
                snapshot = "",
                totalExercises = 4,
                isSelected = false
            ),
            Workout(
                workoutId = 3,
                name = "Blow your arms workout",
                muscleGroup = MuscleGroup.ARMS,
                snapshot = "",
                totalExercises = 6,
                isSelected = true
            )
        )
    }
}