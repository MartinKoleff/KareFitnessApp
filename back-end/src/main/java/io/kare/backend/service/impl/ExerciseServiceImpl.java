package io.kare.backend.service.impl;

import io.kare.backend.entity.*;
import io.kare.backend.payload.request.*;
import io.kare.backend.payload.response.*;
import io.kare.backend.repository.ExerciseRepository;
import io.kare.backend.service.ExerciseService;
import java.util.*;
import org.springframework.stereotype.Service;
import io.kare.backend.mapper.ExerciseMapper;

@Service
public class ExerciseServiceImpl implements ExerciseService {

    private final ExerciseRepository exerciseRepository;
    private final ExerciseMapper exerciseMapper;

    public ExerciseServiceImpl(ExerciseRepository exerciseRepository, ExerciseMapper exerciseMapper) {
        this.exerciseRepository = exerciseRepository;
        this.exerciseMapper = exerciseMapper;
    }

    @Override
    public GetExercisesResponse getExercises(UserEntity user) {
        List<ExerciseEntity> exerciseEntities = this.exerciseRepository.findAllByUser(user);
        return new GetExercisesResponse(this.exerciseMapper.map(exerciseEntities));
    }

    @Override
    public AddExerciseResponse addExercise(AddExerciseRequest request, UserEntity user) {
        ExerciseEntity exerciseEntity = this.exerciseMapper.mapToEntity(request, user);
        this.exerciseRepository.save(exerciseEntity);
        return this.exerciseMapper.mapToResponse(exerciseEntity);
    }

    @Override
    public List<ExerciseEntity> findAllByUser(UserEntity user) {
        return this.exerciseRepository.findAllByUser(user);
    }

    @Override
    public List<ExerciseEntity> findByIds(List<String> ids, UserEntity user) {
        return this.exerciseRepository.findAllByIdsAndUser(ids, user);
    }

    @Override
    public List<ExerciseEntity> save(List<ExerciseEntity> exercises) {
        return this.exerciseRepository.saveAll(exercises);
    }

    @Override
    public Optional<ExerciseEntity> findByName(String name, UserEntity user) {
        return this.exerciseRepository.findByNameAndUser(name, user);
    }

    @Override
    public EmptyResponse updateExercise(UpdateExerciseRequest request, UserEntity user) {
        Optional<ExerciseEntity> optional = this.exerciseRepository.findByIdAndUser(request.id(), user);
        if (optional.isEmpty()) {
            throw new RuntimeException("Exercise not found");
        }
        ExerciseEntity exerciseEntity = optional.get();
        exerciseEntity.setName(request.name());
        exerciseEntity.setDescription(request.description());
        exerciseEntity.setMuscleGroup(request.muscleGroup());
        exerciseEntity.setMachineType(request.machineType());
        exerciseEntity.setUrl(request.url());
        this.exerciseRepository.save(exerciseEntity);
        return new EmptyResponse();
    }

    @Override
    public void removeWorkoutFromExercises(WorkoutEntity workout) {
        List<ExerciseEntity> exercises = this.exerciseRepository.findByWorkout(workout);
        for (ExerciseEntity exercise : exercises) {
            exercise.getWorkouts().remove(workout);
        }
        this.exerciseRepository.saveAll(exercises);
    }
}
