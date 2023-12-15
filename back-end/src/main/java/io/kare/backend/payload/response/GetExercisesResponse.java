package io.kare.backend.payload.response;

import io.kare.backend.payload.data.ExercisePayload;

import java.util.List;

public record GetExercisesResponse(List<ExercisePayload> exercises) {
}
