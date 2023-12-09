package io.kare.backend.mapper;

import io.kare.backend.entity.*;
import io.kare.backend.payload.data.WorkoutExercisePayload;
import java.util.*;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ExerciseOptionMapper {
	default List<ExerciseOptionEntity> toExerciseOptionEntities(List<WorkoutExercisePayload> payloads,
		List<ExerciseEntity> exercises,
		WorkoutEntity workout) {
		List<ExerciseOptionEntity> options = new ArrayList<>();
		for (WorkoutExercisePayload payload : payloads) {
			ExerciseEntity exercise = findExerciseById(exercises, payload.exerciseId());
			if (exercise != null) {
				ExerciseOptionEntity option = new ExerciseOptionEntity();
				option.setExercise(exercise);
				option.setWorkout(workout);
				option.setSets(payload.sets());
				option.setReps(payload.reps());
				option.setWeight(payload.weight());
				options.add(option);
			}
		}
		return options;
	}

	private ExerciseEntity findExerciseById(List<ExerciseEntity> exercises, String id) {
		for (ExerciseEntity exercise : exercises) {
			if (exercise.getId().equals(id)) {
				return exercise;
			}
		}
		return null;
	}
}
