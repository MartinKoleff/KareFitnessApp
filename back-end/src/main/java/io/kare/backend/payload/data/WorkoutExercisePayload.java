package io.kare.backend.payload.data;

public record WorkoutExercisePayload(
        String exerciseId,
        int sets,
        int reps,
        int weight
) {
}
