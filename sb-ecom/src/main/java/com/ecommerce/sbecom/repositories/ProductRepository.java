package com.ecommerce.sbecom.repositories;

import com.ecommerce.sbecom.models.Category;
import com.ecommerce.sbecom.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryOrderByProductPriceAsc(Category categoryId);

    List<Product> findByProductName(String keyword);

    List<Product> findByProductNameLikeIgnoreCase(String keyword);
}
