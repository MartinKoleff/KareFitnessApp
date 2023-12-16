package io.kare.backend.service;

import io.kare.backend.entity.*;
import io.kare.backend.payload.data.*;
import java.util.List;

public interface ExerciseOptionService {

	void save(List<WorkoutExercisePayload> workoutExercisePayloads, List<ExerciseEntity> exercises, WorkoutEntity workout);

	void update(
		List<WorkoutExercisePayload> exercisePayloads,
		List<ExerciseEntity> exercises,
		WorkoutEntity workout
	);

	List<ExerciseOptionEntity> findAllByWorkoutIds(List<String> ids, UserEntity user);

	ExerciseOptionEntity mapToEntity(WorkoutFullExercisePayload exercisePayload, ExerciseEntity exercise, WorkoutEntity workoutEntity);

	List<ExerciseOptionEntity> save(List<ExerciseOptionEntity> exerciseOptionEntities);
}
