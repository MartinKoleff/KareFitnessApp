package com.koleff.kare_android.workout.data

import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.room.dao.WorkoutConfigurationDao
import com.koleff.kare_android.data.room.dao.WorkoutDetailsDao
import com.koleff.kare_android.data.room.dao.WorkoutDetailsId
import com.koleff.kare_android.data.room.entity.Exercise
import com.koleff.kare_android.data.room.entity.WorkoutDetails
import com.koleff.kare_android.data.room.entity.ExerciseWithSets
import com.koleff.kare_android.data.room.entity.WorkoutConfiguration
import com.koleff.kare_android.data.room.entity.relations.WorkoutDetailsExerciseCrossRef
import com.koleff.kare_android.data.room.entity.WorkoutDetailsWithExercises
import com.koleff.kare_android.data.room.entity.WorkoutWithConfig
import com.koleff.kare_android.exercise.data.ExerciseDaoFake
import com.koleff.kare_android.utils.TestLogger

//ExerciseDao is needed for fetching exercises from cross refs
class WorkoutDetailsDaoFake(
    private val exerciseDao: ExerciseDaoFake,
    private val logger: TestLogger
) : WorkoutDetailsDao {

    private val workoutDetailsDB = mutableListOf<WorkoutDetailsWithExercises>()
    private val workoutDetailsExerciseCrossRefs = mutableListOf<WorkoutDetailsExerciseCrossRef>()


    private val isInternalLogging = false

    companion object {
        private const val TAG = "WorkoutDetailsDaoFake"
    }


    //Assuming there is already a workout in the workoutDB -> no need for autoincrement -> id is verified
    override suspend fun insertWorkoutDetails(workoutDetails: WorkoutDetails): WorkoutDetailsId {
        workoutDetailsDB.add(
            WorkoutDetailsWithExercises(
                workoutDetails = workoutDetails,
                exercises = emptyList(),
                configuration = null
            )
        )

        return workoutDetails.workoutDetailsId.toLong()
    }

    override suspend fun insertAllDetails(workoutDetailsList: List<WorkoutDetails>) {
        workoutDetailsList.forEach { workoutDetails ->
            insertWorkoutDetails(workoutDetails)
        }
    }

    override suspend fun insertWorkoutDetailsExerciseCrossRef(crossRef: WorkoutDetailsExerciseCrossRef) {
        workoutDetailsExerciseCrossRefs.add(crossRef)
    }

    override suspend fun insertAllWorkoutDetailsExerciseCrossRef(crossRefs: List<WorkoutDetailsExerciseCrossRef>) {
        workoutDetailsExerciseCrossRefs.addAll(crossRefs)
    }

    override suspend fun deleteWorkoutDetailsExerciseCrossRef(crossRef: WorkoutDetailsExerciseCrossRef) {
        workoutDetailsExerciseCrossRefs.remove(crossRef)
    }

    override suspend fun deleteAllWorkoutDetailsExerciseCrossRef(crossRefs: List<WorkoutDetailsExerciseCrossRef>) {
        workoutDetailsExerciseCrossRefs.removeAll(crossRefs)
    }

    override suspend fun deleteWorkoutDetails(workoutId: Int) {
        val entries = workoutDetailsDB.map {
            it.workoutDetails
        }.filter { it.workoutDetailsId == workoutId }

        workoutDetailsDB.removeAll {
            entries.contains(it.workoutDetails)
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

            //Delete invalid workout?
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
        return workoutDetailsDB.sortedBy { it.workoutDetails.workoutDetailsId }.map {
            WorkoutDetailsWithExercises(
                workoutDetails = it.workoutDetails,
                exercises = getWorkoutExercises(it.workoutDetails.workoutDetailsId),
                configuration = getWorkoutConfiguration(it.workoutDetails.workoutDetailsId)
            )
        }
    }

    private fun getWorkoutConfiguration(workoutId: Int): WorkoutConfiguration? {
        return workoutDetailsDB
            .map { it.configuration }
            .firstOrNull { it?.workoutId == workoutId }
    }

    private fun getWorkoutExercises(workoutId: Int): List<Exercise> {
        val workoutExercisesIndexes = workoutDetailsExerciseCrossRefs
            .map { it.exerciseId }

        val workoutExercises = exerciseDao.getAllExercises()
            .filter { exercise -> workoutExercisesIndexes.contains(exercise.exerciseId) }
            .filter { exercise -> exercise.workoutId == workoutId }

        return workoutExercises
    }

    private fun getAllExercises(): List<Exercise> {
        val workoutExercisesIndexes = workoutDetailsExerciseCrossRefs
            .map { it.exerciseId }

        val workoutExercises = exerciseDao.getAllExercises()
            .filter { exercise -> workoutExercisesIndexes.contains(exercise.exerciseId) }

        return workoutExercises
    }

    suspend fun getWorkoutExercisesWithSets(): List<ExerciseDto> {
        val exercises = getAllExercises()

        val exerciseWithSets = exercises
            .map {
                exerciseDao.getExerciseWithSets(
                    it.exerciseId,
                    it.workoutId
                )
            }
            .map(ExerciseWithSets::toDto)

        if (isInternalLogging) logger.i(TAG, "Get workout exercises with sets -> $exerciseWithSets")
        return exerciseWithSets
    }

    override fun getWorkoutByIsSelected(): WorkoutDetailsWithExercises? {

        val selectedWorkout = workoutDetailsDB.firstOrNull {
            it.workoutDetails.isSelected
        } ?: return null

        val workoutExercises = getWorkoutExercises(selectedWorkout.workoutDetails.workoutDetailsId)
        return selectedWorkout.copy(
            exercises = workoutExercises
        )
    }

//    override fun getWorkoutDetailsById(workoutId: Int): WorkoutDetailsWithExercisesWithSets? {
//        val workoutExercises = getWorkoutExercises().filter { it.workoutId == workoutId }
//            .map { ExerciseWithSets(exercise = it, sets = emptyList()) }
//
//        return WorkoutDetailsWithExercisesWithSets(
//            workoutDetails =
//            workoutDetailsDB.firstOrNull {
//                it.workoutDetails.workoutDetailsId == workoutId
//            }?.workoutDetails ?: return null,
//            exercisesWithSets = workoutExercises
//        )
//    }

    override fun getWorkoutDetailsById(workoutId: Int): WorkoutDetailsWithExercises? {
        val workoutExercises = getWorkoutExercises(workoutId)
        val configuration = getWorkoutConfiguration(workoutId)

        return workoutDetailsDB.firstOrNull {
            it.workoutDetails.workoutDetailsId == workoutId
        }?.copy(
            exercises = workoutExercises,
            configuration = configuration
        )
    }

    fun clearDB() {
        workoutDetailsDB.clear()
        workoutDetailsExerciseCrossRefs.clear()
    }

    //Called when WorkoutConfiguration was inserted/updated
    fun updateWorkoutConfiguration(configuration: WorkoutConfiguration) {
        val index = workoutDetailsDB.map {
            it.workoutDetails
        }.indexOfFirst { it.workoutDetailsId == configuration.workoutId }

        //WorkoutConfiguration found
        if(index != -1){
            workoutDetailsDB[index] = workoutDetailsDB[index].copy(configuration = configuration)
        }else{

            //No workout found for workout configuration
        }
    }

    //Called when WorkoutConfiguration was deleted
    fun deleteWorkoutConfiguration(workoutId: Int) {
        val index = workoutDetailsDB.map {
            it.workoutDetails
        }.indexOfFirst { it.workoutDetailsId == workoutId }

        //WorkoutConfiguration found
        if(index != -1){
            workoutDetailsDB[index] = workoutDetailsDB[index].copy(configuration = null)
        }else{

            //No workout found for workout configuration
        }
    }
}