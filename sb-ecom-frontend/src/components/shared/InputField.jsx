import React from "react";

const InputField = ({
  label,
  id,
  type,
  errors,
  register,
  required,
  message,
  className,
  min,
  value,
  placeholder,
}) => {
  return (
    <div>
      <div className="flex flex-col gap-2 w-full">
        <label
          htmlFor={id}
          className={`${className ? className : ""}text-sm font-medium text-gray-700`}
        >
          {label}
        </label>
        <input
          type={type}
          id={id}
          placeholder={placeholder}
          {...register(id, {
            required: { value: required, message: message },
            minLength: min
              ? {
                  value: min,
                  message: `Minimum ${min} characters are required`,
                }
              : null,
            pattern:
              type === "email"
                ? {
                    value: "^[^\s@]+@[^\s@]+\.[^\s@]+$",
                    message: "Invalid email address",
                  }
                : type === "url"
                  ? {
                      value:
                        /^https?:\/\/(?:www\.)?[-a-zA-Z0-9@:%._\+~#=]{1,256}\.[a-zA-Z0-9()]{1,6}\b(?:[-a-zA-Z0-9()@:%_\+.~#?&\/=]*)$/,
                      message: "Invalid URL",
                    }
                  : null,
            value,
          })}
          className={`${className ? className : ""} w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500 sm:text-sm ${errors[id] ? "border-red-500" : "border-slate-700"}`}
        />
        {errors[id]?.message && (
          <p className="text-red-500 text-sm font-semibold mt-0">
            {errors[id]?.message}
          </p>
        )}
      </div>
    </div>
  );
};

export default InputField;
