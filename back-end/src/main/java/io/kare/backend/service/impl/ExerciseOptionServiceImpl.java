package io.kare.backend.service.impl;

import io.kare.backend.entity.*;
import io.kare.backend.mapper.ExerciseOptionMapper;
import io.kare.backend.payload.data.*;
import io.kare.backend.repository.ExerciseOptionRepository;
import io.kare.backend.service.ExerciseOptionService;
import java.util.*;
import org.springframework.stereotype.Service;

@Service
public class ExerciseOptionServiceImpl implements ExerciseOptionService {
	private final ExerciseOptionRepository exerciseOptionRepository;
	private final ExerciseOptionMapper exerciseOptionMapper;

	public ExerciseOptionServiceImpl(ExerciseOptionRepository exerciseOptionRepository,
		ExerciseOptionMapper exerciseOptionMapper
	) {
		this.exerciseOptionRepository = exerciseOptionRepository;
		this.exerciseOptionMapper = exerciseOptionMapper;
	}

	@Override
	public void save(List<WorkoutExercisePayload> exercisePayloads, List<ExerciseEntity> exercises, WorkoutEntity workout) {
		this.exerciseOptionRepository.saveAll(this.exerciseOptionMapper.toExerciseOptionEntities(
			exercisePayloads,
			exercises,
			workout
		));
	}
}
