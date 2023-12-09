package io.kare.backend.service.impl;

import io.kare.backend.entity.ProgramEntity;
import io.kare.backend.entity.UserEntity;
import io.kare.backend.entity.WorkoutEntity;
import io.kare.backend.mapper.ProgramMapper;
import io.kare.backend.payload.request.AddProgramRequest;
import io.kare.backend.payload.request.GetProgramRequest;
import io.kare.backend.payload.response.AddProgramResponse;
import io.kare.backend.payload.response.GetProgramResponse;
import io.kare.backend.payload.response.GetProgramsResponse;
import io.kare.backend.repository.ProgramRepository;
import io.kare.backend.service.ProgramService;
import io.kare.backend.service.WorkoutService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProgramServiceImpl implements ProgramService {

    private final ProgramRepository programRepository;
    private final ProgramMapper programMapper;
    private final WorkoutService workoutService;

    public ProgramServiceImpl(ProgramRepository programRepository, ProgramMapper programMapper, WorkoutService workoutService) {
        this.programRepository = programRepository;
        this.programMapper = programMapper;
        this.workoutService = workoutService;
    }


    @Override
    public AddProgramResponse addProgram(AddProgramRequest request, UserEntity user) {
        List<WorkoutEntity> workouts = this.workoutService.getWorkouts(request.workoutIds(), user);
        ProgramEntity programEntity = this.programMapper.map(request, user, workouts);
        return this.programMapper.map(this.programRepository.save(programEntity));
    }

    @Override
    public GetProgramsResponse getPrograms(UserEntity user) {
        return this.programMapper.map(this.programRepository.findAllByUser(user));
    }

    @Override
    public GetProgramResponse getProgram(GetProgramRequest request, UserEntity user) {
        return this.programMapper.map(this.programRepository.findById(request.id()).orElseThrow(() -> new RuntimeException("Program not found")));
    }
}
