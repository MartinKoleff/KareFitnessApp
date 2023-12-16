package io.kare.backend.payload.request;

import io.kare.backend.payload.data.WorkoutExercisePayload;
import java.util.List;

public record UpdateWorkoutRequest(String id, List<WorkoutExercisePayload> exercises, String name, String description) {

}
