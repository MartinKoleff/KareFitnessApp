package io.kare.backend.repository;

import feign.Param;
import io.kare.backend.entity.*;
import org.springframework.data.jpa.repository.*;

import java.util.List;

public interface ExerciseRepository extends JpaRepository<ExerciseEntity, String> {
    List<ExerciseEntity> findAllByUser(UserEntity user);
    List<ExerciseEntity> findAllByUserId(String userId);

    @Query("SELECT w FROM ExerciseEntity w WHERE w.id IN :ids AND w.user = :user")
    List<ExerciseEntity> findAllByIdsAndUser(
        @Param("ids") List<String> ids,
        @Param("user") UserEntity user
    );
}
