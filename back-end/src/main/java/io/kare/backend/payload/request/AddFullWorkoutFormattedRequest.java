package io.kare.backend.payload.request;

import io.kare.backend.payload.data.*;
import java.util.List;

public record AddFullWorkoutFormattedRequest(List<WorkoutFullExerciseFormattedPayload> exercises, String name, String description) {

}
