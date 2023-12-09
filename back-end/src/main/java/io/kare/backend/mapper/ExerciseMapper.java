package io.kare.backend.mapper;

import io.kare.backend.entity.ExerciseEntity;
import io.kare.backend.entity.UserEntity;
import io.kare.backend.payload.data.ExercisePayload;
import io.kare.backend.payload.request.AddExerciseRequest;
import io.kare.backend.payload.response.AddExerciseResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ExerciseMapper {
    List<ExercisePayload> map(List<ExerciseEntity> entities);

    @Mapping(target = "user", source = "user")
    ExerciseEntity mapToEntity(AddExerciseRequest request, UserEntity user);

    AddExerciseResponse mapToResponse(ExerciseEntity exerciseEntity);
}
