package io.kare.backend.mapper;

import io.kare.backend.entity.ExerciseEntity;
import io.kare.backend.entity.UserEntity;
import io.kare.backend.entity.WorkoutEntity;
import io.kare.backend.payload.request.AddWorkoutRequest;
import io.kare.backend.payload.response.AddWorkoutResponse;
import io.kare.backend.payload.response.GetWorkoutResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WorkoutMapper {

    @Mapping(target = "user", source = "user")
    @Mapping(target = "exercises", source = "exercises")
    WorkoutEntity mapToEntity(AddWorkoutRequest request, UserEntity user, List<ExerciseEntity> exercises);

    AddWorkoutResponse mapToResponse(WorkoutEntity workoutEntity);

    List<GetWorkoutResponse> map(List<WorkoutEntity> workoutEntities);

    GetWorkoutResponse map(WorkoutEntity workoutEntity);
}
