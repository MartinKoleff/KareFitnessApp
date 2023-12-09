package io.kare.backend.service.impl;

import io.kare.backend.entity.ExerciseEntity;
import io.kare.backend.entity.UserEntity;
import io.kare.backend.payload.request.AddExerciseRequest;
import io.kare.backend.payload.response.AddExerciseResponse;
import io.kare.backend.payload.response.GetExercisesResponse;
import io.kare.backend.repository.ExerciseRepository;
import io.kare.backend.service.ExerciseService;
import org.springframework.stereotype.Service;
import io.kare.backend.mapper.ExerciseMapper;

import java.util.List;

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
}
