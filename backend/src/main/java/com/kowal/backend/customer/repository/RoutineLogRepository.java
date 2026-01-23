package com.kowal.backend.customer.repository;

import com.kowal.backend.customer.dto.response.RoutineLogResponse;
import com.kowal.backend.customer.model.RoutinesLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoutineLogRepository extends JpaRepository<RoutinesLog, Long> {

    Optional<RoutinesLog> findByRoutineIdAndAuthUserIdAndLogDate(
            Long routineId,
            Long authUserId,
            LocalDate logDate
    );

    @Query("""
    SELECT new com.kowal.backend.customer.dto.response.RoutineLogResponse(
        r.logDate,
        r.routine.id,
        r.value
    )
    FROM routine_logs r
    WHERE r.authUser.id = :userId
      AND r.logDate = :date
""")
    List<RoutineLogResponse> findLogsForDay(
            @Param("userId") Long userId,
            @Param("date") LocalDate date
    );
}
