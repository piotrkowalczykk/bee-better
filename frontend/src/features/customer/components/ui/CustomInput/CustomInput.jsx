import classes from "./CustomInput.module.css";

export const CustomInput = ({
  label,
  name,
  type = "text",
  value,
  onChange,
  placeholder,
  options = [],
  rows = 4,
}) => {
  return (
    <div className={classes.formInputContainer}>
      <label htmlFor={name}>{label}</label>

      {type === "select" ? (
        <select
          id={name}
          name={name}
          value={value}
          onChange={onChange}
          className={classes.formInput}
        >
          <option value="">All</option>
          {options.map((opt) => (
            <option key={opt} value={opt}>
              {opt}
            </option>
          ))}
        </select>

      ) : type === "textarea" ? (
        <textarea
          id={name}
          name={name}
          value={value}
          onChange={onChange}
          placeholder={placeholder}
          rows={rows}
          className={classes.formInput}
        />

      ) : (
        <input
          type={type}
          id={name}
          name={name}
          value={value}
          onChange={onChange}
          className={classes.formInput}
          placeholder={placeholder}
        />
      )}
    </div>
  );
};