import React from "react";
import { Link, useNavigate } from "react-router-dom";
import { MdErrorOutline } from "react-icons/md";
import { HiOutlineHome, HiOutlineArrowLeft } from "react-icons/hi";

const STATUS_TITLES = {
  400: "Bad Request",
  401: "Unauthorized",
  403: "Access Denied",
  404: "Page Not Found",
  500: "Server Error",
  503: "Service Unavailable",
};

const ErrorPage = ({ message, statusCode }) => {
  const navigate = useNavigate();
  const title =
    (statusCode && STATUS_TITLES[statusCode]) || "Something went wrong";

  return (
    <div className="flex min-h-[calc(100vh-100px)] items-center justify-center bg-custom-gradient2 px-4 py-16 font-montserrat">
      <div className="relative w-full max-w-lg overflow-hidden rounded-2xl bg-white p-8 sm:p-10 text-center shadow-xl ring-1 ring-slate-200/80">
        {statusCode && (
          <span
            aria-hidden
            className="pointer-events-none absolute -right-4 -top-6 select-none text-[7rem] font-black leading-none text-red-100 sm:text-[8rem]"
          >
            {statusCode}
          </span>
        )}

        <div className="relative mx-auto mb-6 flex h-16 w-16 items-center justify-center rounded-full bg-red-50 text-red-500 ring-8 ring-red-50/60">
          <MdErrorOutline size={34} aria-hidden />
        </div>

        {statusCode && (
          <p className="relative mb-1 text-sm font-semibold uppercase tracking-widest text-red-500">
            Error {statusCode}
          </p>
        )}

        <h2 className="relative mb-3 text-2xl font-bold text-slate-800 sm:text-3xl">
          {title}
        </h2>

        <p className="relative mx-auto mb-8 max-w-sm text-base leading-relaxed text-slate-500">
          {message || "An unexpected error occurred. Please try again later."}
        </p>

        <div className="relative flex flex-col items-center justify-center gap-3 sm:flex-row">
          <Link
            to="/"
            className="inline-flex w-full items-center justify-center gap-2 rounded-lg bg-custom-blue px-6 py-2.5 text-sm font-semibold text-white shadow-md transition hover:brightness-110 active:scale-[0.98] sm:w-auto"
          >
            <HiOutlineHome size={18} aria-hidden />
            Go to Home
          </Link>
          <button
            type="button"
            onClick={() => navigate(-1)}
            className="inline-flex w-full items-center justify-center gap-2 rounded-lg border border-slate-200 bg-white px-6 py-2.5 text-sm font-semibold text-slate-600 transition hover:border-slate-300 hover:bg-slate-50 active:scale-[0.98] sm:w-auto"
          >
            <HiOutlineArrowLeft size={18} aria-hidden />
            Go Back
          </button>
        </div>
      </div>
    </div>
  );
};

export default ErrorPage;
