package com.ecommerce.sbecom.repositories;

import com.ecommerce.sbecom.models.Category;
import com.ecommerce.sbecom.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByCategoryOrderByProductPriceAsc(Category categoryId, Pageable pageDetails);

    List<Product> findByProductName(String keyword);

    Page<Product> findByProductNameLikeIgnoreCase(String keyword, Pageable pageDetails);
}
