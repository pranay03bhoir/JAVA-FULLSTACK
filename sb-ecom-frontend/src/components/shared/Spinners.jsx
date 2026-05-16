import React from "react";

const SIZE_CLASSES = {
  sm: "size-4 border-2",
  md: "size-5 border-2",
  lg: "size-8 border-[3px]",
};

const VARIANT_CLASSES = {
  light: "border-white/30 border-t-white",
  dark: "border-slate-200 border-t-custom-blue",
};

const Spinners = ({
  size = "md",
  variant = "light",
  className = "",
  label = "Loading",
}) => {
  return (
    <span
      role="status"
      aria-live="polite"
      aria-busy="true"
      className={`inline-block shrink-0 rounded-full animate-spin ${SIZE_CLASSES[size]} ${VARIANT_CLASSES[variant]} ${className}`}
    >
      <span className="sr-only">{label}</span>
    </span>
  );
};

export default Spinners;
