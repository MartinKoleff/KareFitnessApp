package io.kare.backend.repository;

import io.kare.backend.entity.ProgramEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgramRepository extends JpaRepository<ProgramEntity, String> {
}
