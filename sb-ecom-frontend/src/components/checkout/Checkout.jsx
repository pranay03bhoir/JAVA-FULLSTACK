import { Step, StepLabel, Stepper } from "@mui/material";
import React, { useEffect, useState } from "react";
import AddressInfo from "./AddressInfo";
import { useDispatch, useSelector } from "react-redux";
import { getUserAddresses } from "../../store/action";

const Checkout = () => {
  const [activeStep, useActiveStep] = useState(0);
  const dispatch = useDispatch();
  const steps = ["Address", "Payment Method", "Order Summary", "Payment"];
  const { address } = useSelector((state) => state.auth);
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
    </div>
  );
};

export default Checkout;
