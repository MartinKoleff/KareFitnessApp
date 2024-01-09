package io.kare.backend.service;

import io.kare.backend.entity.*;

public interface SelectedWorkoutService {

	void create(WorkoutEntity workoutEntity, UserEntity user);

	void edit(WorkoutEntity workoutEntity, UserEntity user);

	WorkoutEntity getSelectedWorkout(UserEntity user);

	void delete(WorkoutEntity workoutEntity);
}
