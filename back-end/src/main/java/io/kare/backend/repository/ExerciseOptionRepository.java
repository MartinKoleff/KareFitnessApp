package io.kare.backend.repository;

import io.kare.backend.entity.*;
import org.springframework.data.jpa.repository.*;

import java.util.List;
import org.springframework.data.repository.query.Param;

public interface ExerciseOptionRepository extends JpaRepository<ExerciseOptionEntity, String> {

	@Query("SELECT e FROM ExerciseOptionEntity e WHERE e.workout.id IN :ids AND e.workout.user = :user")
	List<ExerciseOptionEntity> findAllByWorkoutIdsAndUser(@Param("ids") List<String> ids, @Param("user") UserEntity user);

	@Query("SELECT eo FROM ExerciseOptionEntity eo WHERE eo.workout.id = :workoutId AND eo.exercise.id IN :exerciseIds")
	List<ExerciseOptionEntity> findByWorkoutIdAndExerciseIds(String workoutId, List<String> exerciseIds);

	List<ExerciseOptionEntity> findAllByWorkout(WorkoutEntity workout);
}
