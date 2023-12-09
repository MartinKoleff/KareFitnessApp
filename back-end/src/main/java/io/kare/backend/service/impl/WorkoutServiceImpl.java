package io.kare.backend.service.impl;

import io.kare.backend.entity.ExerciseEntity;
import io.kare.backend.entity.UserEntity;
import io.kare.backend.entity.WorkoutEntity;
import io.kare.backend.exception.WorkoutNotFoundException;
import io.kare.backend.mapper.WorkoutMapper;
import io.kare.backend.payload.request.AddWorkoutRequest;
import io.kare.backend.payload.request.GetWorkoutRequest;
import io.kare.backend.payload.response.AddWorkoutResponse;
import io.kare.backend.payload.response.GetWorkoutResponse;
import io.kare.backend.payload.response.GetWorkoutsResponse;
import io.kare.backend.repository.WorkoutRepository;
import io.kare.backend.service.ExerciseService;
import io.kare.backend.service.WorkoutService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkoutServiceImpl implements WorkoutService {

    private final WorkoutRepository workoutRepository;
    private final ExerciseService exerciseRepository;
    private final WorkoutMapper workoutMapper;

    public WorkoutServiceImpl(WorkoutRepository workoutRepository, ExerciseService exerciseRepository, WorkoutMapper workoutMapper) {
        this.workoutRepository = workoutRepository;
        this.exerciseRepository = exerciseRepository;
        this.workoutMapper = workoutMapper;
    }

    @Override
    public AddWorkoutResponse addWorkout(AddWorkoutRequest request, UserEntity user) {
        List<ExerciseEntity> exercises = this.exerciseRepository.findByIds(request.exercises());
        WorkoutEntity workoutEntity = this.workoutMapper.mapToEntity(request, user, exercises);
        this.workoutRepository.save(workoutEntity);
        return this.workoutMapper.mapToResponse(workoutEntity);
    }

    @Override
    public GetWorkoutsResponse getWorkouts(UserEntity user) {
        List<WorkoutEntity> workoutEntities = this.workoutRepository.findAllByUser(user);
        return new GetWorkoutsResponse(this.workoutMapper.map(workoutEntities));
    }

    @Override
    public GetWorkoutResponse getWorkout(GetWorkoutRequest request, UserEntity user) {
        WorkoutEntity workoutEntity = this.workoutRepository.findById(request.id()).orElseThrow(() -> new WorkoutNotFoundException(request.id()));
        return this.workoutMapper.map(workoutEntity);
    }
}
