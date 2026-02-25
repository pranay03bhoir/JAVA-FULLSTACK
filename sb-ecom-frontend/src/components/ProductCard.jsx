import React, { useState } from "react";
import { FaShoppingCart } from "react-icons/fa";

const ProductCard = ({
  productId,
  productName,
  image,
  description,
  quantity,
  price,
  discount,
  specialPrice,
}) => {
  const [openProductViewModal, setOpenProductViewModal] = useState(false);
  const buttonLoader = false;
  const [selectedViewProduct, setSelectedViewProduct] = useState({});
  const isAvailable = quantity && Number(quantity) > 0;

  const handleProductView = (product) => {
    setSelectedViewProduct(product);
    setOpenProductViewModal(!openProductViewModal);
  };

  return (
    <div
      className={`border rounded-lg shadow-xl overflow-hidden transition-shadow duration-300`}
    >
      <div
        onClick={() => {
          handleProductView({
            id: productId,
            productName,
            image,
            description,
            quantity,
            price,
            discount,
            specialPrice,
          });
        }}
        className={`w-full overflow-hidden aspect-3/2`}
      >
        <img
          className={`w-full h-full cursor-pointer transition-transform duration-300 transform hover:scale-105`}
          src={image}
          alt={productName}
        />
      </div>
      <div className={`p-4`}>
        <h2
          onClick={() => {
            handleProductView({
              id: productId,
              productName,
              image,
              description,
              quantity,
              price,
              discount,
              specialPrice,
            });
          }}
          className={`text-lg font-semibold mb-2 cursor-pointer`}
        >
          {productName}
        </h2>
        <div className={`min-h-20 max-h-20`}>
          <p className={`text-gray-600 text-sm`}>{description}</p>
        </div>
        <div className={`flex items-center justify-between`}>
          {specialPrice ? (
            <div className={`flex flex-col`}>
              <span className={`text-gray-400 line-through`}>
                ₹{Number(price).toFixed(2)}
              </span>
              <span className={`text-slate-800 font-bold text-xl`}>
                ₹{Number(specialPrice).toFixed(2)}
              </span>
            </div>
          ) : (
            <div>
              <span className={`text-gray-600 line-through`}>
                {"  "}₹{Number(price).toFixed(2)}
              </span>
            </div>
          )}
          <button
            disabled={!isAvailable || buttonLoader}
            onClick={() => {}}
            className={`bg-blue-500 text-white rounded-md p-2 transition-colors duration-200 items-center w-36 flex justify-center  ${isAvailable ? "opacity-100 hover:bg-blue-600 active:scale-95" : "opacity-70"}`}
          >
            <FaShoppingCart className={`mr-2`} />
            {isAvailable ? "Add to cart" : "Out of stock"}
          </button>
        </div>
      </div>
    </div>
  );
};

export default ProductCard;
