package io.kare.backend.mapper;

import io.kare.backend.entity.*;
import io.kare.backend.payload.request.AddProgramRequest;
import io.kare.backend.payload.response.AddProgramResponse;
import io.kare.backend.payload.response.GetProgramResponse;
import io.kare.backend.payload.response.GetProgramsResponse;
import io.kare.backend.payload.response.GetWorkoutResponse;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProgramMapper {
    AddProgramResponse map(ProgramEntity programEntity);

	@Mapping(target = "user", source = "user")
	@Mapping(target = "workouts", source = "workouts")
	ProgramEntity mapToEntity(AddProgramRequest request, UserEntity user, List<WorkoutEntity> workouts);
}
