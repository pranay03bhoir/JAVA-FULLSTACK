import "./App.css";
import Products from "./components/products/Products.jsx";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import Home from "./components/home/Home.jsx";
import NavBar from "./components/shared/NavBar.jsx";

function App() {
  return (
    <div>
      <BrowserRouter>
        <NavBar />
        <Routes>
          <Route path={`/`} element={<Home />} />
          <Route path={`/products`} element={<Products />} />
        </Routes>
      </BrowserRouter>
    </div>
  );
}

export default App;
