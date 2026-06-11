import React from "react";
import {
  FaLocationDot,
  FaCreditCard,
  FaBagShopping,
  FaReceipt,
} from "react-icons/fa6";
import { FaCcStripe, FaCcPaypal } from "react-icons/fa";
import { SiRazorpay } from "react-icons/si";
import {
  formatPrice,
  formatPriceCalculation,
} from "../../../utils/formatPrice.js";

const PAYMENT_META = {
  Stripe: {
    label: "Stripe",
    icon: FaCcStripe,
    color: "text-indigo-600",
    bg: "bg-indigo-50",
  },
  Paypal: {
    label: "PayPal",
    icon: FaCcPaypal,
    color: "text-blue-600",
    bg: "bg-blue-50",
  },
  RazorPay: {
    label: "Razorpay",
    icon: SiRazorpay,
    color: "text-sky-700",
    bg: "bg-sky-50",
  },
};

const SectionCard = ({ icon: Icon, title, badge, children }) => (
  <div className="overflow-hidden rounded-xl border border-slate-200 bg-white shadow-sm">
    <div className="flex items-center justify-between gap-3 border-b border-slate-100 bg-slate-50/60 px-5 py-4">
      <div className="flex items-center gap-2.5">
        <div className="flex h-8 w-8 shrink-0 items-center justify-center rounded-lg bg-blue-50 text-blue-600">
          <Icon size={14} />
        </div>
        <h2 className="text-base font-semibold text-slate-800">{title}</h2>
      </div>
      {badge}
    </div>
    <div className="p-5">{children}</div>
  </div>
);

const AddressField = ({ label, value }) => (
  <div>
    <p className="text-xs font-medium uppercase tracking-wide text-slate-400">
      {label}
    </p>
    <p className="mt-0.5 text-sm font-medium text-slate-700">{value || "—"}</p>
  </div>
);

const OrderSummary = ({ totalPrice, cart, address, paymentMethod }) => {
  const payment = PAYMENT_META[paymentMethod];
  const PaymentIcon = payment?.icon ?? FaCreditCard;
  const itemCount = cart?.reduce(
    (sum, item) => sum + Number(item?.productQuantity || 0),
    0,
  );

  const getItemPrice = (item) => item?.specialPrice ?? item?.productPrice ?? 0;

  return (
    <div className="mx-auto max-w-6xl px-4 pb-28 pt-4">
      <div className="mb-8 text-center">
        <h1 className="text-2xl font-bold text-slate-800">Review Your Order</h1>
        <p className="mt-1.5 text-sm text-slate-500">
          Confirm your details before proceeding to payment
        </p>
      </div>

      <div className="grid gap-6 lg:grid-cols-12 lg:gap-8">
        <div className="space-y-5 lg:col-span-8">
          <SectionCard icon={FaLocationDot} title="Delivery Address">
            <div className="space-y-4">
              <div className="rounded-lg border border-slate-100 bg-slate-50/50 p-4">
                <p className="font-semibold text-slate-800">
                  {address?.buildingName}
                </p>
                <p className="mt-1 text-sm text-slate-600">{address?.street}</p>
                <p className="mt-1 text-sm text-slate-600">
                  {[address?.city, address?.state, address?.pincode]
                    .filter(Boolean)
                    .join(", ")}
                </p>
                <p className="mt-1 text-sm text-slate-500">{address?.country}</p>
              </div>
              <div className="grid gap-4 sm:grid-cols-2">
                <AddressField label="Building" value={address?.buildingName} />
                <AddressField label="Street / Area" value={address?.street} />
                <AddressField label="City" value={address?.city} />
                <AddressField label="State" value={address?.state} />
                <AddressField label="Country" value={address?.country} />
                <AddressField label="PIN / ZIP" value={address?.pincode} />
              </div>
            </div>
          </SectionCard>

          <SectionCard icon={FaCreditCard} title="Payment Method">
            <div className="flex items-center gap-3 rounded-xl border border-slate-200 bg-white p-4">
              <div
                className={`flex h-11 w-11 shrink-0 items-center justify-center rounded-xl ${payment?.bg ?? "bg-slate-100"} ${payment?.color ?? "text-slate-600"}`}
              >
                <PaymentIcon size={22} />
              </div>
              <div>
                <p className="font-semibold text-slate-800">
                  {payment?.label ?? paymentMethod ?? "Not selected"}
                </p>
                <p className="mt-0.5 text-sm text-slate-500">
                  Payment will be processed securely at the next step
                </p>
              </div>
            </div>
          </SectionCard>

          <SectionCard
            icon={FaBagShopping}
            title="Order Items"
            badge={
              <span className="rounded-full bg-blue-50 px-2.5 py-0.5 text-xs font-semibold text-blue-700">
                {itemCount} {itemCount === 1 ? "item" : "items"}
              </span>
            }
          >
            <div className="divide-y divide-slate-100">
              {cart?.map((item) => {
                const unitPrice = getItemPrice(item);
                const lineTotal = formatPriceCalculation(
                  item.productQuantity,
                  unitPrice,
                );

                return (
                  <div
                    key={item.productId}
                    className="flex gap-4 py-4 first:pt-0 last:pb-0"
                  >
                    <div className="relative shrink-0">
                      <img
                        src={`${import.meta.env.VITE_BACK_END_URL}/images/${item.productImage}`}
                        alt={item.productName}
                        className="h-16 w-16 rounded-lg border border-slate-200 object-cover sm:h-20 sm:w-20"
                      />
                      <span className="absolute -right-2 -top-2 flex h-5 min-w-5 items-center justify-center rounded-full bg-slate-800 px-1 text-[10px] font-bold text-white">
                        {item.productQuantity}
                      </span>
                    </div>
                    <div className="min-w-0 flex-1">
                      <p className="font-semibold leading-snug text-slate-800">
                        {item.productName}
                      </p>
                      <p className="mt-1 text-sm text-slate-500">
                        {item.productQuantity} × {formatPrice(unitPrice)}
                      </p>
                    </div>
                    <p className="shrink-0 self-center text-sm font-semibold text-slate-800 sm:text-base">
                      {formatPrice(lineTotal)}
                    </p>
                  </div>
                );
              })}
            </div>
          </SectionCard>
        </div>

        <div className="lg:col-span-4">
          <div className="lg:sticky lg:top-6">
            <div className="overflow-hidden rounded-xl border border-slate-200 bg-white shadow-sm">
              <div className="flex items-center gap-2.5 border-b border-slate-100 bg-slate-50/60 px-5 py-4">
                <div className="flex h-8 w-8 items-center justify-center rounded-lg bg-blue-50 text-blue-600">
                  <FaReceipt size={14} />
                </div>
                <h2 className="text-base font-semibold text-slate-800">
                  Price Details
                </h2>
              </div>

              <div className="space-y-3 p-5">
                <div className="flex justify-between text-sm text-slate-600">
                  <span>
                    Products ({itemCount} {itemCount === 1 ? "item" : "items"})
                  </span>
                  <span className="font-medium text-slate-800">
                    {formatPrice(totalPrice)}
                  </span>
                </div>
                <div className="flex justify-between text-sm text-slate-600">
                  <span>Delivery</span>
                  <span className="font-medium text-emerald-600">Free</span>
                </div>
                <div className="flex justify-between text-sm text-slate-600">
                  <span>Tax (0%)</span>
                  <span className="font-medium text-slate-800">
                    {formatPrice(0)}
                  </span>
                </div>

                <div className="border-t border-dashed border-slate-200 pt-3">
                  <div className="flex justify-between items-baseline">
                    <span className="text-base font-semibold text-slate-800">
                      Total Amount
                    </span>
                    <span className="text-xl font-bold text-blue-600">
                      {formatPrice(totalPrice)}
                    </span>
                  </div>
                  <p className="mt-2 text-xs text-slate-400">
                    Inclusive of all taxes. No hidden charges.
                  </p>
                </div>
              </div>

              <div className="border-t border-slate-100 bg-blue-50/40 px-5 py-3">
                <p className="text-center text-xs text-slate-500">
                  Your payment is encrypted and secure
                </p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default OrderSummary;
