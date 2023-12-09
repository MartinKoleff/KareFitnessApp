package io.kare.backend.service;

import io.kare.backend.entity.*;
import io.kare.backend.payload.data.WorkoutExercisePayload;
import java.util.List;

public interface ExerciseOptionService {

	void save(List<WorkoutExercisePayload> workoutExercisePayloads, List<ExerciseEntity> exercises, WorkoutEntity workout);
}
