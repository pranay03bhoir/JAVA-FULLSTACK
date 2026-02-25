import { FaExclamationTriangle } from "react-icons/fa";
import ProductCard from "./ProductCard.jsx";

const Products = () => {
  const isLoading = false;
  const errorMessage = "";
  const products = [
    {
      productId: 652,
      productName: "Iphone 17 pro max",
      image:
        "https://store.storeimages.cdn-apple.com/1/as-images.apple.com/is/iphone-17-pro-finish-select-202509-6-9inch-cosmicorange?wid=5120&hei=2880&fmt=webp&qlt=90&.v=NUNzdzNKR0FJbmhKWm5YamRHb05tUzkyK3hWak1ybHhtWDkwUXVINFc0RnVrUzFnTVVSUnNLVnZUWUMxNTBGaGhsQTdPYWVGbmdIenAvNE9qYmZVYVFDb1F2RTNvUEVHRkpGaGtOSVFHak5NTEhXRE11VU1QNVo2eDJsWlpuWHQyaWthYXpzcEpXMExJLy9GTE9wWkNn&traceId=1",
      description:
        "Experience the latest in mobile technology with advanced cameras, powerful processing, and an all-day battery.",
      quantity: 10,
      price: 113000.0,
      discount: 10.0,
      specialPrice: 92499.99,
    },
    {
      productId: 654,
      productName: "MacBook Pro M5s",
      image:
        "https://store.storeimages.cdn-apple.com/1/as-images.apple.com/is/mac-macbook-pro-size-unselect-202601-gallery-1?wid=5120&hei=3280&fmt=webp&qlt=90&.v=aXlkdGF0T0RUUVdDckNLaUc0OEE0d2huNHI2YVc1MjYxWkRLa3k4U1gzZnMyWXE0MHdoZXVFRHRoTGRqNEREOHJUNGJWZ1llU1plZmhBekVhZm5NQnNqbWRhTGpRM2xxVWJRWUhSaDlCQ3A3cWhkazVFSkNaSzNwMHhJRnYza1FlODBad1VqYUZ3RW54YkRKL2hzbXVR&traceId=1",
      description:
        "Ultra-thin laptop with Apple's M2 chip, providing fast performance in a lightweight, portable design.",
      quantity: 0,
      price: 255000.0,
      discount: 20.0,
      specialPrice: 199000.0,
    },
  ];
  return (
    <div className={`lg:px-14 sm:px-8 px-4 py-14 2xl:w-[90%] 2xl:mx-auto`}>
      {isLoading ? (
        <p>Loading....</p>
      ) : errorMessage ? (
        <div className={`flex justify-center items-center h-50`}>
          <FaExclamationTriangle className={`text-red-600 text-3xl mr-2`} />
          <span className={`text-red-600 text-lg font-medium`}>
            {errorMessage}
          </span>
        </div>
      ) : (
        <div className={`min-h-175`}>
          <div
            className={`pb-6 pt-14 grid 2xl:grid-cols-4 lg:grid-cols-3 sm:grid-cols-2 gap-y-6 gap-x-6`}
          >
            {products &&
              products.map((item, idx) => (
                <div key={idx}>
                  <ProductCard {...item} />
                </div>
              ))}
          </div>
        </div>
      )}
    </div>
  );
};

export default Products;
