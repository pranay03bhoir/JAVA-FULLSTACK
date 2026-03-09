import React, { useState } from "react";
import { FiArrowUp, FiRefreshCw, FiSearch } from "react-icons/fi";
import {
  FormControl,
  InputLabel,
  MenuItem,
  Select,
  Tooltip,
  Button,
} from "@mui/material";

const Filter = () => {
  const categories = [
    { categoryId: 1, categoryName: "Electronics" },
    { categoryId: 2, categoryName: "Clothing" },
    { categoryId: 3, categoryName: "Furniture" },
    { categoryId: 4, categoryName: "Books" },
    { categoryId: 5, categoryName: "Toys" },
  ];
  const [category, setCategory] = useState("all");

  const handleCategoryChange = (e) => {
    setCategory(e.target.value);
  };

  return (
    <div
      className={`flex lg:flex-row flex-col-reverse lg:justify-between justify-center items-center gap-4`}
    >
      <div className={`relative flex items-center 2xl:w-112.5 sm:w-105 w-full`}>
        <input
          type={`text`}
          placeholder={`Search Products`}
          className={`border border-gray-400 text-slate-800 rounded-md py-2 pl-10 pr-4 w-full focus:outline-none focus:ring-2 focus:ring-[#1976d2]`}
        />
        <FiSearch className={`absolute left-3 text-slate-800 size-5`} />
      </div>
      <div className={`flex sm:flex-row flex-col gap-4 items-center`}>
        <FormControl
          className={`text-slate-800 border-slate-700`}
          variant={`outlined`}
          size={`small`}
        >
          <InputLabel>Category</InputLabel>
          <Select
            labelId={`category-select-label`}
            value={category}
            variant={`filled`}
            onChange={handleCategoryChange}
            label={`Category`}
            className={`min-w-[120px] text-slate-800 border-slate-700`}
          >
            <MenuItem value={`all`}>All</MenuItem>
            {categories.map((item) => (
              <MenuItem key={item.categoryId} value={item.categoryName}>
                {item.categoryName}
              </MenuItem>
            ))}
          </Select>
        </FormControl>
        <Tooltip title={`Sorted by price: asc`}>
          <Button
            variant={`contained`}
            color={`primary`}
            className={`flex items-center gap-2 h-10`}
          >
            Sort by
            <FiArrowUp size={20} />
          </Button>
        </Tooltip>
        <button
          className={`flex items-center gap-2 bg-rose-900 text-white px-3 py-2 rounded-sm transition duration-300 ease-in shadow-md focus:outline-none`}
        >
          <FiRefreshCw />
          <span className={`font-semibold`}>Clear filter</span>
        </button>
      </div>
    </div>
  );
};

export default Filter;
