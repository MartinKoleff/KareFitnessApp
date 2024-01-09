package io.kare.backend.service;

import io.kare.backend.entity.*;
import io.kare.backend.payload.request.*;
import io.kare.backend.payload.response.*;

public interface ProgramService {
    AddProgramResponse addProgram(AddProgramRequest request, UserEntity user);

    GetProgramsResponse getPrograms(UserEntity user);

    GetProgramResponse getProgram(GetProgramRequest request, UserEntity user);

    Void updateProgram(UpdateProgramRequest request, UserEntity user);

    void removeWorkout(WorkoutEntity workoutEntity);
}
