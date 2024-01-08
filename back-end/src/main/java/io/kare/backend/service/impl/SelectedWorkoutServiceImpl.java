package io.kare.backend.service.impl;

import io.kare.backend.entity.*;
import io.kare.backend.repository.SelectedWorkoutRepository;
import io.kare.backend.service.SelectedWorkoutService;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class SelectedWorkoutServiceImpl implements SelectedWorkoutService {

	private final SelectedWorkoutRepository selectedWorkoutRepository;

	public SelectedWorkoutServiceImpl(SelectedWorkoutRepository selectedWorkoutRepository) {
		this.selectedWorkoutRepository = selectedWorkoutRepository;
	}

	@Override
	public void create(WorkoutEntity workoutEntity, UserEntity user) {
		SelectedWorkoutEntity selectedWorkoutEntity = new SelectedWorkoutEntity();
		selectedWorkoutEntity.setWorkout(workoutEntity);
		selectedWorkoutEntity.setUser(user);
		this.selectedWorkoutRepository.save(selectedWorkoutEntity);
	}

	@Override
	public void edit(WorkoutEntity workoutEntity, UserEntity user) {
		Optional<SelectedWorkoutEntity> selectedWorkoutEntityOptional = this.selectedWorkoutRepository.findByUser(
			user);
		selectedWorkoutEntityOptional.ifPresentOrElse(
			selectedWorkoutEntity -> {
				selectedWorkoutEntity.setWorkout(workoutEntity);
				this.selectedWorkoutRepository.save(selectedWorkoutEntity);
			},
			() -> this.create(workoutEntity, user)
		);

	}

	@Override
	public WorkoutEntity getSelectedWorkout(UserEntity user) {
		return this.selectedWorkoutRepository.findByUser(user)
			.orElseThrow(() -> new RuntimeException("No selected workout found"))
			.getWorkout();
	}

	@Override
	public void delete(WorkoutEntity workoutEntity) {
		Optional<SelectedWorkoutEntity> selectedWorkoutEntityOptional = this.selectedWorkoutRepository.findByWorkout(
			workoutEntity);
		selectedWorkoutEntityOptional.ifPresentOrElse(
			this.selectedWorkoutRepository::delete,
			() -> {}
		);
	}
}
