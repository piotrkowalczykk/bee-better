package com.kowal.backend.customer.repository;

import com.kowal.backend.customer.model.WorkoutLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface WorkoutLogRepository extends JpaRepository<WorkoutLog, Long> {

    List<WorkoutLog> findByAuthUserId(Long authUserId);
    Optional<WorkoutLog> findByIdAndAuthUserId(Long id, Long userId);
    Optional<WorkoutLog> findByAuthUserIdAndWorkoutDate(Long userId, LocalDate workoutDate);
}