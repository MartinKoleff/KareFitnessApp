package io.kare.backend.mapper;

import io.kare.backend.entity.*;
import io.kare.backend.payload.data.*;
import io.kare.backend.payload.request.*;
import io.kare.backend.payload.response.*;
import java.util.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WorkoutMapper {

    @Mapping(target = "user", source = "user")
    @Mapping(target = "exercises", source = "exercises")
    @Mapping(target = "programs", source = "programs")
    WorkoutEntity mapToEntity(AddWorkoutRequest request, UserEntity user, List<ExerciseEntity> exercises, List<ProgramEntity> programs);

    @Mapping(target = "user", source = "user")
    @Mapping(target = "exercises", source = "exercises")
    @Mapping(target = "programs", source = "programs")
    WorkoutEntity mapToEntity(AddFullWorkoutRequest request, UserEntity user, List<ExerciseEntity> exercises, List<ProgramEntity> programs);

    default WorkoutEntity mapToEntity(AddWorkoutRequest request, UserEntity user, List<ExerciseEntity> exercises) {
        return this.mapToEntity(request, user, exercises, new ArrayList<>());
    }

    default WorkoutEntity mapToEntity(AddFullWorkoutRequest request, UserEntity user, List<ExerciseEntity> exercises) {
        return this.mapToEntity(request, user, exercises, new ArrayList<>());
    }

    AddWorkoutResponse mapToResponse(WorkoutEntity workoutEntity);

    default GetWorkoutsResponse mapToGetWorkouts(
        List<WorkoutEntity> workouts,
        List<ExerciseOptionEntity> exerciseOptions
    ) {
        return new GetWorkoutsResponse(
            workouts.stream()
                .map(workout -> new GetWorkoutResponse(
                    workout.getId(),
                    workout.getName(),
                    workout.getDescription(),
                    this.mapToGetExercises(
                        workout.getExercises(),
                        this.findExerciseOptionsByWorkoutId(workout.getId(), exerciseOptions)
                    )
                ))
                .toList()
        );
    }

    default GetWorkoutResponse mapToGetWorkout(
        WorkoutEntity workoutEntity,
        List<ExerciseOptionEntity> exerciseOptions
    ) {
        return new GetWorkoutResponse(
            workoutEntity.getId(),
            workoutEntity.getName(),
            workoutEntity.getDescription(),
                    this.mapToGetExercises(
                        workoutEntity.getExercises(),
                        this.findExerciseOptionsByWorkoutId(workoutEntity.getId(), exerciseOptions)
                    ));
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
}
