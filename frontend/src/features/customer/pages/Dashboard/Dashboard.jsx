import { Layout } from "../../components/layout/Layout/Layout";
import classes from "./Dashboard.module.css";
import { useState, useEffect } from "react";
import { RoutineCard } from "../../components/ui/RoutineCard/RoutineCard";
import {
  AddIcon,
  QuestionMarkIcon,
  ArrorLeft,
  ArrorRight,
  ChartIcon,
} from "../../../../app/icons/Icons";
import { IconsData } from "../../../../app/icons/IconsData";
import { LogExercise } from "../../components/sections/LogExercise/LogExercise";
import { LogRoutine } from "../../components/sections/LogRoutine/LogRoutine";
import { ModalWindow } from "../../../../components/layout/ModalWindow/ModalWindow";
import { Exercise } from "../../components/ui/Exercise/Exercise";
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from 'recharts';

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
  const [loggedExercises, setLoggedExercises] = useState([]);

  const [chartData, setChartData] = useState([]);
  const [selectedChartExercise, setSelectedChartExercise] = useState(null);
  const [dateRange, setDateRange] = useState(30);

  useEffect(() => {
    fetchUserRoutines();
    fetchUserDays();
    fetchLoggedExercises();
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

  useEffect(() => {
    if (selectedChartExercise) {
      fetchChartData(selectedChartExercise.id, dateRange);
    }
  }, [dateRange, selectedChartExercise]);

  const handleExerciseClickForChart = (exercise) => {
    setSelectedChartExercise(exercise);
    fetchChartData(exercise.id, dateRange);
  };

  const fetchChartData = async (exerciseId, days) => {
    const token = localStorage.getItem("token");
    const to = new Date().toISOString().split('T')[0];
    const fromDate = new Date();
    fromDate.setDate(fromDate.getDate() - days);
    const from = fromDate.toISOString().split('T')[0];

    try {
      const response = await fetch(
        `http://localhost:8080/customer/workouts/logs/${exerciseId}/1rm?from=${from}&to=${to}`,
        { headers: { Authorization: `Bearer ${token}` } }
      );
      if (response.ok) {
        const data = await response.json();
        setChartData(data);
      }
    } catch (error) {
      console.error("Error fetching chart data:", error);
    }
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
    routineLogs.map((log) => [log.routineId, log.value]),
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

  const fetchLoggedExercises = async () => {
    const token = localStorage.getItem("token");
    try {
      const response = await fetch(
        `http://localhost:8080/customer/workouts/logs/exercises`,
        {
          headers: { Authorization: `Bearer ${token}` },
        },
      );
      if (!response.ok) throw new Error("Failed to fetch logged exercises");
      const data = await response.json();
      setLoggedExercises(data);
    } catch (error) {
      console.error(error);
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
                <p style={{color: '#666'}} >No routines for this day</p>
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
            <h2 className={classes.containerTitle}>Overview {selectedChartExercise ? `- ${selectedChartExercise.name}` : ""}</h2>
            {selectedChartExercise ? (
              <>
                
                <div style={{ width: '100%', height: 221, marginTop: '20px' }}>
                  <ResponsiveContainer width="100%" height="100%">
                    <LineChart data={chartData}>
                      <CartesianGrid strokeDasharray="3 3" stroke="#444" />
                      <XAxis dataKey="date" stroke="#888" fontSize={12} />
                      <YAxis stroke="#888" fontSize={12} unit="kg" />
                      <Tooltip 
                        contentStyle={{ backgroundColor: '#222', border: 'none', borderRadius: '8px' }}
                        itemStyle={{ color: '#fff' }}
                      />
                      <Line 
                        type="monotone" 
                        dataKey="oneRm" 
                        stroke="#82ca9d" 
                        strokeWidth={2}
                        dot={{ r: 4 }}
                        activeDot={{ r: 8 }}
                      />
                    </LineChart>
                  </ResponsiveContainer>
                </div>
                <div className={classes.chartControls}>
                  {[7, 30, 90].map(days => (
                    <button 
                      key={days}
                      className={dateRange === days ? classes.activeRange : ""}
                      onClick={() => setDateRange(days)}
                    >
                      {days === 7 ? "1W" : days === 30 ? "1M" : "3M"}
                    </button>
                  ))}
                </div>
              </>
            ) : (
              <p style={{color: '#666', textAlign: 'center', marginTop: '40px'}}>
                <ChartIcon />
                <br/>
                Select an exercise from "Your exercises" to see progress chart
              </p>
            )}
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
          <div className={classes.manageContainer}>
            <h2 className={classes.containerTitle}>Your exercises</h2>
            <div className={classes.manageInnerContainer}>
              {loadingExercises ? (
                <p>Loading exercises...</p>
              ) : loggedExercises.length === 0 ? (
                <p style={{color:` #666`}}>No logged exercises</p>
              ) : (
                loggedExercises.map((exercise) => (
                  <Exercise
                    key={exercise.id}
                    image={exercise.image}
                    name={exercise.name}
                    muscleGroup={exercise.muscleGroup}
                    onClick={() => handleExerciseClickForChart(exercise)}
                  />
                ))
              )}
            </div>
          </div>
        </div>
      </div>
    </Layout>
  );
};
