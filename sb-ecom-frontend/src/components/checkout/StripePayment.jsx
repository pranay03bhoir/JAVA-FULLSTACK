import React, { useEffect } from "react";
import { Alert, AlertTitle, Skeleton } from "@mui/material";
import { useDispatch, useSelector } from "react-redux";
import { Elements } from "@stripe/react-stripe-js";
import { loadStripe } from "@stripe/stripe-js";
import PaymentsForm from "./PaymentsForm.jsx";
import { createStripePaymentService } from "../../store/action/index.js";

const stripePromise = loadStripe(import.meta.env.VITE_STRIPE_PUBLISHABLE_KEY);

const StripePayment = () => {
  const dispatch = useDispatch();
  const { clientSecret } = useSelector((state) => state.auth);
  const { totalPrice } = useSelector((state) => state.carts);
  const { isLoading, errorMessage } = useSelector((state) => state.errors);
  const { user, selectUserCheckoutAddress } = useSelector(
    (state) => state.auth,
  );

  useEffect(() => {
    if (!clientSecret) {
      const sendData = {
        amount: Number(totalPrice) * 100,
        currency: "inr",
        email: user.email,
        name: `${user.username}`,
        address: selectUserCheckoutAddress,
        description: `Order for ${user.email}`,
        metadata: {
          test: "1",
        },
      };

      dispatch(createStripePaymentService(sendData));
    }
  }, []);

  if (isLoading) {
    return (
      <div className={`max-w-lg mx-auto`}>
        <Skeleton />
      </div>
    );
  }

  return (
    <>
      {clientSecret && (
        <Elements stripe={stripePromise} options={{ clientSecret }}>
          <PaymentsForm clientSecret={clientSecret} totalPrice={totalPrice} />
        </Elements>
      )}
    </>
  );
};

export default StripePayment;
