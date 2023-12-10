package io.kare.backend.payload.data;

public record WorkoutExerciseData(
	ExercisePayload exercise,
	int sets,
	int reps,
	int weight
) {

}
