package io.kare.backend.payload.response;

public record GetProgramResponse(GetWorkoutsResponse workouts, String name, String description, String id) {
}
