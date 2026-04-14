import { configureStore } from "@reduxjs/toolkit";
import { productReducer } from "./productReducer.js";
import { errorReducer } from "./errorReducer.js";
import { cartReducer } from "./cartReducer.js";
import {authReducer} from "./authReducer.js";

const user = localStorage.getItem("auth")
    ? JSON.parse(localStorage.getItem("auth"))
    : [];

const cartItems = localStorage.getItem("cartItems")
  ? JSON.parse(localStorage.getItem("cartItems"))
  : [];

const initialState = {auth:{user:user} ,carts: { cart: cartItems } };

export const store = configureStore({
  reducer: {
    products: productReducer,
    errors: errorReducer,
    carts: cartReducer,
    auth:authReducer,
  },
  preloadedState: initialState,
});

export default store;
