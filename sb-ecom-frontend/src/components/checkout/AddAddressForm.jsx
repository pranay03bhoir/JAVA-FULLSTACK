import React from "react";
import { useForm } from "react-hook-form";
import InputField from "../shared/InputField";
import Spinners from "../shared/Spinners";
import { FaMapMarkerAlt } from "react-icons/fa";
import { useDispatch, useSelector } from "react-redux";
import toast from "react-hot-toast";
import { addUpdateUserAddress } from "../../store/action/index.js";

const AddAddressForm = ({ address, onCancel }) => {
  const dispatch = useDispatch();
  const { btnLoader } = useSelector((state) => state.errors);
  const {
    register,
    handleSubmit,
    reset,
    formState: { errors },
  } = useForm({
    mode: "onTouched",
    defaultValues: {
      buildingName: "",
      street: "",
      city: "",
      state: "",
      country: "India",
      pincode: "",
    },
  });

  const onSaveAddressHandler = async (data) => {
    dispatch(addUpdateUserAddress(data, toast, address?.addressId, onCancel));
    reset();
  };

  return (
    <form onSubmit={handleSubmit(onSaveAddressHandler)} className="w-full">
      <div className="flex items-start gap-4 pb-5 border-b border-slate-200">
        <div className="flex h-12 w-12 shrink-0 items-center justify-center rounded-xl bg-blue-50 text-blue-600">
          <FaMapMarkerAlt className="text-xl" />
        </div>
        <div>
          <h2 className="text-xl font-bold text-slate-800 font-montserrat">
            Add delivery address
          </h2>
          <p className="mt-1 text-sm text-slate-500">
            Where should we deliver your order?
          </p>
        </div>
      </div>

      <div className="mt-6 space-y-5">
        <section>
          <p className="mb-3 text-xs font-semibold uppercase tracking-wider text-slate-400">
            Address details
          </p>
          <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
            <div className="sm:col-span-2">
              <InputField
                label="Building / House no."
                required
                id="buildingName"
                type="text"
                message="Building or house number is required"
                placeholder="e.g. Flat 402, Sunrise Apartments"
                register={register}
                errors={errors}
              />
            </div>
            <div className="sm:col-span-2">
              <InputField
                label="Street / Area"
                required
                id="street"
                type="text"
                message="Street or area is required"
                placeholder="e.g. BKC, Bandra West"
                register={register}
                errors={errors}
              />
            </div>
            <InputField
              label="City"
              required
              id="city"
              type="text"
              message="City is required"
              placeholder="e.g. Mumbai"
              register={register}
              errors={errors}
            />
            <InputField
              label="State"
              required
              id="state"
              type="text"
              message="State is required"
              placeholder="e.g. Maharashtra"
              register={register}
              errors={errors}
            />
          </div>
        </section>

        <section>
          <p className="mb-3 text-xs font-semibold uppercase tracking-wider text-slate-400">
            Location
          </p>
          <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
            <InputField
              label="Country"
              required
              id="country"
              type="text"
              message="Country is required"
              placeholder="e.g. India"
              register={register}
              errors={errors}
            />
            <InputField
              label="Pincode"
              required
              id="pincode"
              type="text"
              min={6}
              message="Pincode is required"
              placeholder="e.g. 400001"
              register={register}
              errors={errors}
            />
          </div>
        </section>
      </div>

      <div className="mt-8 flex flex-col-reverse sm:flex-row sm:justify-end gap-3">
        {onCancel && (
          <button
            type="button"
            onClick={onCancel}
            disabled={btnLoader}
            className="w-full sm:w-auto px-6 py-2.5 rounded-lg border border-slate-300 text-slate-700 font-semibold text-sm hover:bg-slate-50 transition-colors duration-150 focus:outline-none focus:ring-2 focus:ring-slate-300 disabled:opacity-50"
          >
            Cancel
          </button>
        )}
        <button
          type="submit"
          disabled={btnLoader}
          className="w-full sm:w-auto sm:min-w-[140px] px-6 py-2.5 rounded-lg bg-blue-600 hover:bg-blue-700 text-white font-semibold text-sm shadow-sm transition-all duration-150 focus:outline-none focus:ring-2 focus:ring-blue-400 disabled:opacity-70 disabled:cursor-not-allowed flex items-center justify-center gap-2"
        >
          {btnLoader ? (
            <>
              <Spinners /> Saving...
            </>
          ) : (
            "Save address"
          )}
        </button>
      </div>
    </form>
  );
};

export default AddAddressForm;
