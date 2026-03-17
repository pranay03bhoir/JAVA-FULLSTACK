import { FaExclamationTriangle } from "react-icons/fa";
import ProductCard from "../shared/ProductCard.jsx";
import { useDispatch, useSelector } from "react-redux";
import { useEffect } from "react";
import { fetchCategories } from "../../store/action/index.js";
import Filter from "./Filter.jsx";
import useProductFilter from "../hooks/useProductFilter.js";
import Loader from "../shared/Loader.jsx";
import PaginationComp from "../shared/PaginationComp.jsx";

const Products = () => {
  const { isLoading, errorMessage } = useSelector((state) => state.errors);
  const { products, categories, pagination } = useSelector(
    (state) => state.products,
  );
  const dispatch = useDispatch();
  useProductFilter();
  useEffect(() => {
    dispatch(fetchCategories());
  }, [dispatch]);

  return (
    <div className={`lg:px-14 sm:px-8 px-4 py-14 2xl:w-[90%] 2xl:mx-auto`}>
      <Filter categories={categories ? categories : []} />
      {isLoading ? (
        <Loader text={`Products are loading....`} />
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
          <div className={`flex justify-center pt-10`}>
            <PaginationComp {...pagination} />
          </div>
        </div>
      )}
    </div>
  );
};

export default Products;
