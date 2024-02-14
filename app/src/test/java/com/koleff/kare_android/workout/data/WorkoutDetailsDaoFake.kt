package com.koleff.kare_android.workout.data

import com.koleff.kare_android.data.room.dao.WorkoutDetailsDao
import com.koleff.kare_android.data.room.entity.WorkoutDetails
import com.koleff.kare_android.data.room.entity.relations.WorkoutDetailsExerciseCrossRef
import com.koleff.kare_android.data.room.entity.relations.WorkoutDetailsWithExercises

class WorkoutDetailsDaoFake : WorkoutDetailsDao {

    private val workoutDetailsDB = mutableListOf<WorkoutDetailsWithExercises>()
    private val workoutDetailsExerciseCrossRefs = mutableListOf<WorkoutDetailsExerciseCrossRef>()


    //Assuming there is already a workout in the workoutDB -> no need for autoincrement -> id is verified
    override suspend fun insertWorkoutDetails(workoutDetails: WorkoutDetails): Long {
        workoutDetailsDB.add(
            WorkoutDetailsWithExercises(
                workoutDetails = workoutDetails,
                exercises = emptyList()
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

        if (index != -1) {
            val exercises = workoutDetailsDB[index].exercises

            //Replace workout
            workoutDetailsDB[index] = WorkoutDetailsWithExercises(
                workoutDetails = workout,
                exercises = exercises
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
            WorkoutDetailsWithExercises(it.workoutDetails, it.exercises)
        }
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
}