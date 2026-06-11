import api from "../../api/api.js";

export const fetchProducts = (queryString) => async (dispatch) => {
  try {
    dispatch({ type: "IS_FETCHING" });
    const { data } = await api.get(`/public/products?${queryString}`);
    dispatch({
      type: "FETCH_PRODUCTS",
      payload: data.content,
      pageNumber: data.pageNumber,
      pageSize: data.pageSize,
      totalElements: data.totalElements,
      totalPages: data.totalPages,
      lastPage: data.lastPage,
    });
    dispatch({ type: "IS_SUCCESS" });
  } catch (error) {
    console.error("Error: ", error);

    dispatch({
      type: "IS_ERROR",
      payload: error?.response?.data?.message || "Failed to fetch products",
    });
  }
};
export const fetchCategories = () => async (dispatch) => {
  try {
    dispatch({ type: "CATEGORY_LOADER" });
    const { data } = await api.get(`/public/categories`);
    dispatch({
      type: "FETCH_CATEGORIES",
      payload: data.content,
      pageNumber: data.pageNumber,
      pageSize: data.pageSize,
      totalElements: data.totalElements,
      totalPages: data.totalPages,
      lastPage: data.lastPage,
    });
    dispatch({ type: "CATEGORY_SUCCESS" });
  } catch (error) {
    console.error("Error: ", error);

    dispatch({
      type: "IS_ERROR",
      payload: error?.response?.data?.message || "Failed to fetch categories",
    });
  }
};
export const addToCart =
  (data, qty = 1, toast) =>
  (dispatch, getState) => {
    const { products } = getState().products;
    const getProduct = products.find(
      (item) => item.productId === data.productId,
    );
    const isQuantityExists = getProduct.productQuantity >= qty;

    if (isQuantityExists) {
      dispatch({
        type: "ADD_CART",
        payload: { ...data, productQuantity: qty },
      });
      localStorage.setItem("cartItems", JSON.stringify(getState().carts.cart));
      toast.success(`${data.productName} added to cart.`);
    } else {
      toast.error(`Out of stock`);
    }
  };
export const increaseCartQuantity =
  (data, toast, currentQuantity, setCurrentQuantity) =>
  (dispatch, getState) => {
    const { products } = getState().products;

    const getProduct = products.find(
      (item) => item.productId === data.productId,
    );

    const isQuantityExist = getProduct.productQuantity >= currentQuantity + 1;
    if (isQuantityExist) {
      const newQuantity = currentQuantity + 1;
      setCurrentQuantity(newQuantity);
      dispatch({
        type: "ADD_CART",
        payload: { ...data, productQuantity: newQuantity + 1 },
      });
      localStorage.setItem("cartItems", JSON.stringify(getState().carts.cart));
    } else {
      toast.error("Quantity reached to limit");
    }
  };

export const decreaseCartQuantity =
  (data, newQuantity) => (dispatch, getState) => {
    dispatch({
      type: "ADD_CART",
      payload: { ...data, productQuantity: newQuantity },
    });
    localStorage.setItem("cartItems", JSON.stringify(getState().carts.cart));
  };

export const removeFromCart = (data, toast) => (dispatch, getState) => {
  dispatch({ type: "REMOVE_CART", payload: data });
  toast.success(`${data.productName} is removed from cart`);
  localStorage.setItem("cartItems", JSON.stringify(getState().carts.cart));
};

export const authenticateSignInUser =
  (sendData, toast, reset, navigate, setLoader) => async (dispatch) => {
    try {
      setLoader(true);
      const { data } = await api.post("/auth/signin", sendData);
      dispatch({ type: "LOGIN_USER", payload: data });
      localStorage.setItem("auth", JSON.stringify(data));
      reset();
      toast.success("Login successful");
      navigate("/");
    } catch (error) {
      console.log(error);
      toast.error(error.response.data.message || "Internal server error");
    } finally {
      setLoader(false);
    }
  };
export const registerNewUser = async (
  sendData,
  toast,
  reset,
  navigate,
  setLoader,
) => {
  try {
    setLoader(true);
    const { data } = await api.post("/auth/signup", sendData);
    reset();
    toast.success(data?.message || "User Registered successfully");
    navigate("/login");
  } catch (error) {
    console.log(error);
    toast.error(error.response.data.message || "Internal server error");
  } finally {
    setLoader(false);
  }
};

export const logOutUser = (navigate) => (dispatch) => {
  dispatch({ type: "LOGOUT_USER" });
  localStorage.removeItem("auth");
  navigate("/login");
};

export const addUpdateUserAddress =
  (sendData, toast, addressId, onCancel) => async (dispatch) => {
    dispatch({ type: "BUTTON_LOADER" });
    try {
      if (!addressId) {
        await api.post("/addresses", sendData);
        toast.success("Address saved successfully.");
      } else {
        await api.put(`/addresses/${addressId}`, sendData);
        toast.success("Address updated successfully.");
      }
      dispatch({ type: "IS_SUCCESS" });
      await dispatch(getUserAddresses());
    } catch (e) {
      console.log(e);
      toast.error(e?.response?.data?.message || "Internal Server Error");
      dispatch({ type: "IS_ERROR", payload: null });
    } finally {
      onCancel?.();
    }
  };
export const getUserAddresses = () => async (dispatch) => {
  try {
    dispatch({ type: "IS_FETCHING" });
    const { data } = await api.get(`/user/addresses`);
    dispatch({
      type: "USER_ADDRESS",
      payload: data,
    });
    dispatch({ type: "IS_SUCCESS" });
  } catch (error) {
    console.error("Error: ", error);

    dispatch({
      type: "IS_ERROR",
      payload: error?.response?.data?.message || "Failed to fetch addresses",
    });
  }
};
export const selectUserCheckoutAddress = (address) => {
  return {
    type: "SELECT_CHECKOUT_ADDRESS",
    payload: address,
  };
};

export const deleteUserAddress =
  (addressId, toast, onSuccess) => async (dispatch, getState) => {
    dispatch({ type: "BUTTON_LOADER" });
    try {
      await api.delete(`/addresses/${addressId}`);
      toast.success("Address deleted successfully.");
      const { selectedUserAddress } = getState().auth;
      dispatch(clearCheckOutAddress());
      dispatch({ type: "IS_SUCCESS" });
      await dispatch(getUserAddresses());
      onSuccess?.();
    } catch (e) {
      console.log(e);
      toast.error(e?.response?.data?.message || "Failed to delete address");
      dispatch({ type: "IS_ERROR", payload: null });
    }
  };

export const clearCheckOutAddress = () => {
  return {
    type: "REMOVE_CHECKOUT_ADDRESS",
  };
};
export const addPaymentMethod = (method) => {
  return {
    type: "ADD_PAYMENT_METHOD",
    payload: method,
  };
};
export const createUserCart = (sendCartItems) => async (dispatch, getState) => {
  try {
    dispatch({ type: "IS_FETCHING" });
    await api.post("/carts/cart/create", sendCartItems);
    await dispatch(getUserCart());
  } catch (error) {
    console.error("Error: ", error);
    dispatch({
      type: "IS_ERROR",
      payload: error?.response?.data?.message || "Failed to create cart items",
    });
  }
};
export const getUserCart = () => async (dispatch, getState) => {
  try {
    dispatch({ type: "IS_FETCHING" });
    const { data } = await api.get("/carts/carts/users/cart");
    dispatch({
      type: "GET_USER_CART_PRODUCTS",
      payload: data.products,
      totalPrice: data.totalPrice,
      cartId: data.cartId,
    });
    localStorage.setItem("cartItems", JSON.stringify(getState().carts.cart));
    dispatch({ type: "IS_SUCCESS" });
  } catch (error) {
    console.error("Error: ", error);
    dispatch({
      type: "IS_ERROR",
      payload: error?.response?.data?.message || "Failed to fetch cart items",
    });
  }
};
