package io.kare.backend.mapper;

import io.kare.backend.entity.ProgramEntity;
import io.kare.backend.payload.response.AddProgramResponse;
import io.kare.backend.payload.response.GetProgramResponse;
import io.kare.backend.payload.response.GetProgramsResponse;
import io.kare.backend.payload.response.GetWorkoutResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProgramMapper {
    GetProgramsResponse map(List<ProgramEntity> programEntities);
    GetProgramResponse map(ProgramEntity programEntity, List<GetWorkoutResponse> workouts);
    AddProgramResponse map(ProgramEntity programEntity);
}
