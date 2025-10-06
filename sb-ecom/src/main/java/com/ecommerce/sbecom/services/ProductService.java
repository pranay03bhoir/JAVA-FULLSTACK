package com.ecommerce.sbecom.services;

import com.ecommerce.sbecom.models.Product;
import com.ecommerce.sbecom.payload.ProductDTO;
import com.ecommerce.sbecom.payload.ProductResponse;
import org.springframework.stereotype.Service;

@Service
public interface ProductService {

    ProductDTO addProduct(Long categoryId, Product product);

    ProductResponse getAllProducts();
}

