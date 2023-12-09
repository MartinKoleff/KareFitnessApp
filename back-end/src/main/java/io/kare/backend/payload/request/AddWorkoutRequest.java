package io.kare.backend.payload.request;

import io.kare.backend.payload.data.ExercisePayload;
import io.kare.backend.payload.data.WorkoutExercisePayload;

import java.util.List;

public record AddWorkoutRequest(List<WorkoutExercisePayload> exercises, String name, String description) {
}
