package io.kare.backend.service.impl;

import io.kare.backend.entity.*;
import io.kare.backend.exception.ProgramNotFoundException;
import io.kare.backend.mapper.ProgramMapper;
import io.kare.backend.payload.request.*;
import io.kare.backend.payload.response.*;
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

	public ProgramServiceImpl(
		ProgramRepository programRepository,
		ProgramMapper programMapper,
		WorkoutService workoutService
	) {
		this.programRepository = programRepository;
		this.programMapper = programMapper;
		this.workoutService = workoutService;
	}

	@Override
	public AddProgramResponse addProgram(AddProgramRequest request, UserEntity user) {
		List<WorkoutEntity> workouts = this.workoutService.getWorkouts(request.workoutIds(), user);
		ProgramEntity programEntity = this.programMapper.mapToEntity(request, user, workouts);
		return this.programMapper.mapToAdd(this.programRepository.save(programEntity));
	}

	@Override
	public GetProgramsResponse getPrograms(UserEntity user) {
		List<ProgramEntity> entities = this.programRepository.findAllByUser(user);
		List<ExerciseOptionEntity> exerciseOptions = this.workoutService.getExerciseOptionEntitiesByWorkoutIds(
			entities.stream()
				.map(entity -> entity.getWorkouts()
					.stream()
					.map(WorkoutEntity::getId)
					.toList())
				.flatMap(List::stream)
				.toList(), user);
		return new GetProgramsResponse(this.programMapper.mapToGet(entities, exerciseOptions));
	}

	@Override
	public GetProgramResponse getProgram(GetProgramRequest request, UserEntity user) {
		ProgramEntity entity = this.programRepository.findById(request.id())
			.orElseThrow(ProgramNotFoundException::new);
		List<ExerciseOptionEntity> exerciseOptions = this.workoutService.getExerciseOptionEntitiesByWorkoutIds(
			entity.getWorkouts()
				.stream()
				.map(WorkoutEntity::getId)
				.toList(), user);
		return this.programMapper.mapToGet(this.programRepository.findById(request.id())
			.orElseThrow(ProgramNotFoundException::new), exerciseOptions);
	}

	@Override
	public EmptyResponse updateProgram(UpdateProgramRequest request, UserEntity user) {
		ProgramEntity entity = this.programRepository.findById(request.id())
			.orElseThrow(ProgramNotFoundException::new);
		List<WorkoutEntity> workouts = this.workoutService.getWorkouts(request.workoutIds(), user);
		entity.setName(request.name());
		entity.setWorkouts(workouts);
		this.programRepository.save(entity);
		return new EmptyResponse();
	}
}
