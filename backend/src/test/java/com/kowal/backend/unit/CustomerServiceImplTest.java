package com.kowal.backend.unit;

import com.kowal.backend.customer.dto.request.AddRoutineRequest;
import com.kowal.backend.customer.dto.response.RoutineResponse;
import com.kowal.backend.customer.mapper.DayExerciseMapper;
import com.kowal.backend.customer.mapper.DayMapper;
import com.kowal.backend.customer.mapper.ExerciseMapper;
import com.kowal.backend.customer.mapper.RoutineMapper;
import com.kowal.backend.customer.model.Routine;
import com.kowal.backend.customer.repository.*;
import com.kowal.backend.customer.service.CustomerServiceImpl;
import com.kowal.backend.exception.customer.NotRoutineOwnerException;
import com.kowal.backend.security.model.AuthUser;
import com.kowal.backend.security.repository.AuthUserRepository;
import com.kowal.backend.util.FileStorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceImplTest {

    @Mock private AuthUserRepository authUserRepository;
    @Mock private RoutineRepository routineRepository;
    @Mock private RoutineMapper routineMapper;
    @Mock private DayRepository dayRepository;
    @Mock private DayMapper dayMapper;
    @Mock private ExerciseRepository exerciseRepository;
    @Mock private ExerciseMapper exerciseMapper;
    @Mock private FileStorageService fileStorageService;
    @Mock private DayExerciseRepository dayExerciseRepository;
    @Mock private DayExerciseMapper dayExerciseMapper;
    @Mock private WorkoutLogRepository workoutLogRepository;
    @Mock private RoutineLogRepository routineLogRepository;
    @Mock private ExerciseLogRepository exerciseLogRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private AuthUser testUser;
    private final String EMAIL = "test@mail.com";

    @BeforeEach
    void setUp() {
        testUser = new AuthUser();
        testUser.setId(1L);
        testUser.setEmail(EMAIL);
    }


    @Test
    void shouldReturnAllRoutinesForUser() {
        Routine routine = new Routine();
        routine.setName("FBW");
        when(authUserRepository.findByEmail(EMAIL)).thenReturn(Optional.of(testUser));
        when(routineRepository.findByAuthUserId(testUser.getId())).thenReturn(List.of(routine));

        customerService.getAllRoutines(EMAIL);

        verify(routineRepository).findByAuthUserId(testUser.getId());
        verify(routineMapper).mapRoutineToRoutineResponse(routine);
    }

    @Test
    void shouldAddRoutineCorrectly() {
        AddRoutineRequest req = new AddRoutineRequest();
        req.setName("Morning Routine");
        req.setFrequency("1,3,5");

        when(authUserRepository.findByEmail(EMAIL)).thenReturn(Optional.of(testUser));
        when(routineMapper.mapStringFrequencyToIntegerSet("1,3,5"))
                .thenReturn(Set.of(1, 3, 5));

        customerService.addRoutine(req, EMAIL);

        verify(routineRepository).save(argThat(routine ->
                routine.getName().equals("Morning Routine") &&
                        routine.getAuthUser().equals(testUser) &&
                        routine.getFrequency().containsAll(Set.of(1, 3, 5))
        ));
    }

    @Test
    void shouldThrowExceptionWhenUserIsNotOwnerOfRoutine() {
        Long routineId = 99L;
        AuthUser stranger = new AuthUser();
        stranger.setId(2L);

        Routine routine = new Routine();
        routine.setId(routineId);
        routine.setAuthUser(stranger);

        when(authUserRepository.findByEmail(EMAIL)).thenReturn(Optional.of(testUser));
        when(routineRepository.findById(routineId)).thenReturn(Optional.of(routine));

        assertThrows(NotRoutineOwnerException.class, () -> {
            customerService.getRoutineById(routineId, EMAIL);
        });
    }
}
