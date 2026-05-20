const initialState = {
  user: null,
  address: [],
  selectedUserAddress: null,
};

export const authReducer = (state = initialState, action) => {
  switch (action.type) {
    case "LOGIN_USER":
      return { ...state, user: action.payload };
    case "USER_ADDRESS":
      return { ...state, address: action.payload };
    case "SELECT_CHECKOUT_ADDRESS":
      return { ...state, selectedUserAddress: action.payload };
    case "LOGOUT_USER":
      return { user: null, address: null };
    default:
      return state;
  }
};
