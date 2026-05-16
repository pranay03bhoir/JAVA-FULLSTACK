import "./App.css";
import Products from "./components/products/Products.jsx";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import Home from "./components/home/Home.jsx";
import NavBar from "./components/shared/NavBar.jsx";
import About from "./components/About.jsx";
import ContactUs from "./components/ContactUs.jsx";
import { Toaster } from "react-hot-toast";
import Cart from "./components/cart/Cart.jsx";
import Login from "./components/auth/Login.jsx";
import PrivateRoute from "./components/PrivateRoute.jsx";
import Register from "./components/auth/Register.jsx";

function App() {
  return (
    <div>
      <BrowserRouter>
        <NavBar />
        <Routes>
          <Route path={`/`} element={<Home />} />
          <Route path={`/products`} element={<Products />} />
          <Route path={`/about`} element={<About />} />
          <Route path={`/contact`} element={<ContactUs />} />
          <Route path={`/cart`} element={<Cart />} />
          <Route path={`/`} element={<PrivateRoute publicPage />}>
            <Route path={`/login`} element={<Login />}></Route>
            <Route path={`/register`} element={<Register />} />
          </Route>
        </Routes>
      </BrowserRouter>
      <Toaster position={`bottom-center`} />
    </div>
  );
}

export default App;
