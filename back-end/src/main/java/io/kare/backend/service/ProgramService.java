package io.kare.backend.service;

import io.kare.backend.entity.UserEntity;
import io.kare.backend.payload.request.AddProgramRequest;
import io.kare.backend.payload.request.GetProgramRequest;
import io.kare.backend.payload.response.AddProgramResponse;
import io.kare.backend.payload.response.GetProgramResponse;
import io.kare.backend.payload.response.GetProgramsResponse;

public interface ProgramService {
    AddProgramResponse addProgram(AddProgramRequest request, UserEntity user);

    GetProgramsResponse getPrograms(UserEntity user);

    GetProgramResponse getProgram(GetProgramRequest request, UserEntity user);
}
