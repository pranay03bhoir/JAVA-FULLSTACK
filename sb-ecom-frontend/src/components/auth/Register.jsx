import React, { useState } from "react";
import { useForm } from "react-hook-form";
import { FaUserPlus } from "react-icons/fa";
import { Link, useNavigate } from "react-router-dom";
import InputField from "../shared/InputField";
import { useDispatch } from "react-redux";
import { registerNewUser } from "../../store/action";
import { toast } from "react-hot-toast";
import Spinners from "../shared/Spinners";

const Register = () => {
  const navigate = useNavigate();
  const [loader, setLoader] = useState(false);
  const dispatch = useDispatch();
  const {
    register,
    handleSubmit,
    reset,
    formState: { errors },
  } = useForm({ mode: "onTouched" });

  const registerHandler = async (data) => {
    console.log("Register click");
    dispatch(registerNewUser(data, toast, reset, navigate, setLoader));
  };

  return (
    <div
      className={`min-h-[calc(100vh-70px)] flex items-center justify-center`}
    >
      <form
        onSubmit={handleSubmit(registerHandler)}
        className={`sm:w-[450px] w-[360px] shadow-custom py-8 sm:px-8 px-4 rounded-md`}
      >
        <div className={`flex flex-col items-center justify-center space-y-4`}>
          <FaUserPlus className={`text-slate-800 text-5xl`} />
          <h1
            className={`text-slate-800 text-center font-montserrat lg:text-3xl text-2xl font-bold`}
          >
            Register here
          </h1>
        </div>
        <hr className={`mt-2 mb-5 text-black`} />
        <div className="flex flex-col gap-3">
          <InputField
            label="Username"
            required
            id="username"
            type="text"
            message={`*Username is required`}
            placeholder={"Enter your username"}
            register={register}
            errors={errors}
          />
          <InputField
            label="Email"
            required
            id="email"
            type="email"
            message={`*Email is required`}
            placeholder={"Enter your Email"}
            register={register}
            errors={errors}
          />
          <InputField
            label="Password"
            required
            id="password"
            type="password"
            min={6}
            message={`*Password is required`}
            placeholder={"Enter your password"}
            register={register}
            errors={errors}
          />
        </div>
        <button
          className="bg-button-gradient flex gap-2 items-center justify-center font-semibold text-white w-full py-2 hover:text-slate-400 transition-colors duration-100 rounded-sm my-3"
          disabled={loader}
          type="submit"
        >
          {loader ? (
            <p className="flex gap-2 items-center">
              <Spinners /> Loading...
            </p>
          ) : (
            <p>Register</p>
          )}
        </button>
        <p className="text-center text-sm text-gray-700 mt-4">
          Already have an account?{" "}
          <Link
            to="/login"
            className="text-blue-600 hover:underline font-semibold"
          >
            Login
          </Link>
        </p>
      </form>
    </div>
  );
};

export default Register;
