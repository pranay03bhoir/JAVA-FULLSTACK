import React from "react";
import ProductCard from "./shared/ProductCard.jsx";

const products = [
  {
    image:
      "https://inspireonline.in/cdn/shop/files/iPhone_13_Pro_Max_Sierra_Blue_PDP_Image_Position-1A__GBEN_3045e384-d6a0-4b26-98f3-717a5e153f7f.jpg?v=1691412534&width=1445",
    productName: "iPhone 13 Pro Max",
    description:
      "The iPhone 13 Pro Max offers exceptional performance with its A15 Bionic chip, stunning Super Retina XDR display, and advanced camera features for breathtaking photos.",
    specialPrice: 720,
    price: 780,
  },
  {
    image: "https://m.media-amazon.com/images/I/91-uC3gGWGL.jpg",
    productName: "Samsung Galaxy S21",
    description:
      "Experience the brilliance of the Samsung Galaxy S21 with its vibrant AMOLED display, powerful camera, and sleek design that fits perfectly in your hand.",
    specialPrice: 699,
    price: 799,
  },
  {
    image:
      "https://m.media-amazon.com/images/I/61OJJSFEkBL._AC_UF1000,1000_QL80_.jpg",
    productName: "Google Pixel 6",
    description:
      "The Google Pixel 6 boasts cutting-edge AI features, exceptional photo quality, and a stunning display, making it a perfect choice for Android enthusiasts.",
    price: 599,
    specialPrice: 400,
  },
];
const About = () => {
  return (
    <div className={`max-w-5xl mx-auto px-4 py-8`}>
      <h1 className={`text-slate-800 text-4xl font-bold text-center mb-12`}>
        About Us
      </h1>
      <div
        className={`flex flex-col lg:flex-row justify-between items-center mb-12`}
      >
        <div className={`w-full md:w-1/2 text-center md:text-left`}>
          <p className="text-slate-700 text-lg mb-4 leading-7">
            Welcome to our e-commerce website! We sell high-quality products
            from various categories at competitive prices. Browse our catalog,
            compare prices, and place your orders securely. Fast and reliable
            delivery options for your convenience.
          </p>
        </div>
        <div className={`w-full md:w-1/2 mb-6 md:mb-0`}>
          <img
            src={`https://repository-images.githubusercontent.com/456963513/82528385-a73f-488f-9003-513321283a6b`}
            alt={`About Us`}
            className={`w-full h-auto rounded-lg shadow-lg transform transition-transform duration-300 hover:scale-105`}
          />
        </div>
      </div>
      <div className={`py-7 space-y-8`}>
        <h1 className={`text-slate-800 text-4xl font-bold text-center mb-4`}>
          Our products
        </h1>
        <div className={`grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6`}>
          {products.map((product, index) => (
            <ProductCard
              key={index}
              productImage={product.image}
              productName={product.productName}
              productDescription={product.description}
              specialPrice={product.specialPrice}
              productPrice={product.price}
              about={true}
            />
          ))}
        </div>
      </div>
    </div>
  );
};

export default About;
