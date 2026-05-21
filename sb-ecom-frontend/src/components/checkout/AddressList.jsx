import React from "react";
import { FaCheck, FaPen } from "react-icons/fa6";
import { FaMapMarkerAlt } from "react-icons/fa";
import { MdDelete } from "react-icons/md";
import { useDispatch, useSelector } from "react-redux";
import { selectUserCheckoutAddress } from "../../store/action";

const AddressList = ({
  addresses,
  selectedAddress,
  setSelectedAddress,
  setOpenAddressModal,
  setOpenDeleteModal,
}) => {
  const dispatch = useDispatch();
  const { selectedUserAddress } = useSelector((state) => state.auth);
  const handleAddressSelection = (address) => {
    setSelectedAddress(address);
    dispatch(selectUserCheckoutAddress(address));
  };

  const handleEditAddress = (e, address) => {
    e.stopPropagation();
    setSelectedAddress(address);
    setOpenAddressModal(true);
  };
  const handleDeleteAddress = (e, address) => {
    e.stopPropagation();
    setSelectedAddress(address);
    setOpenDeleteModal(true);
  };

  return (
    <div className="space-y-3">
      {addresses.map((address) => {
        const isSelected = selectedUserAddress?.addressId === address.addressId;

        return (
          <div
            key={address.addressId}
            onClick={() => handleAddressSelection(address)}
            className={`group relative flex gap-3 p-4 rounded-xl border-2 cursor-pointer transition-all duration-150 ${
              isSelected
                ? "border-blue-500 bg-blue-50/60 shadow-sm"
                : "border-slate-200 bg-white hover:border-slate-300 hover:shadow-sm"
            }`}
          >
            <div
              className={`mt-1 flex h-5 w-5 shrink-0 items-center justify-center rounded-full border-2 transition-colors ${
                isSelected
                  ? "border-blue-500 bg-blue-500"
                  : "border-slate-300 bg-white"
              }`}
              aria-hidden
            >
              {isSelected && <FaCheck size={10} className="text-white" />}
            </div>

            <div className="flex h-11 w-11 shrink-0 items-center justify-center rounded-xl bg-slate-100 text-slate-600 transition-colors group-hover:bg-blue-50 group-hover:text-blue-600">
              <FaMapMarkerAlt size={18} />
            </div>

            <div className="min-w-0 flex-1 space-y-0.5">
              <div className="flex items-start justify-between gap-2">
                <p className="font-semibold text-slate-800 leading-snug">
                  {address.buildingName}
                </p>
                <div className="flex gap-3">
                  <button
                    type="button"
                    onClick={(e) => handleEditAddress(e, address)}
                    className="shrink-0 rounded-lg p-1.5 text-slate-400 transition-colors hover:bg-blue-100 hover:text-blue-600 focus:outline-none focus:ring-2 focus:ring-blue-400"
                    aria-label="Edit address"
                  >
                    <FaPen size={16} />
                  </button>
                  <button
                    type="button"
                    onClick={(e) => handleDeleteAddress(e, address)}
                    className="shrink-0 rounded-lg p-1.5 text-slate-400 transition-colors hover:bg-red-50 hover:text-red-600 focus:outline-none focus:ring-2 focus:ring-red-400"
                    aria-label="Delete address"
                  >
                    <MdDelete size={16} className="text-red-600" />
                  </button>
                </div>
              </div>
              {address.street && (
                <p className="text-sm text-slate-600">{address.street}</p>
              )}
              <p className="text-sm text-slate-500">
                {[address.city, address.state].filter(Boolean).join(", ")}
                {address.pincode && ` · ${address.pincode}`}
              </p>
              {address.country && (
                <p className="text-xs font-medium uppercase tracking-wide text-slate-400">
                  {address.country}
                </p>
              )}
            </div>
          </div>
        );
      })}
    </div>
  );
};

export default AddressList;
