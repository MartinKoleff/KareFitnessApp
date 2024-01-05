package io.kare.backend.payload.request;

import io.kare.backend.payload.data.*;

public record UpdateExerciseRequest(
	String id,
	String name,
	String description,
	MuscleGroup muscleGroup,
	MachineType machineType,
	String url
) {

}
