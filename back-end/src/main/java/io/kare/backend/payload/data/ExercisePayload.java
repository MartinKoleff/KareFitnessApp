package io.kare.backend.payload.data;

public record ExercisePayload(
        String id,
        String name,
        String description,
        MuscleGroup muscleGroup,
        MachineType machineType,
        String url
) {
}
