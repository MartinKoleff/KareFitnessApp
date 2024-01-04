package io.kare.backend.mapper;

import io.kare.backend.entity.*;
import io.kare.backend.payload.data.*;
import io.kare.backend.payload.request.AddProgramRequest;
import io.kare.backend.payload.response.*;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProgramMapper {

	AddProgramResponse mapToAdd(ProgramEntity programEntity);

	default GetProgramResponse mapToGet(
		ProgramEntity programEntity,
		List<ExerciseOptionEntity> exerciseOptions
	) {
		return new GetProgramResponse(
			programEntity.getId(),
			programEntity.getName(),
			programEntity.getDescription(),
			this.mapToGetWorkouts(programEntity.getWorkouts(), exerciseOptions)
		);
	}

	private GetWorkoutsResponse mapToGetWorkouts(
		List<WorkoutEntity> workouts,
		List<ExerciseOptionEntity> exerciseOptions
	) {
		return new GetWorkoutsResponse(
			workouts.stream()
				.map(workout -> {
					var exercises = this.mapToGetExercises(
						workout.getExercises(),
						this.findExerciseOptionsByWorkoutId(workout.getId(), exerciseOptions)
					);
					return new GetWorkoutResponse(
					workout.getId(),
					workout.getName(),
					workout.getDescription(),
					exercises,
					exercises.size()
				);})
				.toList()
		);
	}

	private List<WorkoutExerciseData> mapToGetExercises(
		List<ExerciseEntity> exercises,
		List<ExerciseOptionEntity> exerciseOptions
	) {
		return exercises.stream()
			.map(exercise -> {
				ExerciseOptionEntity exerciseOption = exerciseOptions.stream()
					.filter(option -> option.getExercise()
						.getId()
						.equals(exercise.getId()))
					.findFirst()
					.orElse(null);
				if (exerciseOption == null) {
					return null;
				}
				return new WorkoutExerciseData(
					new ExercisePayload(
						exercise.getId(),
						exercise.getName(),
						exercise.getDescription(),
						exercise.getMuscleGroup(),
						exercise.getMachineType(),
						exercise.getUrl()
					),
					exerciseOption.getSets(),
					exerciseOption.getReps(),
					exerciseOption.getWeight()
				);
			})
			.toList();
	}

	default List<GetProgramResponse> mapToGet(
		List<ProgramEntity> programEntities,
		List<ExerciseOptionEntity> exerciseOptions
	) {
		return programEntities.stream()
			.map(programEntity -> this.mapToGet(programEntity, this.findExerciseOptionsByWorkoutIds(
				programEntity.getWorkouts()
					.stream()
					.map(WorkoutEntity::getId)
					.toList(), exerciseOptions)))
			.toList();
	}

	private List<ExerciseOptionEntity> findExerciseOptionsByWorkoutId(
		String workoutId,
		List<ExerciseOptionEntity> exerciseOptions
	) {
		return exerciseOptions.stream()
			.filter(exerciseOption -> exerciseOption.getWorkout()
				.getId()
				.equals(workoutId))
			.toList();
	}

	private List<ExerciseOptionEntity> findExerciseOptionsByWorkoutIds(
		List<String> workoutIds,
		List<ExerciseOptionEntity> exerciseOptions
	) {
		return exerciseOptions.stream()
			.filter(exerciseOption -> workoutIds.contains(exerciseOption.getWorkout()
				.getId()))
			.toList();
	}

	@Mapping(target = "user", source = "user")
	@Mapping(target = "workouts", source = "workouts")
	ProgramEntity mapToEntity(
		AddProgramRequest request,
		UserEntity user,
		List<WorkoutEntity> workouts
	);
}
