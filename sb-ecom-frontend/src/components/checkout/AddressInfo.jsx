import { Skeleton } from "@mui/material";
import React, { useState } from "react";
import { FaAddressBook, FaPlus } from "react-icons/fa6";
import AddressInfoModal from "./AddressInfoModal";
import AddAddressForm from "./AddAddressForm";

const AddressInfo = () => {
  const noAddressExists = true;
  const isLoading = false;
  const [openAddressModal, setOpenAddressModal] = useState(false);
  const [selectedAddress, setSelectedAddress] = useState("");
  const addNewAddressHandler = () => {
    setSelectedAddress("");
    setOpenAddressModal(true);
  };
  return (
    <div className="pt-4">
      {noAddressExists ? (
        <div className="p-6 rounded-lg max-w-md mx-auto flex flex-col items-center justify-center">
          <FaAddressBook size={50} className="text-gray-500 mb-4" />
          <h1 className="mb-2 text-slate-800 text-center font-semibold text-2xl">
            No Address Added
          </h1>
          <p className="mb-6 text-slate-800 text-center">
            Please add your address to complete the purchase
          </p>
          <button
            className="px-6 py-2 bg-blue-600 hover:bg-blue-700 text-white rounded-md font-semibold shadow transition-all duration-150 ease-in-out flex items-center gap-2 focus:outline-none focus:ring-2 focus:ring-blue-400"
            onClick={addNewAddressHandler}
          >
            <FaPlus className="inline text-white" size={20} />
            Add Address
          </button>
        </div>
      ) : (
        <div className="relative p-6 rounded-lg max-w-md mx-auto">
          <h1 className="text-slate-800 text-center font-bold text-2xl">
            Select Address
          </h1>
          {isLoading ? (
            <div className="py-8">
              <Skeleton
                variant="rectangular"
                width={410}
                height={150}
                animation="wave"
                className="rounded-lg "
              />
            </div>
          ) : (
            <div className="space-y-4 pt-6">
              <p>Address List</p>
            </div>
          )}
        </div>
      )}
      <AddressInfoModal open={openAddressModal} setOpen={setOpenAddressModal}>
        <AddAddressForm onCancel={() => setOpenAddressModal(false)} />
      </AddressInfoModal>
    </div>
  );
};

export default AddressInfo;
