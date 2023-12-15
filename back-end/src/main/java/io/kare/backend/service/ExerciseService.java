package io.kare.backend.service;

import io.kare.backend.entity.ExerciseEntity;
import io.kare.backend.entity.UserEntity;
import io.kare.backend.payload.request.AddExerciseRequest;
import io.kare.backend.payload.response.AddExerciseResponse;
import io.kare.backend.payload.response.GetExercisesResponse;

import java.util.*;

public interface ExerciseService {
    GetExercisesResponse getExercises(UserEntity user);

    AddExerciseResponse addExercise(AddExerciseRequest request, UserEntity user);

    List<ExerciseEntity> findAllByUser(UserEntity user);

    List<ExerciseEntity> findByIds(List<String> id, UserEntity user);

	List<ExerciseEntity> save(List<ExerciseEntity> exercises);

	Optional<ExerciseEntity> findByName(String id, UserEntity user);
}
