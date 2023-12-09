package io.kare.backend.service;

import io.kare.backend.entity.UserEntity;
import io.kare.backend.entity.WorkoutEntity;
import io.kare.backend.payload.request.AddWorkoutRequest;
import io.kare.backend.payload.request.GetWorkoutRequest;
import io.kare.backend.payload.response.AddWorkoutResponse;
import io.kare.backend.payload.response.GetWorkoutResponse;
import io.kare.backend.payload.response.GetWorkoutsResponse;

import java.util.List;

public interface WorkoutService {
    AddWorkoutResponse addWorkout(AddWorkoutRequest request, UserEntity user);

    GetWorkoutsResponse getWorkouts(UserEntity user);

    List<WorkoutEntity> getWorkouts(List<String> ids, UserEntity user);

    GetWorkoutResponse getWorkout(GetWorkoutRequest request, UserEntity user);
}
