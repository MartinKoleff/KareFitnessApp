//package com.koleff.kare_android.workout.data
//
//import com.koleff.kare_android.data.room.dao.WorkoutDao
//import com.koleff.kare_android.data.room.entity.Workout
//import com.koleff.kare_android.data.room.entity.relations.WorkoutDetailsExerciseCrossRef
//import com.koleff.kare_android.data.room.entity.relations.WorkoutDetailsWorkoutCrossRef
//
//class WorkoutDaoFake : WorkoutDao {
//
//    private var workoutsDB = mutableListOf<Workout>()
//    private val workoutDetailsWorkoutCrossRefs = mutableListOf<WorkoutDetailsWorkoutCrossRef>()
//
//    private var autoIncrementId = 1
//
//    override suspend fun insertWorkout(workout: Workout): Long {
//        return if (workout.workoutId == 0) {
//            workoutsDB.add(workout.copy(workoutId = autoIncrementId))
//            autoIncrementId++.toLong()
//        } else {
//            workoutsDB.add(workout)
//            workout.workoutId.toLong()
//        }
//    }
//
//    override suspend fun insertAll(workouts: List<Workout>) {
//        workouts.forEach { insertWorkout(it) }
//    }
//
//    override suspend fun insertAllWorkoutDetailsWorkoutCrossRef(crossRefs: List<WorkoutDetailsWorkoutCrossRef>) {
//        workoutDetailsWorkoutCrossRefs.addAll(crossRefs)
//    }
//
//    override suspend fun insertWorkoutDetailsWorkoutCrossRef(crossRef: WorkoutDetailsWorkoutCrossRef) {
//        workoutDetailsWorkoutCrossRefs.add(crossRef)
//    }
//
//    override suspend fun deleteWorkout(workoutId: Int) {
//
//        //If there are multiple entries -> remove all
//        workoutsDB.removeAll { it.workoutId == workoutId }
//    }
//
//    override suspend fun updateWorkout(workout: Workout) {
//
//        //Find workout
//        val index = workoutsDB.indexOfFirst { it.workoutId == workout.workoutId }
//
//        //Workout found
//        if (index != -1) {
//
//            //Replace workout
//            workoutsDB[index] = workout
//        } else {
//
//            //Delete invalid workout?
//        }
//    }
//
//    override suspend fun selectWorkoutById(workoutId: Int) {
//        workoutsDB.forEach {
//            it.isSelected = it.workoutId == workoutId
//        }
//    }
//
//    override fun getWorkoutsOrderedById(): List<Workout> = workoutsDB.sortedBy { it.workoutId }
//
//    override fun getWorkoutByIsSelected(): Workout? = workoutsDB.firstOrNull { it.isSelected }
//    override fun getWorkoutById(workoutId: Int): Workout =
//        workoutsDB.first { it.workoutId == workoutId }
//
//    fun clearDB() {
//        workoutsDB.clear()
//        workoutDetailsWorkoutCrossRefs.clear()
//    }
//}