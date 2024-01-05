package io.kare.backend.payload.data;

public record WorkoutFullExercisePayload(
		int sets,
		int reps,
		int weight,
		String name,
		String description,
		String url,
		MuscleGroup muscleGroup,
		MachineType machineType
) {

}
