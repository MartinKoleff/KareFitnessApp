package com.koleff.kare_android.workout.data

import com.koleff.kare_android.data.room.dao.WorkoutDetailsDao
import com.koleff.kare_android.data.room.dao.WorkoutDetailsId
import com.koleff.kare_android.data.room.entity.Exercise
import com.koleff.kare_android.data.room.entity.WorkoutDetails
import com.koleff.kare_android.data.room.entity.WorkoutConfiguration
import com.koleff.kare_android.data.room.entity.WorkoutDetailsWithExercises
import com.koleff.kare_android.exercise.data.ExerciseSetChangeListener

class WorkoutDetailsDaoFakeV2 : WorkoutDetailsDao, WorkoutConfigurationChangeListener, ExerciseChangeListener,
    WorkoutDetailsChangeListener {

    private val workoutDetailsDB = mutableListOf<WorkoutDetailsWithExercises>()
    private lateinit var compositeExerciseSetChangeListener: CompositeExerciseSetChangeListener

    fun setExerciseSetChangeListeners(compositeExerciseSetChangeListener: CompositeExerciseSetChangeListener) {
        this.compositeExerciseSetChangeListener = compositeExerciseSetChangeListener
    }

    private val isInternalLogging = false
    companion object {
        private const val TAG = "WorkoutDetailsDaoFakeV2"
    }

    override suspend fun insertWorkoutDetails(workoutDetails: WorkoutDetails): WorkoutDetailsId {
        workoutDetailsDB.add(
            WorkoutDetailsWithExercises(
                workoutDetails = workoutDetails,
                exercises = emptyList(), //Initially workout has no exercises...
                configuration = null //Initially workout has no configuration...
            )
        )

        return workoutDetails.workoutDetailsId.toLong()
    }

    override suspend fun insertAllWorkoutDetails(workoutDetailsList: List<WorkoutDetails>) {
        workoutDetailsList.forEach { workoutDetails ->
            insertWorkoutDetails(workoutDetails)
        }
    }

    override suspend fun deleteWorkoutDetails(workoutId: Int) {
        workoutDetailsDB.removeAll {
            it.workoutDetails.workoutDetailsId == workoutId
        }
    }

    //Only update WorkoutDetails and keep the same exercises
    override suspend fun updateWorkoutDetails(workout: WorkoutDetails) {

        //Find workout
        val index = workoutDetailsDB.map {
            it.workoutDetails
        }.indexOfFirst { it.workoutDetailsId == workout.workoutDetailsId }

        //WorkoutDetails found
        if (index != -1) {
            val exercises = workoutDetailsDB[index].exercises
            val configuration = workoutDetailsDB[index].configuration

            //Replace workout
            workoutDetailsDB[index] = WorkoutDetailsWithExercises(
                workoutDetails = workout,
                exercises = exercises,
                configuration = configuration
            )
        } else {

            //No workout found
        }
    }

    override suspend fun selectWorkoutDetailsById(workoutId: Int) {
        workoutDetailsDB.map {
            it.workoutDetails
        }.forEach {
            it.isSelected = it.workoutDetailsId == workoutId
        }
    }

    override fun getWorkoutDetailsOrderedById(): List<WorkoutDetailsWithExercises> {
        return workoutDetailsDB.sortedBy { it.workoutDetails.workoutDetailsId }
    }

    override fun getWorkoutByIsSelected(): WorkoutDetailsWithExercises? {
        return workoutDetailsDB.firstOrNull {
            it.workoutDetails.isSelected
        }
    }

    override fun getWorkoutDetailsById(workoutId: Int): WorkoutDetailsWithExercises? {
        return workoutDetailsDB.firstOrNull {
            it.workoutDetails.workoutDetailsId == workoutId
        }
    }

    override fun onWorkoutConfigurationUpdated(configuration: WorkoutConfiguration) {
        val index = workoutDetailsDB.map {
            it.workoutDetails
        }.indexOfFirst { it.workoutDetailsId == configuration.workoutId }

        //WorkoutConfiguration found
        if (index != -1) {
            workoutDetailsDB[index] = workoutDetailsDB[index].copy(configuration = configuration)
        } else {

            //No workout found for workout configuration
        }
    }

    override fun onWorkoutConfigurationDeleted(workoutId: Int) {
        val index = workoutDetailsDB.map {
            it.workoutDetails
        }.indexOfFirst { it.workoutDetailsId == workoutId }

        //WorkoutConfiguration found
        if (index != -1) {
            workoutDetailsDB[index] = workoutDetailsDB[index].copy(configuration = null)
        } else {

            //No workout found for workout configuration
        }
    }

    override fun onExerciseAdded(exercise: Exercise) {
        val index = workoutDetailsDB.indexOfFirst {
            it.workoutDetails.workoutDetailsId == exercise.workoutId
        }

        //WorkoutDetails found
        if (index != -1) {
            val updatedExercises = workoutDetailsDB[index].exercises?.toMutableList() ?: return
            updatedExercises.add(exercise)

            workoutDetailsDB[index] = workoutDetailsDB[index].copy(exercises = updatedExercises)
        }
    }

    override fun onExerciseUpdated(exercise: Exercise) {
        val index = workoutDetailsDB.indexOfFirst {
            it.workoutDetails.workoutDetailsId == exercise.workoutId
        }

        //WorkoutDetails found
        if (index != -1) {
            val updatedExercises = workoutDetailsDB[index].exercises?.toMutableList() ?: return

            val exerciseIndex = updatedExercises.indexOfFirst {
                it.exerciseId == exercise.exerciseId
            }

            if (exerciseIndex != -1) {
                updatedExercises[exerciseIndex] = exercise
                workoutDetailsDB[index] = workoutDetailsDB[index].copy(exercises = updatedExercises)
            }
        }
    }

    override suspend fun onExerciseDeleted(exercise: Exercise) {
        val index = workoutDetailsDB.indexOfFirst {
            it.workoutDetails.workoutDetailsId == exercise.workoutId
        }

        //WorkoutDetails found
        if (index != -1) {
            compositeExerciseSetChangeListener.onSetsDeleted(exercise.exerciseId, exercise.workoutId).also {
                val updatedExercises = workoutDetailsDB[index].exercises?.toMutableList() ?: return
                updatedExercises.removeAll { it.exerciseId == exercise.exerciseId }

                workoutDetailsDB[index] = workoutDetailsDB[index].copy(exercises = updatedExercises)
            }
        }
    }

    override suspend fun onExercisesDeleted(workoutId: Int) {
        val exercisesToDelete = workoutDetailsDB
            .filter { it.workoutDetails.workoutDetailsId == workoutId }
            .map { it.exercises }
            .firstOrNull() ?: emptyList()

        exercisesToDelete.forEach { exercise ->
            onExerciseDeleted(exercise)
        }
    }

    fun clearDB() {
        workoutDetailsDB.clear()
    }

    override fun onWorkoutDetailsDeleted(workoutId: Int) {
        workoutDetailsDB.removeAll { it.workoutDetails.workoutDetailsId == workoutId }
    }
}