import React, { useEffect, useState } from "react";
import { FiArrowDown, FiArrowUp, FiRefreshCw, FiSearch } from "react-icons/fi";
import {
  FormControl,
  InputLabel,
  MenuItem,
  Select,
  Tooltip,
  Button,
} from "@mui/material";
import { useLocation, useNavigate, useSearchParams } from "react-router-dom";

const Filter = () => {
  const categories = [
    { categoryId: 1, categoryName: "Electronics" },
    { categoryId: 2, categoryName: "Clothing" },
    { categoryId: 3, categoryName: "Furniture" },
    { categoryId: 4, categoryName: "Books" },
    { categoryId: 5, categoryName: "Toys" },
  ];

  const [searchParams] = useSearchParams();
  const params = new URLSearchParams(searchParams);
  const pathName = useLocation().pathname;
  const navigate = useNavigate();

  const [category, setCategory] = useState("all");
  const [sortOrder, setSortOrder] = useState("asc");
  const [searchTerm, setSearchTerm] = useState("");

  useEffect(() => {
    const currentCategory = searchParams.get("category") || "all";
    const currentSortOrder = searchParams.get("sortby") || "asc";
    const currentSearchTerm = searchParams.get("keyword") || "";

    setCategory(currentCategory);
    setSortOrder(currentSortOrder);
    setSearchTerm(currentSearchTerm);
  }, [searchParams]);

  useEffect(() => {
    const handler = setTimeout(() => {
      if (searchTerm) {
        searchParams.set("keyword", searchTerm);
      } else {
        searchParams.delete("keyword");
      }
      navigate(`${pathName}?${searchParams.toString()}`);
    }, 700);
    return () => {
      clearTimeout(handler);
    };
  }, [searchParams, searchTerm, navigate, pathName]);

  const handleCategoryChange = (e) => {
    const selectedCategory = e.target.value;
    if (selectedCategory === "all") {
      params.delete("category");
    } else {
      params.set("category", selectedCategory);
    }
    navigate(`${pathName}?${params}`);
    setCategory(e.target.value);
  };

  const toggleSortOrder = () => {
    setSortOrder((prevOrder) => {
      const newOrder = prevOrder === "asc" ? "desc" : "asc";
      params.set("sortby", newOrder);
      navigate(`${pathName}?${params}`);
      return newOrder;
    });
  };

  function handleClearFilters() {
    navigate({ pathName: pathName });
  }

  return (
    <div
      className={`flex lg:flex-row flex-col-reverse lg:justify-between justify-center items-center gap-4`}
    >
      <div className={`relative flex items-center 2xl:w-112.5 sm:w-105 w-full`}>
        <input
          type={`text`}
          placeholder={`Search Products`}
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
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
            onClick={toggleSortOrder}
            variant={`contained`}
            color={`primary`}
            className={`flex items-center gap-2 h-10`}
          >
            Sort by
            {sortOrder === "asc" ? (
              <FiArrowUp size={20} />
            ) : (
              <FiArrowDown size={20} />
            )}
          </Button>
        </Tooltip>
        <button
          className={`flex items-center gap-2 bg-rose-900 text-white px-3 py-2 rounded-sm transition duration-300 ease-in shadow-md focus:outline-none`}
          onClick={handleClearFilters}
        >
          <FiRefreshCw />
          <span className={`font-semibold`}>Clear filter</span>
        </button>
      </div>
    </div>
  );
};

export default Filter;
