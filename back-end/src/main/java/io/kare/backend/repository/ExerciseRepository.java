package io.kare.backend.repository;

import io.kare.backend.entity.*;
import java.util.*;
import org.springframework.data.jpa.repository.*;

import org.springframework.data.repository.query.Param;

public interface ExerciseRepository extends JpaRepository<ExerciseEntity, String> {
    List<ExerciseEntity> findAllByUser(UserEntity user);
    List<ExerciseEntity> findAllByUserId(String userId);

    @Query("SELECT w FROM ExerciseEntity w WHERE w.id IN :ids AND w.user = :user")
    List<ExerciseEntity> findAllByIdsAndUser(
        @Param("ids") List<String> ids,
        @Param("user") UserEntity user
    );

    Optional<ExerciseEntity> findByIdAndUser(
        String id,
        UserEntity user
    );

	Optional<ExerciseEntity> findByNameAndUser(String name, UserEntity user);

	@Query("SELECT e FROM ExerciseEntity e WHERE :workout MEMBER OF e.workouts")
	List<ExerciseEntity> findByWorkout(WorkoutEntity workout);
}
