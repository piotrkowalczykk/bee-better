import classes from "./LogRoutine.module.css";
import { useState, useEffect } from "react";
import { RoutineCard } from "../../ui/RoutineCard/RoutineCard";
import { IconsData } from "../../../../../app/icons/IconsData";
import { CustomInput } from "../../ui/CustomInput/CustomInput";
import { CustomBtn } from "../../../../../components/ui/CustomBtn/CustomBtn";

export const LogRoutine = ({ routineId, onClose, onUpdate, logDate }) => {
  const [routine, setRoutine] = useState(null);
  const [newProgress, setNewProgress] = useState("");
  const [loggedValue, setLoggedValue] = useState(0);

  useEffect(() => {
  if (routineId) {
    fetchRoutine();
    fetchRoutineLogForDay(); // 🔥 NOWE
  }
}, [routineId, logDate]);

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

  const fetchRoutineLogForDay = async () => {
  try {
    const token = localStorage.getItem("token");

    const formattedDate =
      logDate instanceof Date
        ? logDate.toISOString().split("T")[0]
        : logDate;

    const response = await fetch(
      `http://localhost:8080/customer/routines/logs?date=${formattedDate}`,
      { headers: { Authorization: `Bearer ${token}` } }
    );

    if (!response.ok) throw new Error("Failed to fetch routine logs");

    const data = await response.json();

    const log = data.find((l) => l.routineId === routineId);

    if (log) {
      setLoggedValue(log.value);
      setNewProgress(log.value);
    } else {
      setLoggedValue(0);
      setNewProgress(0);
    }
  } catch (err) {
    console.error(err);
  }
};

  const logRoutine = async () => {
    try {
      const token = localStorage.getItem("token");

      const response = await fetch(
        `http://localhost:8080/customer/routines/log`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
          body: JSON.stringify({
            value: Number(newProgress),
            routineId: routineId,
            logDate: logDate
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
        userProgres={loggedValue}
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
        onClick={logRoutine}
      />
    </div>
  );
};
