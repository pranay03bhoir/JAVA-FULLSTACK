import React from "react";
import { FaCheck } from "react-icons/fa6";
import { FaCcStripe, FaCcPaypal } from "react-icons/fa";
import { SiRazorpay } from "react-icons/si";
import { useDispatch, useSelector } from "react-redux";
import { addPaymentMethod } from "../../store/action/index.js";

const PAYMENT_METHODS = [
  {
    value: "Stripe",
    label: "Stripe",
    description: "Pay securely with credit or debit card",
    icon: FaCcStripe,
    iconColor: "text-indigo-600",
    iconBg: "bg-indigo-50",
  },
  {
    value: "Paypal",
    label: "PayPal",
    description: "Fast checkout with your PayPal account",
    icon: FaCcPaypal,
    iconColor: "text-blue-600",
    iconBg: "bg-blue-50",
  },
  {
    value: "RazorPay",
    label: "Razorpay",
    description: "UPI, cards, wallets & net banking",
    icon: SiRazorpay,
    iconColor: "text-sky-700",
    iconBg: "bg-sky-50",
  },
];

const PaymentMethod = () => {
  const dispatch = useDispatch();
  const { paymentMethod } = useSelector((state) => state.payment);

  function paymentMethodHandler(method) {
    dispatch(addPaymentMethod(method));
  }

  return (
    <div className="pt-4">
      <div className="relative p-6 rounded-lg max-w-md mx-auto">
        <h1 className="text-slate-800 text-center font-bold text-2xl">
          Select Payment Method
        </h1>
        <p className="mt-2 text-center text-sm text-slate-500">
          Choose how you&apos;d like to pay for your order
        </p>

        <div className="space-y-3 pt-6">
          {PAYMENT_METHODS.map((method) => {
            const isSelected = paymentMethod === method.value;
            const Icon = method.icon;

            return (
              <button
                key={method.value}
                type="button"
                onClick={() => paymentMethodHandler(method.value)}
                className={`group relative flex w-full gap-3 p-4 rounded-xl border-2 text-left transition-all duration-150 focus:outline-none focus:ring-2 focus:ring-blue-400 focus:ring-offset-2 ${
                  isSelected
                    ? "border-blue-500 bg-blue-50/60 shadow-sm"
                    : "border-slate-200 bg-white hover:border-slate-300 hover:shadow-sm"
                }`}
                aria-pressed={isSelected}
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

                <div
                  className={`flex h-11 w-11 shrink-0 items-center justify-center rounded-xl transition-colors ${method.iconBg} ${method.iconColor} group-hover:opacity-90`}
                >
                  <Icon size={22} />
                </div>

                <div className="min-w-0 flex-1">
                  <p className="font-semibold text-slate-800 leading-snug">
                    {method.label}
                  </p>
                  <p className="mt-0.5 text-sm text-slate-500">
                    {method.description}
                  </p>
                </div>
              </button>
            );
          })}
        </div>

        <p className="mt-6 text-center text-xs text-slate-400">
          All transactions are encrypted and secure
        </p>
      </div>
    </div>
  );
};

export default PaymentMethod;
