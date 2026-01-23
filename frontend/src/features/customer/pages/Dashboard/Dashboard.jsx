import { Layout } from "../../components/layout/Layout/Layout";
import classes from "./Dashboard.module.css";
import { useState, useEffect } from "react";
import { RoutineCard } from "../../components/ui/RoutineCard/RoutineCard";
import {
  AddIcon,
  QuestionMarkIcon,
  ArrorLeft,
  ArrorRight,
} from "../../../../app/icons/Icons";
import { IconsData } from "../../../../app/icons/IconsData";
import { LogExercise } from "../../components/sections/LogExercise/LogExercise";
import { LogRoutine } from "../../components/sections/LogRoutine/LogRoutine";
import { ModalWindow } from "../../../../components/layout/ModalWindow/ModalWindow";
import { Exercise } from "../../components/ui/Exercise/Exercise";

export const Dashboard = () => {
  const [loadingRoutines, setLoadingRoutines] = useState(true);
  const [loadingExercises, setLoadingExercises] = useState(true);
  const [userRoutines, setUserRoutines] = useState([]);
  const [showRoutineModal, setShowRoutineModal] = useState(false);
  const [selectedRoutineId, setSelectedRoutineId] = useState(null);
  const [selectedExercise, setSelectedExercise] = useState(null);
  const [showExerciseModal, setShowExerciseModal] = useState(false);
  const [userDays, setUserDays] = useState([]);
  const [currentDate, setCurrentDate] = useState(() => new Date());
  const [routineLogs, setRoutineLogs] = useState([]);

  useEffect(() => {
    fetchUserRoutines();
    fetchUserDays();
  }, []);

  useEffect(() => {
    fetchRoutineLogsForDay();
  }, [currentDate]);

  const formatDate = (date) => date.toLocaleDateString("pl-PL");

  const jsDay = currentDate.getDay();
  const dayOfWeek = jsDay === 0 ? 7 : jsDay;

  const currentDay = userDays.find((day) => day.frequency?.includes(dayOfWeek));
  const userExercises = currentDay?.exercises || [];

  const routinesForDay = userRoutines.filter((routine) =>
    routine.frequency?.includes(dayOfWeek),
  );

  const changeDay = (direction) => {
    setCurrentDate((prev) => {
      const newDate = new Date(prev);
      newDate.setDate(prev.getDate() + direction);
      return newDate;
    });
  };

  const fetchUserRoutines = async () => {
    const token = localStorage.getItem("token");
    try {
      const response = await fetch("http://127.0.0.1:8080/customer/routines", {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      if (!response.ok) throw new Error("Failed to fetch routines");

      const data = (await response.json()).sort(
        (a, b) => a.dashboardPriority - b.dashboardPriority,
      );

      setUserRoutines(data);
    } catch (error) {
      console.error("Error fetching routines:", error);
    } finally {
      setLoadingRoutines(false);
    }
  };

  const fetchRoutineLogsForDay = async () => {
    const token = localStorage.getItem("token");
    const dateStr = currentDate.toISOString().split("T")[0];

    const res = await fetch(
      `http://127.0.0.1:8080/customer/routines/logs?date=${dateStr}`,
      { headers: { Authorization: `Bearer ${token}` } },
    );

    if (res.ok) {
      const data = await res.json();
      setRoutineLogs(data);
    }
  };

  const logsMap = Object.fromEntries(
    routineLogs.map((log) => [log.routineId, log.value])
  );

  const fetchUserDays = async () => {
    const token = localStorage.getItem("token");
    try {
      const response = await fetch(`http://127.0.0.1:8080/customer/days`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      if (!response.ok) throw new Error("Failed to fetch days");
      const data = await response.json();
      setUserDays(data);
    } catch (error) {
      console.error(error);
    } finally {
      setLoadingExercises(false);
    }
  };

  return (
    <Layout>
      {showRoutineModal && (
        <ModalWindow
          onClose={() => setShowRoutineModal(false)}
          title="Log Routine"
        >
          <LogRoutine
            routineId={selectedRoutineId}
            logDate={currentDate}
            onClose={() => setShowRoutineModal(false)}
            onUpdate={() => fetchRoutineLogsForDay()}
          />
        </ModalWindow>
      )}

      {showExerciseModal && (
        <ModalWindow
          onClose={() => setShowExerciseModal(false)}
          title="Log Exercise"
        >
          <LogExercise
            dayId={currentDay.id}
            exercise={selectedExercise.exercise}
            plannedSets={selectedExercise.exercise.sets}
            workoutDate={currentDate}
            onClose={() => setShowExerciseModal(false)}
            onUpdate={() => fetchUserDays()}
          />
        </ModalWindow>
      )}

      <div className={classes.dashboardContainer}>
        <div className={classes.dashboardLeftContainer}>
          <div className={classes.routinesContainer}>
            <h2 className={classes.containerTitle}>Today's routines</h2>
            <div className={classes.cardsContainer}>
              {loadingRoutines ? (
                <p>Loading routines...</p>
              ) : routinesForDay.length === 0 ? (
                <p>No routines for this day</p>
              ) : (
                routinesForDay.map((routine) => {
                  const loggedValue = logsMap[routine.id] || 0;

                  return (
                    <RoutineCard
                      key={routine.id}
                      title={routine.name}
                      Icon={IconsData[routine.icon] || QuestionMarkIcon}
                      userProgres={loggedValue}
                      scope={routine.scope}
                      unit={routine.units}
                      value={(loggedValue / routine.scope) * 100}
                      pathColor={routine.color}
                      trailColor="#2b2b27"
                      onClick={() => {
                        setSelectedRoutineId(routine.id);
                        setShowRoutineModal(true);
                      }}
                    />
                  );
                })
              )}
            </div>
          </div>
          <div className={classes.overviewContainer}>
            <h2 className={classes.containerTitle}>Overview</h2>
          </div>
        </div>
        <div className={classes.dashboardRightContainer}>
          <div className={classes.exercisesContainer}>
            <div className={classes.dataNav}>
              <button className={classes.cardBtn} onClick={() => changeDay(-1)}>
                <ArrorLeft />
              </button>
              <div className={classes.dataNavText}>
                <h2>{formatDate(currentDate)}</h2>
                <h5>{currentDay?.name || "Rest day"}</h5>
              </div>
              <button className={classes.cardBtn} onClick={() => changeDay(1)}>
                <ArrorRight />
              </button>
            </div>
            <div className={classes.exercisesListContainer}>
              {!currentDay ? (
                <p></p>
              ) : (
                userExercises.map((exercise) => (
                  <Exercise
                    key={exercise.exercise.id}
                    image={exercise.exercise.image}
                    name={exercise.exercise.name}
                    muscleGroup={exercise.exercise.muscleGroup}
                    onClick={() => {
                      console.log("dashid");
                      console.log(exercise.id);
                      setSelectedExercise({ exercise });
                      setShowExerciseModal(true);
                    }}
                  />
                ))
              )}
            </div>
          </div>
          <div className={classes.manageContainer}>siema</div>
        </div>
      </div>
    </Layout>
  );
};
