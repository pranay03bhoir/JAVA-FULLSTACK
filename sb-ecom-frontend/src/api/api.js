import axios from "axios";

const api = axios.create({
  baseURL: `${import.meta.env.VITE_BACK_END_URL}/api`,
  withCredentials: true,
});

api.interceptors.request.use((config) => {
  const auth = localStorage.getItem("auth");
  if (auth) {
    const { jwtToken } = JSON.parse(auth);
    if (jwtToken) {
      config.headers.Authorization = `Bearer ${jwtToken}`;
    }
  }
  return config;
});

export default api;
