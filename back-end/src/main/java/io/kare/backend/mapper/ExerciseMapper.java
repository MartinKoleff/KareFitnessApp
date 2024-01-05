package io.kare.backend.mapper;

import io.kare.backend.entity.*;
import io.kare.backend.payload.data.*;
import io.kare.backend.payload.request.AddExerciseRequest;
import io.kare.backend.payload.response.AddExerciseResponse;
import java.util.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ExerciseMapper {
    List<ExercisePayload> map(List<ExerciseEntity> entities);

    @Mapping(target = "user", source = "user")
    @Mapping(target = "workouts", source = "workouts")
    ExerciseEntity mapToEntity(AddExerciseRequest request, UserEntity user, List<WorkoutEntity> workouts);

    default ExerciseEntity mapToEntity(AddExerciseRequest request, UserEntity user) {
        return this.mapToEntity(request, user, new ArrayList<>());
    }

    AddExerciseResponse mapToResponse(ExerciseEntity exerciseEntity);


	default List<ExerciseEntity> mapFromWorkoutExerciseFullPayload(
        List<WorkoutFullExercisePayload> exercises,
        UserEntity user
    ) {
        List<ExerciseEntity> entities = new ArrayList<>();
        for (WorkoutFullExercisePayload exercise : exercises) {
            entities.add(this.mapFromWorkoutExerciseFullPayload(exercise, user));
        }
        return entities;
    }

    @Mapping(target = "user", source = "user")
    ExerciseEntity mapFromWorkoutExerciseFullPayload(WorkoutFullExercisePayload exercise, UserEntity user);
}
