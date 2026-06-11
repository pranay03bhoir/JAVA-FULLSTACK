import React from "react";
import { Alert, AlertTitle } from "@mui/material";

const PaypalPayment = () => {
  return (
    <div className={`h-96 flex justify-center items-center`}>
      <Alert
        severity="warning"
        variant={`filled`}
        style={{ maxWidth: "400px" }}
      >
        <AlertTitle>PayPal Method Unavailable</AlertTitle>
        Coming Soon.
      </Alert>
    </div>
  );
};

export default PaypalPayment;
