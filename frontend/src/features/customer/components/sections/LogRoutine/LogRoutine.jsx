import classes from "./LogRoutine.module.css";
import { useState, useEffect } from "react";
import { RoutineCard } from "../../ui/RoutineCard/RoutineCard";
import { IconsData } from "../../../../../app/icons/IconsData";
import { CustomInput } from "../../ui/CustomInput/CustomInput";
import { CustomBtn } from "../../../../../components/ui/CustomBtn/CustomBtn";

export const LogRoutine = ({ routineId, onClose, onUpdate }) => {
  const [routine, setRoutine] = useState(null);
  const [newProgress, setNewProgress] = useState("");

  useEffect(() => {
    if (routineId) {
      fetchRoutine();
    }
  }, [routineId]);

  useEffect(() => {
    if (routine) {
      setNewProgress(routine.value || 0);
    }
  }, [routine]);

  const fetchRoutine = async () => {
    try {
      const token = localStorage.getItem("token");
      const response = await fetch(
        `http://localhost:8080/customer/routines/${routineId}`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const data = await response.json();
      setRoutine(data);
    } catch (error) {
      console.error("Failed to fetch routine:", error);
    }
  };

  const updateRoutine = async () => {
    try {
      const token = localStorage.getItem("token");

      const response = await fetch(
        `http://localhost:8080/customer/routines/${routineId}`,
        {
          method: "PUT",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
          body: JSON.stringify({
            value: Number(newProgress),
          }),
        }
      );

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      await fetchRoutine();

      if (onUpdate) onUpdate();
      if (onClose) onClose();
    } catch (error) {
      console.error("Failed to update routine:", error);
    }
  };

  if (!routine) {
    return <p>Loading...</p>;
  }

  return (
    <div className={classes.logRoutineContainer}>
      <RoutineCard
        title={routine.name}
        Icon={IconsData[routine.icon] || IconsData.QuestionMarkIcon}
        userProgres={routine.value || 0}
        scope={routine.scope}
        unit={routine.units}
        value={(newProgress / routine.scope) * 100}
        pathColor={routine.color}
        trailColor="#2b2b27"
      />

      <CustomInput
        type="number"
        value={newProgress}
        onChange={(e) => setNewProgress(e.target.value)}
      />

      <CustomBtn
        text="Save"
        bgColor="#209d3dff"
        color="white"
        onClick={updateRoutine}
      />
    </div>
  );
};
