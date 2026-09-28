import {
  PaymentElement,
  useElements,
  useStripe,
} from "@stripe/react-stripe-js";
import React, { useState } from "react";
import { Skeleton } from "@mui/material";
import Spinners from "../shared/Spinners";

const PaymentsForm = ({ clientSecret, totalPrice }) => {
  const stripe = useStripe();
  const elements = useElements();
  const [loading, setLoading] = useState(false);
  const [errorMessage, setErrorMessage] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!stripe || !elements) {
      return;
    }
    const { error: submitError } = await elements.submit();
    const { error } = await stripe.confirmPayment({
      elements,
      clientSecret,
      confirmParams: {
        return_url: `${import.meta.env.VITE_FRONTEND_URL}/order-confirm`,
      },
    });
    if (error) {
      setErrorMessage(error.message);
      return false;
    }
  };
  const paymentsElementsOptions = {
    layout: "tabs",
  };
  return (
    <form onSubmit={handleSubmit} className="max-w-lg mx-auto p-4">
      <h2 className={`text-xl font-semibold mb-4`}>Payment Information</h2>
      {loading ? (
        <Skeleton />
      ) : (
        <>
          {clientSecret && <PaymentElement options={paymentsElementsOptions} />}
          {errorMessage && (
            <div className={`text-red-500 mt-2`}>{errorMessage}</div>
          )}
          <button
            type="submit"
            disabled={!stripe || loading}
            className="mt-6 w-full px-6 py-3 rounded-lg bg-blue-600 hover:bg-blue-700 text-white font-semibold text-sm shadow-sm transition-all duration-150 focus:outline-none focus:ring-2 focus:ring-blue-400 focus:ring-offset-2 disabled:opacity-70 disabled:cursor-not-allowed disabled:hover:bg-blue-600 flex items-center justify-center gap-2"
          >
            {loading ? (
              <>
                <Spinners size="sm" variant="light" />
                Processing...
              </>
            ) : (
              `Pay ₹${Number(totalPrice).toFixed(2)}`
            )}
          </button>
        </>
      )}
    </form>
  );
};

export default PaymentsForm;
