import React, { useState } from "react";
import { Link, useLocation } from "react-router-dom";
import { FaStore } from "react-icons/fa6";
import { Badge } from "@mui/material";
import { FaShoppingCart, FaSignInAlt } from "react-icons/fa";
import { RxCross2 } from "react-icons/rx";
import { IoIosMenu } from "react-icons/io";

const NavBar = () => {
  const path = useLocation().pathname;
  const [navBarOpen, setNavBarOpen] = useState(false);
  return (
    <div
      className={`h-[70px] bg-custom-gradient text-white z-50 flex items-center sticky top-0 shadow-lg`}
    >
      <div className={`lg:px-14 sm:px-8 px-4 w-full flex justify-between items-center relative`}>
        <Link to={`/`} className={`flex items-center text-2xl font-bold hover:opacity-80 transition-opacity duration-200`}>
          <FaStore className={`mr-2 text-3xl`} />
          <span className={`font-[Poppins] hidden sm:inline`}>E-Shop</span>
          <span className={`font-[Poppins] sm:hidden text-xl`}>E-Shop</span>
        </Link>
        <ul
          className={`flex sm:flex-row flex-col sm:gap-10 sm:items-center gap-6 sm:static absolute left-0 right-0 top-[70px] sm:shadow-none shadow-xl sm:bg-transparent bg-gray-800 sm:h-auto h-fit sm:py-0 py-6 px-4 ${navBarOpen ? "opacity-100 visible translate-y-0" : "opacity-0 invisible -translate-y-2 sm:opacity-100 sm:visible sm:translate-y-0"} transition-all duration-300 ease-in-out`}
        >
          <li
            className={`font-[500] transition-all duration-150 sm:text-white text-gray-200 sm:text-base text-sm`}
          >
            <Link
              to={`/`}
              className={`${path === "/" ? "text-white font-semibold sm:after:content-[''] sm:after:block sm:after:h-0.5 sm:after:bg-white sm:after:mt-1" : "text-gray-200 hover:text-white sm:hover:text-gray-200"} transition-colors duration-200 block py-1`}
            >
              Home
            </Link>
          </li>
          <li
            className={`font-[500] transition-all duration-150 sm:text-white text-gray-200 sm:text-base text-sm`}
          >
            <Link
              to={`/about`}
              className={`${path === "/about" ? "text-white font-semibold sm:after:content-[''] sm:after:block sm:after:h-0.5 sm:after:bg-white sm:after:mt-1" : "text-gray-200 hover:text-white sm:hover:text-gray-200"} transition-colors duration-200 block py-1`}
            >
              About Us
            </Link>
          </li>
          <li
            className={`font-[500] transition-all duration-150 sm:text-white text-gray-200 sm:text-base text-sm`}
          >
            <Link
              to={`/products`}
              className={`${path === "/products" ? "text-white font-semibold sm:after:content-[''] sm:after:block sm:after:h-0.5 sm:after:bg-white sm:after:mt-1" : "text-gray-200 hover:text-white sm:hover:text-gray-200"} transition-colors duration-200 block py-1`}
            >
              Products
            </Link>
          </li>
          <li
            className={`font-[500] transition-all duration-150 sm:text-white text-gray-200 sm:text-base text-sm`}
          >
            <Link
              to={`/contact`}
              className={`${path === "/contact" ? "text-white font-semibold sm:after:content-[''] sm:after:block sm:after:h-0.5 sm:after:bg-white sm:after:mt-1" : "text-gray-200 hover:text-white sm:hover:text-gray-200"} transition-colors duration-200 block py-1`}
            >
              Contact
            </Link>
          </li>
          <li
            className={`font-[500] transition-all duration-150 sm:text-white text-gray-200 sm:text-base text-sm`}
          >
            <Link
              to={`/cart`}
              className={`${path === "/cart" ? "text-white font-semibold" : "text-gray-200 hover:text-white sm:hover:text-gray-200"} transition-colors duration-200 block py-1`}
            >
              <Badge
                showZero
                badgeContent={0}
                color={`primary`}
                overlap={`circular`}
                anchorOrigin={{ vertical: "top", horizontal: "right" }}
              >
                <FaShoppingCart size={25} />
              </Badge>
            </Link>
          </li>
          <li
            className={`font-[500] transition-all duration-150 sm:text-white text-gray-200 sm:mt-0 mt-2 sm:text-base text-sm`}
          >
            <Link
              to={`/login`}
              className={`flex items-center space-x-2 px-4 py-[6px] bg-gradient-to-r from-purple-600 to-red-500 text-white font-semibold rounded-md shadow-lg hover:from-purple-500 hover:to-red-400 transition duration-300 ease-in-out transform hover:scale-105 justify-center sm:justify-start`}
            >
              <FaSignInAlt />
              <span>Login</span>
            </Link>
          </li>
        </ul>
        <button
          onClick={() => setNavBarOpen(!navBarOpen)}
          className={`sm:hidden flex items-center justify-center w-10 h-10 rounded-lg hover:bg-white/10 transition-colors duration-200 focus:outline-none focus:ring-2 focus:ring-white/50`}
          aria-label={navBarOpen ? "Close menu" : "Open menu"}
          aria-expanded={navBarOpen}
        >
          {navBarOpen ? (
            <RxCross2 className={`text-white text-3xl`} />
          ) : (
            <IoIosMenu className={`text-white text-3xl`} />
          )}
        </button>
      </div>
    </div>
  );
};

export default NavBar;
