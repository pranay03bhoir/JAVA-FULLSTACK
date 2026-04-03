import React from "react";
import { FaPlus } from "react-icons/fa6";
import { FaMinus } from "react-icons/fa6";
const btnStyles = `border-[1.2px] border-slate-800 px-3 py-1 rounded-md`;
const SetQuantity = ({
  quantity,
  cardCounter,
  handleQtyIncrease,
  handleQtyDecrease,
}) => {
  return (
    <div className={`flex gap-8 items-center`}>
      {cardCounter ? null : <div className={`font-semibold`}>{quantity}</div>}
      <div
        className={`flex md:flex-row flex-col gap-4 items-center lg:text-[22px] text-sm`}
      >
        <button
          disabled={quantity <= 1}
          className={btnStyles}
          onClick={handleQtyDecrease}
        >
          <FaMinus />{" "}
        </button>
        <div className={`text-red-500`}>{quantity}</div>
        <button className={btnStyles} onClick={handleQtyIncrease}>
          {" "}
          <FaPlus />{" "}
        </button>
      </div>
    </div>
  );
};

export default SetQuantity;
