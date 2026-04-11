import { MdShoppingCart } from "react-icons/md";
import { Link } from "react-router-dom";

const CartEmpty = () => {
  return (
    <div className="flex flex-col items-center justify-center py-24 px-4">
      <MdShoppingCart className="text-6xl text-gray-300 mb-4" />
      <h2 className="text-2xl font-bold text-gray-700 mb-2">
        Your cart is empty
      </h2>
      <p className="text-gray-500 mb-6 text-center">
        Looks like you haven&#39;t added anything to your cart yet.
      </p>
      <Link
        to="/"
        className="inline-block px-6 py-2 bg-blue-600 hover:bg-blue-700 text-white rounded-md transition"
      >
        Continue Shopping
      </Link>
    </div>
  );
};

export default CartEmpty;
