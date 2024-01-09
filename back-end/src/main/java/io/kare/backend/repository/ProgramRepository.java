package io.kare.backend.repository;

import io.kare.backend.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProgramRepository extends JpaRepository<ProgramEntity, String> {
    List<ProgramEntity> findAllByUser(UserEntity user);

	List<ProgramEntity> findAllByWorkoutsContaining(WorkoutEntity workoutEntity);
}
