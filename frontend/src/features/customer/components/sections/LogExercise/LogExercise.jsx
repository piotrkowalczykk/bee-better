import classes from "./LogExercise.module.css";
import { useState, useEffect } from "react";
import { Exercise } from "../../ui/Exercise/Exercise";
import { CustomInput } from "../../ui/CustomInput/CustomInput";
import { CustomBtn } from "../../../../../components/ui/CustomBtn/CustomBtn";

export const LogExercise = ({
  dayId,
  exercise,
  plannedSets,
  workoutDate,
  onClose,
  onUpdate,
}) => {
  const [sets, setSets] = useState([]);

  const fetchLoggedExercise = async () => {
    try {
      const token = localStorage.getItem("token");
      const formattedDate =
        workoutDate instanceof Date
          ? workoutDate.toISOString().split("T")[0]
          : workoutDate; 

      const response = await fetch(
        `http://localhost:8080/customer/workouts/by-date?date=${formattedDate}`,
        {
          headers: { Authorization: `Bearer ${token}` },
        }
      );

      if (!response.ok) throw new Error("Failed to fetch logged exercises");

      const data = await response.json();

      const loggedExercise = data.exercises.find(
        (ex) => ex.exerciseId === exercise.exercise.id
      );


      
      console.log("siema");
      
      if (loggedExercise) {
        setSets(
          loggedExercise.sets.map((s) => ({
            setNumber: s.setNumber,
            reps: s.reps,
            weight: s.weight,
            rir: s.rir,
          }))
        );
      } else {
        setSets(
          Array.from({ length: plannedSets }, (_, i) => ({
            setNumber: i + 1,
            reps: exercise.reps || "",
            weight: exercise.weight || "",
            rir: exercise.rir || "",
          }))
        );
      }
    } catch (err) {
      console.error(err);
      setSets(
        Array.from({ length: plannedSets }, (_, i) => ({
          setNumber: i + 1,
          reps: exercise.reps || "",
          weight: exercise.weight || "",
          rir: exercise.rir || "",
        }))
      );
    }
  };

  useEffect(() => {
    if (exercise) fetchLoggedExercise();
  }, [exercise, plannedSets, workoutDate]);

  const updateSet = (index, field, value) => {
    setSets((prev) =>
      prev.map((set, i) => (i === index ? { ...set, [field]: value } : set))
    );
  };

  const saveWorkout = async () => {
    try {
      const token = localStorage.getItem("token");

      const response = await fetch(
        "http://localhost:8080/customer/workouts/log-workout",
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
          body: JSON.stringify({
            dayId,
            exerciseId: exercise.exercise.id,
            workoutDate,
            sets: sets.map((s) => ({
              setNumber: s.setNumber,
              reps: Number(s.reps),
              weight: Number(s.weight),
              rir: Number(s.rir),
            })),
          }),
        }
      );

      if (!response.ok) throw new Error("Failed to log exercise");

      if (onUpdate) onUpdate();
      if (onClose) onClose();
    } catch (err) {
      console.error(err);
    }
  };

  return (
    <div className={classes.logExerciseContainer}>
      <Exercise
        image={exercise.exercise.image}
        name={exercise.exercise.name}
        muscleGroup={exercise.exercise.muscleGroup}
      />

      <div className={classes.inputsContainer}>
        {sets.map((set, index) => (
          <div key={set.setNumber} className={classes.setRow}>
            <h4 className={classes.setNumber}>Set {set.setNumber}</h4>

            <CustomInput
              label="Reps"
              type="number"
              value={set.reps}
              onChange={(e) => updateSet(index, "reps", e.target.value)}
            />

            <CustomInput
              label="Weight"
              type="number"
              value={set.weight}
              onChange={(e) => updateSet(index, "weight", e.target.value)}
            />

            <CustomInput
              label="RIR"
              type="number"
              value={set.rir}
              onChange={(e) => updateSet(index, "rir", e.target.value)}
            />
          </div>
        ))}
      </div>

      <CustomBtn
        text="Save"
        bgColor="#209d3d"
        color="white"
        onClick={saveWorkout}
      />
    </div>
  );
};
