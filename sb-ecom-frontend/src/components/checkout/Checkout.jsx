import { Button, Step, StepLabel, Stepper } from "@mui/material";
import React, { useEffect, useState } from "react";
import AddressInfo from "./AddressInfo";
import { useDispatch, useSelector } from "react-redux";
import { getUserAddresses } from "../../store/action";
import { FaArrowLeft } from "react-icons/fa";
import { toast } from "react-hot-toast";

const Checkout = () => {
  const [activeStep, setActiveStep] = useState(0);
  const dispatch = useDispatch();
  const steps = ["Address", "Payment Method", "Order Summary", "Payment"];
  const { address, selectedUserAddress } = useSelector((state) => state.auth);
  const { isLoading, errorMessage } = useSelector((state) => state.errors);
  const paymentMethod = false;
  const handleBackButton = () => {
    setActiveStep((prevStep) => prevStep - 1);
  };
  const handleNextButton = () => {
    if (activeStep === 0 && !selectedUserAddress) {
      toast.error("Please select checkout address before proceeding.");
      return;
    }
    if (activeStep === 1 && (!selectedUserAddress || !paymentMethod)) {
      toast.error("Please select payment method before proceeding.");
      return;
    }
    setActiveStep((prevStep) => prevStep + 1);
  };
  useEffect(() => {
    dispatch(getUserAddresses());
  }, [dispatch]);

  return (
    <div className={`py-14 min-h-[calc(100vh-100px)]`}>
      <Stepper activeStep={activeStep} alternativeLabel>
        {steps.map((label, index) => (
          <Step key={index}>
            <StepLabel>{label}</StepLabel>
          </Step>
        ))}
      </Stepper>
      <div className="mt-5">
        {activeStep === 0 && <AddressInfo address={address} />}
      </div>
      <div className="flex justify-between items-center px-8 fixed z-50 h-20 bottom-0 bg-linear-to-t from-white to-slate-50/60 left-0 w-full py-2 border-t border-slate-300 shadow-md transition-all duration-200">
        <Button
          variant="contained"
          color="primary"
          disabled={activeStep === 0}
          startIcon={<FaArrowLeft />}
          size="large"
          onClick={handleBackButton}
          sx={{
            boxShadow:
              activeStep === 0 ? "none" : "0 2px 8px 0 rgba(0,0,0,0.12)",
            borderRadius: 2,
            minWidth: 120,
            fontWeight: 600,
            letterSpacing: 1,
            textTransform: "none",
          }}
        >
          Back
        </Button>
        <div className="flex-1 text-center">
          <span className="text-slate-600 font-medium">
            Step {activeStep + 1} of {steps.length}
          </span>
        </div>
        <Button
          variant="contained"
          color="primary"
          disabled={
            errorMessage || activeStep === 0
              ? !selectedUserAddress
              : activeStep === 1
                ? !paymentMethod
                : false
          }
          endIcon={<FaArrowLeft style={{ transform: "rotate(180deg)" }} />}
          size="large"
          onClick={handleNextButton}
          sx={{
            boxShadow:
              activeStep === steps.length - 1
                ? "none"
                : "0 2px 8px 0 rgba(0,0,0,0.12)",
            borderRadius: 2,
            minWidth: 120,
            fontWeight: 600,
            letterSpacing: 1,
            textTransform: "none",
          }}
        >
          {activeStep === steps.length - 1 ? "Confirm" : "Next"}
        </Button>

        <div style={{ width: 120 }} />
      </div>
    </div>
  );
};

export default Checkout;
