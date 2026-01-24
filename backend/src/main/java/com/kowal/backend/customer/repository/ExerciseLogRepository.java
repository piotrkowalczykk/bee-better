package com.kowal.backend.customer.repository;

import com.kowal.backend.customer.model.ExerciseLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExerciseLogRepository extends JpaRepository<ExerciseLog, Long> {
    List<ExerciseLog> findByExerciseIdAndWorkoutAuthUserId(Long exerciseId, Long authUserId);
}
