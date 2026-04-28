import React from "react";
import { Link, useNavigate } from "react-router-dom";
import { useState } from "react";
import { useForm } from "react-hook-form";
import { AiOutlineLogin } from "react-icons/ai";
import InputField from "../shared/InputField";

const Login = () => {
  const navigate = useNavigate();
  const [loader, setLoader] = useState(false);
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm({ mode: "onTouched" });

  const loginHandler = (data) => {
    console.log(data);
  };

  return (
    <div
      className={`min-h-[calc(100vh-70px)] flex items-center justify-center`}
    >
      <form
        onSubmit={handleSubmit(loginHandler)}
        className={`sm:w-[450px] w-[360px] shadow-custom py-8 sm:px-8 px-4 rounded-md`}
      >
        <div className={`flex flex-col items-center justify-center space-y-4`}>
          <AiOutlineLogin className={`text-slate-800 text-5xl`} />
          <h1
            className={`text-slate-800 text-center font-montserrat lg:text-3xl text-2xl font-bold`}
          >
            Login here
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
            label="Password"
            required
            id="password"
            type="password"
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
          {loader ? <p>Loading...</p> : <p>Login</p>}
        </button>
        <p className="text-center text-sm text-gray-700 mt-4">
          Don't have an account?{" "}
          <Link
            to="/register"
            className="text-blue-600 hover:underline font-semibold"
          >
            Signup
          </Link>
        </p>
      </form>
    </div>
  );
};

export default Login;
