const initialState = {
  user: null,
  address: [],
  clientSecret: null,
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
    case "REMOVE_CHECKOUT_ADDRESS":
      return { ...state, selectedUserAddress: null };
    case "CLIENT_SECRET":
      return { ...state, clientSecret: action.payload };
    case "REMOVE_CLIENT_SECRET_ADDRESS":
      return { ...state, clientSecret: null, selectedUserAddress: null };
    case "LOGOUT_USER":
      return { user: null, address: null };
    default:
      return state;
  }
};
