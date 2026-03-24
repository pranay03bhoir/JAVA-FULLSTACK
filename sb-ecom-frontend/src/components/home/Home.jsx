import React, { useEffect } from "react";
import HeroBanner from "./HeroBanner.jsx";
import ProductCard from "../shared/ProductCard.jsx";
import { useDispatch, useSelector } from "react-redux";
import { fetchProducts } from "../../store/action/index.js";
import Loader from "../shared/Loader.jsx";
import { FaExclamationTriangle } from "react-icons/fa";

const Home = () => {
  const dispatch = useDispatch();
  const { products } = useSelector((state) => state.products);
  const { isLoading, errorMessage } = useSelector((state) => state.errors);
  useEffect(() => {
    dispatch(fetchProducts());
  }, [dispatch]);
  return (
    <div className={`lg:px-14 sm:px-8 px-4`}>
      <div className={`py-6`}>
        <HeroBanner />
      </div>
      <div className={`py-5`}>
        <div className={`flex flex-col justify-center items-center space-y-2`}>
          <h1 className={`text-slate-800 text-3xl font-bold`}>Products</h1>
          <span className={`text-slate-700`}>
            Discover our handpicked selection of our top rated items for you!!
          </span>
        </div>
        {isLoading ? (
          <Loader text={``} />
        ) : errorMessage ? (
          <div className={`flex justify-center items-center h-50`}>
            <FaExclamationTriangle className={`text-red-600 text-3xl mr-2`} />
            <span className={`text-red-600 text-lg font-medium`}>
              {errorMessage}
            </span>
          </div>
        ) : (
          <div
            className={`pb-6 pt-14 grid 2xl:grid-cols-4 lg:grid-cols-3 sm:grid-cols-2 gap-y-6 gap-x-6`}
          >
            {products &&
              products.slice(0, 8).map((item, idx) => (
                <div key={idx}>
                  <ProductCard {...item} />
                </div>
              ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default Home;
