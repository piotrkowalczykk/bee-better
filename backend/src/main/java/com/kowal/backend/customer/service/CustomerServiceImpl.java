package com.kowal.backend.customer.service;

import com.kowal.backend.customer.dto.request.*;
import com.kowal.backend.customer.dto.response.*;
import com.kowal.backend.customer.mapper.DayExerciseMapper;
import com.kowal.backend.customer.mapper.DayMapper;
import com.kowal.backend.customer.mapper.ExerciseMapper;
import com.kowal.backend.customer.mapper.RoutineMapper;
import com.kowal.backend.customer.model.*;
import com.kowal.backend.customer.repository.*;
import com.kowal.backend.exception.customer.*;
import com.kowal.backend.security.model.AuthUser;
import com.kowal.backend.security.repository.AuthUserRepository;
import com.kowal.backend.util.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final AuthUserRepository authUserRepository;
    private final RoutineRepository routineRepository;
    private final RoutineMapper routineMapper;
    private final DayRepository dayRepository;
    private final DayMapper dayMapper;
    private final ExerciseRepository exerciseRepository;
    private final ExerciseMapper exerciseMapper;
    private final FileStorageService fileStorageService;
    private final DayExerciseRepository dayExerciseRepository;
    private final DayExerciseMapper dayExerciseMapper;
    private final WorkoutLogRepository workoutLogRepository;

    @Autowired
    public CustomerServiceImpl(AuthUserRepository authUserRepository, RoutineRepository routineRepository, RoutineMapper routineMapper,
                               DayRepository dayRepository, DayMapper dayMapper, ExerciseRepository exerciseRepository, ExerciseMapper exerciseMapper,
                                FileStorageService fileStorageService, DayExerciseRepository dayExerciseRepository, DayExerciseMapper dayExerciseMapper,
                                WorkoutLogRepository workoutLogRepository) {
        this.authUserRepository = authUserRepository;
        this.routineRepository = routineRepository;
        this.routineMapper = routineMapper;
        this.dayRepository = dayRepository;
        this.dayMapper = dayMapper;
        this.exerciseRepository = exerciseRepository;
        this.exerciseMapper = exerciseMapper;
        this.fileStorageService = fileStorageService;
        this.dayExerciseRepository = dayExerciseRepository;
        this.dayExerciseMapper = dayExerciseMapper;
        this.workoutLogRepository = workoutLogRepository;
    }

    @Override
    public RoutineResponse addRoutine(AddRoutineRequest addRoutineRequest, String userEmail) {
        AuthUser authUser = findAuthUserByEmail(userEmail);

        Routine newRoutine = new Routine();
        newRoutine.setAuthUser(authUser);
        newRoutine.setName(addRoutineRequest.getName());
        newRoutine.setIcon(addRoutineRequest.getIcon());
        newRoutine.setColor(addRoutineRequest.getColor());
        newRoutine.setScope(addRoutineRequest.getScope());
        newRoutine.setUnits(addRoutineRequest.getUnits());
        newRoutine.setDashboardPriority(addRoutineRequest.getDashboardPriority());

        Set<Integer> frequency = routineMapper.mapStringFrequencyToIntegerSet(addRoutineRequest.getFrequency());

        newRoutine.setFrequency(frequency);

        routineRepository.save(newRoutine);
        return routineMapper.mapRoutineToRoutineResponse(newRoutine);
    }

    @Override
    public RoutineResponse getRoutineById(Long routineId, String userEmail) {
        AuthUser authUser = findAuthUserByEmail(userEmail);
        Routine routine = findRoutineAndVerifyOwner(routineId, authUser.getId());
        return routineMapper.mapRoutineToRoutineResponse(routine);
    }

    @Override
    public List<RoutineResponse> getAllRoutines(String userEmail) {
        AuthUser authUser = findAuthUserByEmail(userEmail);
        List<Routine> routines = routineRepository.findByAuthUserId(authUser.getId());

        return routines.stream()
                .map(routineMapper::mapRoutineToRoutineResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RoutineResponse updateRoutine(Long routineId, UpdateRoutineRequest updateRoutineRequest, String userEmail) {
        AuthUser authUser = findAuthUserByEmail(userEmail);
        Routine routineToUpdate = findRoutineAndVerifyOwner(routineId, authUser.getId());

        if (updateRoutineRequest.getName() != null) routineToUpdate.setName(updateRoutineRequest.getName());
        if (updateRoutineRequest.getIcon() != null) routineToUpdate.setIcon(updateRoutineRequest.getIcon());
        if (updateRoutineRequest.getColor() != null) routineToUpdate.setColor(updateRoutineRequest.getColor());
        if (updateRoutineRequest.getScope() != null) routineToUpdate.setScope(updateRoutineRequest.getScope());
        if (updateRoutineRequest.getUnits() != null) routineToUpdate.setUnits(updateRoutineRequest.getUnits());
        if (updateRoutineRequest.getDashboardPriority() != null) routineToUpdate.setDashboardPriority(updateRoutineRequest.getDashboardPriority());
        if (updateRoutineRequest.getFrequency() != null) routineToUpdate.setFrequency(routineMapper.mapStringFrequencyToIntegerSet(updateRoutineRequest.getFrequency()));
        if(updateRoutineRequest.getValue() != null) routineToUpdate.setValue(updateRoutineRequest.getValue());

        routineRepository.save(routineToUpdate);
        return routineMapper.mapRoutineToRoutineResponse(routineToUpdate);
    }

    @Override
    public RoutineResponse deleteRoutine(Long routineId, String userEmail) {
        AuthUser authUser = findAuthUserByEmail(userEmail);
        Routine routineToDelete= findRoutineAndVerifyOwner(routineId, authUser.getId());
        routineRepository.deleteById(routineToDelete.getId());
        return routineMapper.mapRoutineToRoutineResponse(routineToDelete);
    }

    @Override
    public DayResponse addDay(AddDayRequest request, String userEmail) {
        AuthUser authUser = findAuthUserByEmail(userEmail);

        Day day = new Day();
        day.setName(request.getName());
        day.setColor(request.getColor());
        day.setSecondaryColor(request.getSecondaryColor());
        day.setDescription(request.getDescription());
        day.setAuthUser(authUser);
        day.setIcon(request.getIcon());
        if(request.getFrequency() != null){
            Set<Integer> frequency = routineMapper.mapStringFrequencyToIntegerSet(request.getFrequency());
            day.setFrequency(frequency);
        }

        List<DayExercise> dayExercises = dayExerciseMapper.mapToListOfDayExercises(request.getExercises(), day, exerciseRepository);
        day.setWorkoutExercises(dayExercises);

        dayRepository.save(day);
        return dayMapper.mapDayToDayResponse(day);
    }

    @Override
    public List<DayResponse> getAllDays(String userEmail) {
        AuthUser authUser = findAuthUserByEmail(userEmail);

        return dayRepository.findByAuthUserId(authUser.getId())
                .stream()
                .map(dayMapper::mapDayToDayResponse)
                .collect(Collectors.toList());
    }

    @Override
    public DayResponse getDayById(Long dayId, String userEmail) {
        AuthUser authUser = findAuthUserByEmail(userEmail);
        Day day = findDayAndVerifyOwner(dayId, authUser.getId());
        return dayMapper.mapDayToDayResponse(day);
    }

    @Override
    @Transactional
    public DayResponse updateDay(Long dayId, UpdateDayRequest updateDayRequest, String userEmail) {
        AuthUser authUser = findAuthUserByEmail(userEmail);
        Day day = findDayAndVerifyOwner(dayId, authUser.getId());

        if (updateDayRequest.getName() != null) day.setName(updateDayRequest.getName());
        if (updateDayRequest.getColor() != null) day.setColor(updateDayRequest.getColor());
        if (updateDayRequest.getSecondaryColor() != null) day.setSecondaryColor(updateDayRequest.getSecondaryColor());
        if (updateDayRequest.getIcon() != null) day.setIcon(updateDayRequest.getIcon());
        if (updateDayRequest.getExercises() != null) {
            List<DayExercise> dayExercises = dayExerciseMapper
                    .mapToListOfDayExercises(updateDayRequest.getExercises(), day, exerciseRepository);

            day.getWorkoutExercises().clear();
            day.getWorkoutExercises().addAll(dayExercises);
        }
        if (updateDayRequest.getDescription() != null) day.setDescription(updateDayRequest.getDescription());
        if (updateDayRequest.getFrequency() != null){
            Set<Integer> frequency = routineMapper.mapStringFrequencyToIntegerSet(updateDayRequest.getFrequency());
            day.setFrequency(frequency);
        }

        dayRepository.save(day);
        return dayMapper.mapDayToDayResponse(day);
    }

    @Override
    public DayResponse deleteDay(Long dayId, String userEmail) {
        AuthUser authUser = findAuthUserByEmail(userEmail);
        Day day = findDayAndVerifyOwner(dayId, authUser.getId());

        dayRepository.delete(day);
        return dayMapper.mapDayToDayResponse(day);
    }

    @Override
    public ExerciseResponse addExercise(AddExerciseRequest request, String userEmail) {
        AuthUser authUser = findAuthUserByEmail(userEmail);

        Exercise exercise = new Exercise();
        exercise.setName(request.getName());
        exercise.setMuscleGroup(request.getMuscleGroup());
        exercise.setAuthUser(authUser);
        exercise.setEquipment(request.getEquipment());

        if (request.getImage() != null && !request.getImage().isEmpty()) {
            String imageUrl = fileStorageService.storeExerciseImage(request.getImage());
            exercise.setImage(imageUrl);
        }

        exerciseRepository.save(exercise);
        return exerciseMapper.mapExerciseToExerciseResponse(exercise);
    }

    @Override
    public List<ExerciseResponse> getAllExercises(String userEmail) {
        AuthUser authUser = findAuthUserByEmail(userEmail);

        return exerciseRepository.findByAuthUserIdOrAuthUserIsNull(authUser.getId())
                .stream()
                .map(exerciseMapper::mapExerciseToExerciseResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ExerciseResponse getExerciseById(Long exerciseId, String userEmail) {
        AuthUser authUser = findAuthUserByEmail(userEmail);
        Exercise exercise = findExerciseAndVerifyOwner(exerciseId, authUser.getId());
        return exerciseMapper.mapExerciseToExerciseResponse(exercise);
    }

    @Override
    @Transactional
    public ExerciseResponse updateExercise(Long exerciseId, UpdateExerciseRequest request, String userEmail) {
        AuthUser authUser = findAuthUserByEmail(userEmail);
        Exercise exercise = findExerciseAndVerifyOwner(exerciseId, authUser.getId());

        if (request.getName() != null) exercise.setName(request.getName());
        if (request.getMuscleGroup() != null) exercise.setMuscleGroup(request.getMuscleGroup());

        if (request.getImage() != null && !request.getImage().isEmpty()) {
            fileStorageService.deleteFile(exercise.getImage());
            String imageUrl = fileStorageService.storeExerciseImage(request.getImage());
            exercise.setImage(imageUrl);
        }

        exerciseRepository.save(exercise);
        return exerciseMapper.mapExerciseToExerciseResponse(exercise);
    }

    @Override
    public ExerciseResponse deleteExercise(Long exerciseId, String userEmail) {
        AuthUser authUser = findAuthUserByEmail(userEmail);
        Exercise exercise = findExerciseAndVerifyOwner(exerciseId, authUser.getId());

        exerciseRepository.delete(exercise);
        return exerciseMapper.mapExerciseToExerciseResponse(exercise);
    }

    @Override
    public List<Equipment> getAllEquipment(String userEmail) {
        AuthUser authUser = findAuthUserByEmail(userEmail);
        return List.of(Equipment.values());
    }

    @Override
    public List<WorkoutLog> getAllWorkoutLogs(String userEmail) {
        AuthUser authUser = findAuthUserByEmail(userEmail);
        return workoutLogRepository.findByAuthUserId(authUser.getId());
    }

    @Override
    public WorkoutLog logWorkout(String userEmail, LogWorkoutRequest logWorkoutRequest) {
        AuthUser authUser = findAuthUserByEmail(userEmail);
        WorkoutLog workoutLog = workoutLogRepository.findByAuthUserIdAndWorkoutDate(authUser.getId(), logWorkoutRequest.getWorkoutDate())
                .orElseGet(()->createWorkout(authUser, logWorkoutRequest));

        upsertExerciseLog(workoutLog, logWorkoutRequest);

        return workoutLogRepository.save(workoutLog);
    }

    @Override
    public WorkoutDayResponse getWorkoutByDate(String userEmail, LocalDate date) {

        AuthUser user = findAuthUserByEmail(userEmail);

        WorkoutLog workoutLog = workoutLogRepository
                .findByAuthUserIdAndWorkoutDate(user.getId(), date)
                .orElseThrow(() -> new WorkoutLogNotFoundException(date));

        List<ExerciseLogResponse> exercises = workoutLog.getExerciseLogs().stream()
                .map(exLog -> new ExerciseLogResponse(
                        exLog.getExercise().getId(),
                        exLog.getExercise().getName(),
                        exLog.getExercise().getMuscleGroup(),
                        exLog.getExercise().getImage(),
                        exLog.getSets().stream()
                                .map(s -> new SetLogResponse(
                                        s.getSetNumber(),
                                        s.getReps(),
                                        s.getWeight(),
                                        s.getRir()
                                ))
                                .toList()
                ))
                .toList();

        return new WorkoutDayResponse(workoutLog.getWorkoutDate(), exercises);
    }

    @Override
    public DayResponse getDayForDate(String userEmail, LocalDate date) {
        AuthUser user = findAuthUserByEmail(userEmail);

        int jsDay = date.getDayOfWeek().getValue();

        return dayRepository.findByAuthUserId(user.getId()).stream()
                .filter(day -> day.getFrequency().contains(jsDay))
                .findFirst()
                .map(day -> dayMapper.mapDayToDayResponse(day))
                .orElseThrow(()-> new DayNotFoundException(date));
    }

    private WorkoutLog createWorkout(AuthUser user, LogWorkoutRequest logWorkoutRequest) {

        Day day = dayRepository.findById(logWorkoutRequest.getDayId())
                .orElseThrow(() -> new DayNotFoundException(logWorkoutRequest.getDayId()));

        WorkoutLog workoutLog = new WorkoutLog();
        workoutLog.setAuthUser(user);
        workoutLog.setDay(day);
        workoutLog.setWorkoutDate(logWorkoutRequest.getWorkoutDate());
        workoutLog.setExerciseLogs(new ArrayList<>());

        return workoutLog;
    }

    private void upsertExerciseLog(WorkoutLog workoutLog, LogWorkoutRequest req) {

        ExerciseLog exerciseLog = workoutLog.getExerciseLogs().stream()
                .filter(e -> e.getExercise().getId().equals(req.getExerciseId()))
                .findFirst()
                .orElseGet(() -> {
                    Exercise exercise = exerciseRepository.findById(req.getExerciseId())
                            .orElseThrow(() -> new ExerciseNotFoundException(req.getExerciseId()));

                    ExerciseLog newLog = new ExerciseLog();
                    newLog.setWorkout(workoutLog);
                    newLog.setExercise(exercise);
                    newLog.setSets(new ArrayList<>());

                    workoutLog.getExerciseLogs().add(newLog);
                    return newLog;
                });

        exerciseLog.getSets().clear();

        for (SetLogRequest s : req.getSets()) {
            SetLog setLog = new SetLog();
            setLog.setSetNumber(s.getSetNumber());
            setLog.setReps(s.getReps());
            setLog.setWeight(s.getWeight());
            setLog.setRir(s.getRir());
            setLog.setExerciseLog(exerciseLog);
            exerciseLog.getSets().add(setLog);
        }
    }

    @Override
    @Transactional
    public WorkoutLog deleteWorkoutLog(String userEmail, Long workoutLogId) {

        AuthUser authUser = findAuthUserByEmail(userEmail);

        WorkoutLog workoutLog = workoutLogRepository
                .findByIdAndAuthUserId(workoutLogId, authUser.getId())
                .orElseThrow(() -> new WorkoutLogNotFoundException(workoutLogId));

        workoutLogRepository.delete(workoutLog);

        return workoutLog;
    }

    public void deleteWorkoutByDate(String userEmail, LocalDate date) {

        AuthUser user = findAuthUserByEmail(userEmail);

        WorkoutLog workout = workoutLogRepository
                .findByAuthUserIdAndWorkoutDate(user.getId(), date)
                .orElseThrow(() -> new WorkoutLogNotFoundException(date));

        workoutLogRepository.delete(workout);
    }

    private AuthUser findAuthUserByEmail(String userEmail) {
        return authUserRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + userEmail));
    }

    private Routine findRoutineAndVerifyOwner(Long routineId, Long authUserId) {
        Routine routine = routineRepository.findById(routineId)
                .orElseThrow(() -> new RoutineNotFoundException(routineId));

        if (!routine.getAuthUser().getId().equals(authUserId)) {
            throw new NotRoutineOwnerException(routineId);
        }
        return routine;
    }

    private Day findDayAndVerifyOwner(Long dayId, Long authUserId) {
        Day day = dayRepository.findById(dayId)
                .orElseThrow(() -> new DayNotFoundException(dayId));

        if (!day.getAuthUser().getId().equals(authUserId)) {
            throw new NotDayOwnerException(dayId);
        }
        return day;
    }

    private Exercise findExerciseAndVerifyOwner(Long exerciseId, Long authUserId) {
        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new ExerciseNotFoundException(exerciseId));

        if (!exercise.getAuthUser().getId().equals(authUserId)) {
            throw new NotExerciseOwnerException(exerciseId);
        }
        return exercise;
    }
}
