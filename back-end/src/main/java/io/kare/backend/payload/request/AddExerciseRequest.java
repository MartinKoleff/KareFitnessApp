package io.kare.backend.payload.request;

import io.kare.backend.payload.data.MachineType;
import io.kare.backend.payload.data.MuscleGroup;

public record AddExerciseRequest(
    String name,
    String description,
    MuscleGroup muscleGroup,
    MachineType machineType,
    String url
) {
}
