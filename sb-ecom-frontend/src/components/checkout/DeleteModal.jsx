import {
  Description,
  Dialog,
  DialogBackdrop,
  DialogPanel,
  DialogTitle,
} from "@headlessui/react";
import React from "react";
import { MdClose, MdWarning } from "react-icons/md";
import Spinners from "../shared/Spinners";

const DeleteModal = ({
  open,
  setOpen,
  title = "Delete address?",
  description = "This action cannot be undone.",
  message = "Are you sure you want to remove this address from your account?",
  itemName,
  onDeleteHandler,
  loader = false,
}) => {
  const handleClose = () => {
    if (!loader) setOpen(false);
  };

  return (
    <Dialog open={open} onClose={handleClose} className="relative z-50">
      <DialogBackdrop
        transition
        className="fixed inset-0 bg-black/40 backdrop-blur-sm transition-opacity data-closed:opacity-0"
      />

      <div className="fixed inset-0 flex w-screen items-center justify-center p-4">
        <DialogPanel
          transition
          className="w-full max-w-md rounded-2xl bg-white p-6 sm:p-8 shadow-xl ring-1 ring-slate-200 transition-all data-closed:scale-95 data-closed:opacity-0"
        >
          <div className="flex items-start justify-between gap-4">
            <div className="flex h-12 w-12 shrink-0 items-center justify-center rounded-full bg-red-50 text-red-600">
              <MdWarning size={26} aria-hidden />
            </div>
            <button
              type="button"
              onClick={handleClose}
              disabled={loader}
              className="rounded-lg p-1.5 text-slate-400 transition-colors hover:bg-slate-100 hover:text-slate-600 focus:outline-none focus:ring-2 focus:ring-slate-300 disabled:opacity-50 disabled:cursor-not-allowed"
              aria-label="Close dialog"
            >
              <MdClose size={22} />
            </button>
          </div>

          <div className="mt-4 space-y-2">
            <DialogTitle className="text-xl font-bold text-slate-800">
              {title}
            </DialogTitle>
            <Description className="text-sm text-slate-500">
              {description}
            </Description>
          </div>

          <p className="mt-4 text-sm leading-relaxed text-slate-600">{message}</p>

          {itemName && (
            <div className="mt-4 rounded-lg border border-red-100 bg-red-50/50 px-4 py-3">
              <p className="text-sm font-semibold text-slate-800">{itemName}</p>
            </div>
          )}

          <div className="mt-8 flex flex-col-reverse sm:flex-row sm:justify-end gap-3">
            <button
              type="button"
              onClick={handleClose}
              disabled={loader}
              className="w-full sm:w-auto px-6 py-2.5 rounded-lg border border-slate-300 text-slate-700 font-semibold text-sm hover:bg-slate-50 transition-colors duration-150 focus:outline-none focus:ring-2 focus:ring-slate-300 disabled:opacity-50 disabled:cursor-not-allowed"
            >
              Cancel
            </button>
            <button
              type="button"
              onClick={onDeleteHandler}
              disabled={loader}
              className="w-full sm:w-auto sm:min-w-[140px] px-6 py-2.5 rounded-lg bg-red-600 hover:bg-red-700 text-white font-semibold text-sm shadow-sm transition-all duration-150 focus:outline-none focus:ring-2 focus:ring-red-400 disabled:opacity-70 disabled:cursor-not-allowed flex items-center justify-center gap-2"
            >
              {loader ? (
                <>
                  <Spinners size="sm" variant="light" label="Deleting" />
                  Deleting...
                </>
              ) : (
                "Delete"
              )}
            </button>
          </div>
        </DialogPanel>
      </div>
    </Dialog>
  );
};

export default DeleteModal;
