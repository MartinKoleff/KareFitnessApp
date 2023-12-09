package io.kare.backend.mapper;

import io.kare.backend.entity.*;
import io.kare.backend.payload.request.AddWorkoutRequest;
import io.kare.backend.payload.response.AddWorkoutResponse;
import io.kare.backend.payload.response.GetWorkoutResponse;
import java.util.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WorkoutMapper {

    @Mapping(target = "user", source = "user")
    @Mapping(target = "exercises", source = "exercises")
    @Mapping(target = "programs", source = "programs")
    WorkoutEntity mapToEntity(AddWorkoutRequest request, UserEntity user, List<ExerciseEntity> exercises, List<ProgramEntity> programs);

    default WorkoutEntity mapToEntity(AddWorkoutRequest request, UserEntity user, List<ExerciseEntity> exercises) {
        return this.mapToEntity(request, user, exercises, new ArrayList<>());
    }

    AddWorkoutResponse mapToResponse(WorkoutEntity workoutEntity);

    List<GetWorkoutResponse> map(List<WorkoutEntity> workoutEntities);

    GetWorkoutResponse map(WorkoutEntity workoutEntity);
}
