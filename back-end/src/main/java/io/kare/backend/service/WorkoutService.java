package io.kare.backend.service;

import io.kare.backend.entity.*;
import io.kare.backend.payload.request.*;
import io.kare.backend.payload.response.AddWorkoutResponse;
import io.kare.backend.payload.response.GetWorkoutResponse;
import io.kare.backend.payload.response.GetWorkoutsResponse;

import java.util.List;

public interface WorkoutService {
    AddWorkoutResponse addWorkout(AddWorkoutRequest request, UserEntity user);

    GetWorkoutsResponse getWorkouts(UserEntity user);

    List<WorkoutEntity> getWorkouts(List<String> ids, UserEntity user);

    GetWorkoutResponse getWorkout(GetWorkoutRequest request, UserEntity user);

    List<ExerciseOptionEntity> getExerciseOptionEntitiesByWorkoutIds(
        List<String> ids,
        UserEntity user
    );

    void selectWorkout(SelectWorkoutRequest request, UserEntity user);

    GetWorkoutResponse getSelectedWorkout(UserEntity user);

	AddWorkoutResponse addFullWorkout(AddFullWorkoutRequest request, UserEntity user);
}
