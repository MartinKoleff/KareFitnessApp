package io.kare.backend.payload.response;

import java.util.List;

public record GetWorkoutsResponse(List<GetWorkoutResponse> workouts) {
}
