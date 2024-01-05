package io.kare.backend.payload.response;

import io.kare.backend.payload.data.*;

import java.util.List;

public record GetWorkoutResponse(String id, String name, String description, List<WorkoutExerciseData> exercises, int totalExercises) {
}
