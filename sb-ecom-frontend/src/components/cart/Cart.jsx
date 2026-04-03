import React, { useEffect } from "react";
import { MdArrowBack, MdShoppingCart } from "react-icons/md";
import { Divider } from "@mui/material";
import { Link } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import ItemContent from "./ItemContent.jsx";
import { fetchProducts } from "../../store/action/index.js";

const Cart = () => {
  const dispatch = useDispatch();
  const { cart } = useSelector((state) => state.carts);
  const newCart = { ...cart };
  newCart.totalPrice = cart?.reduce(
    (acc, cur) =>
      acc * Number(cur?.specialPrice) * Number(cur?.productQuantity),
    0,
  );
  useEffect(() => {
    dispatch(fetchProducts());
  }, []);
  if (!cart || cart.length === 0) {
    return <h1>Cart is Empty</h1>;
  }
  return (
    <div className={`lg:px-14 sm:px-8 px-4 py-10`}>
      <div className={`flex flex-col items-center mb-12`}>
        <h1
          className={`text-4xl font-bold text-gray-900 flex items-center gap-3`}
        >
          <MdShoppingCart size={36} className={`text-gray-700`} />
          Your Cart
        </h1>
        <p className={`text-lg text-gray-600 mt-2`}>All your selected items.</p>
      </div>
      <div
        className={`grid md:grid-cols-5 grid-cols-4 gap-4 pb-2 font-semibold items-center`}
      >
        <div
          className={`md:col-span-2 justify-self-start text-lg text-slate-800 lg:ps-4`}
        >
          Product
        </div>
        <div className={`justify-self-center text-lg text-slate-800`}>
          Price
        </div>
        <div className={`justify-self-center text-lg text-slate-800`}>
          Quantity
        </div>
        <div className={`justify-self-center text-lg text-slate-800`}>
          Total
        </div>
      </div>
      <Divider component={`div`} />
      <div>
        {cart &&
          cart.length > 0 &&
          cart.map((item, i) => <ItemContent key={i} {...item} />)}
      </div>
      <Divider component={`div`} />
      <div className={`max-w-sm ml-auto`}>
        <div className={`flex flex-col gap-3 mt-6`}>
          <div
            className={`flex justify-between items-center text-sm md:text-base font-semibold`}
          >
            <span>Subtotal</span>
            <span>₹70000</span>
          </div>
          <p className={`text-slate-500 text-sm`}>
            Taxes and shipping calculated at checkout.
          </p>
          <Link to={`/checkout`} className={`w-full`}>
            <button
              onClick={() => {}}
              className={`font-semibold w-full py-2 px-6 rounded-sm bg-custom-blue text-white flex items-center justify-center gap-2 hover:text-gray-300 transition duration-300`}
            >
              <MdShoppingCart size={20} />
              Checkout
            </button>
          </Link>
          <Link
            to={`/products`}
            className={`flex gap-2 items-center mt-2 text-slate-500 hover:text-slate-700 transition-colors`}
          >
            <MdArrowBack />
            <span>Continue Shopping</span>
          </Link>
        </div>
      </div>
    </div>
  );
};

export default Cart;
