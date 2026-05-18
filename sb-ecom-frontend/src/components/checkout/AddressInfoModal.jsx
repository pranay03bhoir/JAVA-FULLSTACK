import { Dialog, DialogBackdrop, DialogPanel } from "@headlessui/react";
import React from "react";

const AddressInfoModal = ({ open, setOpen, children }) => {
  return (
    <Dialog
      open={open}
      onClose={() => setOpen(false)}
      className="relative z-50"
    >
      <DialogBackdrop className="fixed inset-0 bg-black/40 backdrop-blur-sm transition-opacity" />

      <div className="fixed inset-0 flex w-screen items-center justify-center p-4">
        <DialogPanel className="w-full max-w-2xl rounded-2xl bg-white p-6 sm:p-8 shadow-xl ring-1 ring-slate-200">
          {children}
        </DialogPanel>
      </div>
    </Dialog>
  );
};

export default AddressInfoModal;
