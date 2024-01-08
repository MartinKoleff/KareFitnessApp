package io.kare.backend.repository;

import io.kare.backend.entity.*;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SelectedWorkoutRepository extends JpaRepository<SelectedWorkoutEntity, String> {

	Optional<SelectedWorkoutEntity> findByUser(UserEntity user);

	Optional<SelectedWorkoutEntity> findByWorkout(WorkoutEntity workoutEntity);
}
