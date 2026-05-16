import React from "react";
import { createPortal } from "react-dom";

const BackDrop = ({ data, onClick }) => {
  return createPortal(
    <div
      role="presentation"
      onClick={onClick}
      className={`fixed left-0 right-0 bottom-0 z-1200 transition-all duration-200 opacity-50 bg-slate-300 ${data ? "top-16" : "top-0"}`}
    />,
    document.body
  );
};

export default BackDrop;
