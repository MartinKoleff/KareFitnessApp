package io.kare.backend.payload.response;

public record GetProgramResponse(String id, String name, String description, GetWorkoutsResponse workouts) {
}
