package com.koleff.kare_android.workout.data

import com.koleff.kare_android.data.room.dao.WorkoutDetailsDao
import com.koleff.kare_android.data.room.dao.WorkoutDetailsId
import com.koleff.kare_android.data.room.entity.Exercise
import com.koleff.kare_android.data.room.entity.ExerciseSet
import com.koleff.kare_android.data.room.entity.ExerciseWithSets
import com.koleff.kare_android.data.room.entity.WorkoutDetails
import com.koleff.kare_android.data.room.entity.WorkoutConfiguration
import com.koleff.kare_android.data.room.entity.WorkoutDetailsWithExercises
import com.koleff.kare_android.exercise.data.ExerciseSetChangeListener
import com.koleff.kare_android.utils.FakeDao

class WorkoutDetailsDaoFakeV2 : WorkoutDetailsDao, WorkoutConfigurationChangeListener,
    ExerciseChangeListener, ExerciseSetChangeListener, WorkoutDetailsChangeListener, FakeDao {

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

    override suspend fun favoriteWorkoutDetailsById(workoutId: Int) {
        val index = workoutDetailsDB.map {
            it.workoutDetails
        }.indexOfFirst { it.workoutDetailsId == workoutId }

        //WorkoutDetails found
        if (index != -1) {

            val updatedWorkoutDetails = workoutDetailsDB[index].workoutDetails.copy(isFavorite = true)
            workoutDetailsDB[index] = workoutDetailsDB[index].copy(workoutDetails = updatedWorkoutDetails)
        } else {

            //No WorkoutDetails found
        }

//        workoutDetailsDB
//            .map { it.workoutDetails }
//            .filter { it.workoutDetailsId == workoutId }
//            .map { it.isFavorite = true }

    }

    override suspend fun unfavoriteWorkoutDetailsById(workoutId: Int) {
        val index = workoutDetailsDB.map {
            it.workoutDetails
        }.indexOfFirst { it.workoutDetailsId == workoutId }

        //WorkoutDetails found
        if (index != -1) {

            val updatedWorkoutDetails = workoutDetailsDB[index].workoutDetails.copy(isFavorite = false)
            workoutDetailsDB[index] = workoutDetailsDB[index].copy(workoutDetails = updatedWorkoutDetails)
        } else {

            //No WorkoutDetails found
        }
    }


    override fun getWorkoutDetailsOrderedById(): List<WorkoutDetailsWithExercises> {
        return workoutDetailsDB.sortedBy { it.workoutDetails.workoutDetailsId }
    }

    override fun getWorkoutByIsFavorite(): WorkoutDetailsWithExercises? {
        return workoutDetailsDB.firstOrNull {
            it.workoutDetails.isFavorite
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
            updatedExercises.add(
                ExerciseWithSets(
                    exercise = exercise,
                    sets = emptyList() //To be wired onSetAdded...
                )
            )

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
                it.exercise.exerciseId == exercise.exerciseId &&
                        it.exercise.workoutId == exercise.workoutId
            }

            if (exerciseIndex != -1) {
                updatedExercises[exerciseIndex] =
                    ExerciseWithSets(
                        exercise = exercise,
                        sets = updatedExercises[exerciseIndex].sets
                    )
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
            compositeExerciseSetChangeListener.onSetsDeleted(
                exercise.exerciseId,
                exercise.workoutId
            ).also {
                val updatedExercises = workoutDetailsDB[index].exercises?.toMutableList() ?: return
                updatedExercises.removeAll {
                    it.exercise.exerciseId == exercise.exerciseId
                            && it.exercise.workoutId == exercise.workoutId
                }

                workoutDetailsDB[index] = workoutDetailsDB[index].copy(exercises = updatedExercises)
            }
        }
    }

    override suspend fun onExercisesDeleted(workoutId: Int) {
        val exercisesToDelete = workoutDetailsDB
            .filter { it.workoutDetails.workoutDetailsId == workoutId }
            .map { it.exercises }
            .firstOrNull() ?: emptyList()

        exercisesToDelete.forEach { exerciseWithSets ->
            onExerciseDeleted(exerciseWithSets.exercise)
        }
    }

    override fun onWorkoutDetailsDeleted(workoutId: Int) {
        workoutDetailsDB.removeAll { it.workoutDetails.workoutDetailsId == workoutId }
    }


    override suspend fun onSetAdded(exerciseSet: ExerciseSet) {
        val workoutPosition = workoutDetailsDB.indexOfFirst {
            it.workoutDetails.workoutDetailsId == exerciseSet.workoutId
        }

        //Workout found
        if (workoutPosition != -1) {
            try {
                val updatedExercises = workoutDetailsDB[workoutPosition].exercises?.toMutableList()
                    ?: throw IllegalArgumentException("Exercises not found for workout with id ${workoutDetailsDB[workoutPosition].workoutDetails.workoutDetailsId}")

                val exercisePosition = updatedExercises.indexOfFirst {
                    it.exercise.exerciseId == exerciseSet.exerciseId &&
                            it.exercise.workoutId == exerciseSet.workoutId
                }

                //Exercise found
                if (exercisePosition != -1) {
                    val exerciseWithSets = updatedExercises[exercisePosition]

                    val updatedSets = exerciseWithSets.sets.toMutableList()
                    updatedSets.add(exerciseSet)

                    val updatedExercise = ExerciseWithSets(
                        exercise = exerciseWithSets.exercise,
                        sets = updatedSets
                    )
                    updatedExercises.removeAt(exercisePosition)
                    updatedExercises.add(exercisePosition, updatedExercise)

                    val updatedWorkout = WorkoutDetailsWithExercises(
                        workoutDetails = workoutDetailsDB[workoutPosition].workoutDetails,
                        exercises = updatedExercises,
                        configuration = workoutDetailsDB[workoutPosition].configuration
                    )
                    workoutDetailsDB[workoutPosition] = updatedWorkout
                } else {
                    throw NoSuchElementException("Exercise not found for workout with id ${workoutDetailsDB[workoutPosition].workoutDetails.workoutDetailsId}")
                }
            } catch (e: NoSuchElementException) {
                return
            } catch (e: IllegalArgumentException) {
                return
            }
        }
    }

    override suspend fun onSetUpdated(exerciseSet: ExerciseSet) {
        val workoutPosition = workoutDetailsDB.indexOfFirst {
            it.workoutDetails.workoutDetailsId == exerciseSet.workoutId
        }

        //Workout found
        if (workoutPosition != -1) {
            try {
                val updatedExercises = workoutDetailsDB[workoutPosition].exercises?.toMutableList()
                    ?: throw IllegalArgumentException("Exercises not found for workout with id ${workoutDetailsDB[workoutPosition].workoutDetails.workoutDetailsId}")

                val exercisePosition = updatedExercises.indexOfFirst {
                    it.exercise.exerciseId == exerciseSet.exerciseId &&
                            it.exercise.workoutId == exerciseSet.workoutId
                }

                //Exercise found
                if (exercisePosition != -1) {
                    val exerciseWithSets = updatedExercises[exercisePosition]

                    val updatedSets = exerciseWithSets.sets.toMutableList()
                    updatedSets.removeAll { it.setId == exerciseSet.setId }
                    updatedSets.add(exerciseSet)

                    val updatedExercise = ExerciseWithSets(
                        exercise = exerciseWithSets.exercise,
                        sets = updatedSets
                    )
                    updatedExercises.removeAt(exercisePosition)
                    updatedExercises.add(exercisePosition, updatedExercise)

                    val updatedWorkout = WorkoutDetailsWithExercises(
                        workoutDetails = workoutDetailsDB[workoutPosition].workoutDetails,
                        exercises = updatedExercises,
                        configuration = workoutDetailsDB[workoutPosition].configuration
                    )
                    workoutDetailsDB[workoutPosition] = updatedWorkout
                } else {
                    throw NoSuchElementException("Exercise not found for workout with id ${workoutDetailsDB[workoutPosition].workoutDetails.workoutDetailsId}")
                }
            } catch (e: NoSuchElementException) {
                return
            } catch (e: IllegalArgumentException) {
                return
            }
        }
    }

    override suspend fun onSetDeleted(exerciseSet: ExerciseSet) {
        val workoutPosition = workoutDetailsDB.indexOfFirst {
            it.workoutDetails.workoutDetailsId == exerciseSet.workoutId
        }

        //Workout found
        if (workoutPosition != -1) {
            try {
                val updatedExercises = workoutDetailsDB[workoutPosition].exercises?.toMutableList()
                    ?: throw IllegalArgumentException("Exercises not found for workout with id ${workoutDetailsDB[workoutPosition].workoutDetails.workoutDetailsId}")

                val exercisePosition = updatedExercises.indexOfFirst {
                    it.exercise.exerciseId == exerciseSet.exerciseId &&
                            it.exercise.workoutId == exerciseSet.workoutId
                }

                //Exercise found
                if (exercisePosition != -1) {
                    val exerciseWithSets = updatedExercises[exercisePosition]

                    val updatedSets = exerciseWithSets.sets.toMutableList()
                    updatedSets.removeAll { it.setId == exerciseSet.setId }

                    val updatedExercise = ExerciseWithSets(
                        exercise = exerciseWithSets.exercise,
                        sets = updatedSets
                    )
                    updatedExercises.removeAt(exercisePosition)
                    updatedExercises.add(exercisePosition, updatedExercise)

                    val updatedWorkout = WorkoutDetailsWithExercises(
                        workoutDetails = workoutDetailsDB[workoutPosition].workoutDetails,
                        exercises = updatedExercises,
                        configuration = workoutDetailsDB[workoutPosition].configuration
                    )
                    workoutDetailsDB[workoutPosition] = updatedWorkout
                } else {
                    throw NoSuchElementException("Exercise not found for workout with id ${workoutDetailsDB[workoutPosition].workoutDetails.workoutDetailsId}")
                }
            } catch (e: NoSuchElementException) {
                return
            } catch (e: IllegalArgumentException) {
                return
            }
        }
    }

    override suspend fun onSetsDeleted(exerciseId: Int, workoutId: Int) {
        val workoutPosition = workoutDetailsDB.indexOfFirst {
            it.workoutDetails.workoutDetailsId == workoutId
        }

        //Workout found
        if (workoutPosition != -1) {
            try {
                val updatedExercises = workoutDetailsDB[workoutPosition].exercises?.toMutableList()
                    ?: throw IllegalArgumentException("Exercises not found for workout with id ${workoutDetailsDB[workoutPosition].workoutDetails.workoutDetailsId}")

                val exercisePosition = updatedExercises.indexOfFirst {
                    it.exercise.exerciseId == exerciseId &&
                            it.exercise.workoutId == workoutId
                }

                //Exercise found
                if (exercisePosition != -1) {
                    val exerciseWithSets = updatedExercises[exercisePosition]
                    val updatedExercise = ExerciseWithSets(
                        exercise = exerciseWithSets.exercise,
                        sets = emptyList()
                    )
                    updatedExercises.add(exercisePosition, updatedExercise)

                    val updatedWorkout = WorkoutDetailsWithExercises(
                        workoutDetails = workoutDetailsDB[workoutPosition].workoutDetails,
                        exercises = updatedExercises,
                        configuration = workoutDetailsDB[workoutPosition].configuration
                    )
                    workoutDetailsDB[workoutPosition] = updatedWorkout
                } else {
                    throw NoSuchElementException("Exercise not found for workout with id ${workoutDetailsDB[workoutPosition].workoutDetails.workoutDetailsId}")
                }
            } catch (e: NoSuchElementException) {
                return
            } catch (e: IllegalArgumentException) {
                return
            }
        }
    }

    override fun clearDB() {
        workoutDetailsDB.clear()
    }
}